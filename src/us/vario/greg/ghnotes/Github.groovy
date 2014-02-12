package us.vario.greg.ghnotes

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler
import com.sun.jersey.core.util.MultivaluedMapImpl
import groovy.xml.MarkupBuilder
import org.codehaus.jackson.map.ObjectMapper

import javax.ws.rs.core.MultivaluedMap

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/11/14
 * Time: 4:51 PM
 */
public class Github {
    static String BASE_URL = "https://api.github.com"
    static String ORG_PATH = "/repos/{ORG}"
    static String PROJ_PATH = ORG_PATH + "/{PROJECT}"
    static String ISSUES_PATH = "/issues"
    static String NEW_MILESTONE = "/milestones"
    static String NEW_LABEL_PATH = "/labels"
    static String ISSUE_PATH = ISSUES_PATH + "/{ISSUE}"
    static String ISSUE_COMMENTS_PATH = ISSUE_PATH + "/comments"
    static String GH_BETA_JSON_CONTENTTYPE = "application/vnd.github.beta+json"
    def mock
    def mapper = new ObjectMapper()
    Client projectRest
    def static onFailure = { response ->
        throw new RuntimeException("Request failed: ${response}: ${responseText(response)}")
    }
    private static Client mkClient(){
        DefaultClientConfig config = new DefaultClientConfig();
        config.getProperties().put(URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);
        return Client.create(config);
    }
    public static createUri(String urlString, Map comps) {
        String baseurl = urlString
        String pathurl
        String qurl
        def m = (urlString =~ /^(.+?\/?)(\{.*?)(\?.*)?$/)
        if (m.matches()) {
            baseurl = m.group(1)
            pathurl = m.group(2)
            qurl = m.group(3)
        }
        def uribuild = javax.ws.rs.core.UriBuilder.fromUri(baseurl)

        if (pathurl) {
            uribuild = uribuild.path(pathurl)
        }
        if (qurl) {
            uribuild = uribuild.replaceQuery(qurl.substring(1))
        }
        uribuild.buildFromMap(comps)
    }

    public static basicAuthHeader(user, pass) {
        ['Authorization': 'Basic ' + "${user}:${pass}".toString().bytes.encodeBase64().toString()]
    }
    Map<String,Object> defaultHeaders=[:]
    String defaultAccept
    Map pathComponents
    boolean xmlDeclaration=true
    public Github(org, project) {
        this(null, null, org, project)
    }
    public Github(user, password, org, project) {
        projectRest = mkClient()
        pathComponents= [ORG: org, PROJECT: project]
        if (user && password) {
            defaultHeaders = basicAuthHeader(user, password)
        }
        defaultHeaders['Content-Type'] = 'application/json'
        defaultAccept = 'application/json'
    }

    public serializeJson(data) {
        def dst = new ByteArrayOutputStream()
        mapper.writeValue(dst, data)
        dst.toString()
    }

    private extractNextPageUrl(response) {
        //Link: <https://api.github.com/user/repos?page=3&per_page=100>; rel="next",
        //<https://api.github.com/user/repos?page=50&per_page=100>; rel="last"
        def result = null
        if (response.headers['Link']) {
            def hdrstr = response.headers.getFirst('Link')
            def links = hdrstr.split(/,\s*/)

            links.each { l ->
                def x = l.split(/;\s*/)
                if ("rel=\"next\"" in x) {
                    result = x[0].replaceAll(/^<(.+)>$/, '$1')
                }
            }
        }
        result
    }

    private makeRequest(client, builder, Closure clos) {
        def response = builder.with(mock ?: clos)
        def onfail = onFailure
        if (onfail && (response.status < 200 || response.status >= 300)) {
            onfail(response)
        }
        response
    }

    private query(WebResource res, params = [:]) {
        if (params) {
            MultivaluedMap<String, String> qparams = new MultivaluedMapImpl();
            params.each { String k, String v ->
                qparams.putSingle(k, v)
            }
            return res.queryParams(qparams)
        }
        res
    }

    private addHeaders(WebResource.Builder builder,Map<String,Object> headers=[:]) {
        (defaultHeaders+headers).each{
            builder.header(it.key,it.value)
        }
    }

    private requestBuilder(Client client, String url, Map components, headers, params) {
        WebResource resource = client.resource(url.startsWith('http') ? url : createUri(BASE_URL + PROJ_PATH + url, pathComponents + components))
        resource = query(resource, params)
        WebResource.Builder builder = resource.accept(defaultAccept)
        addHeaders(builder, headers)
        builder
    }

    static def responseXML (ClientResponse response) {
        new XmlParser(false, true).parse(response.getEntity(InputStream.class))
    }

    static def responseJson (ClientResponse response) {
        new ObjectMapper().readValue(response.getEntity(InputStream.class), Object.class)
    }

    static def responseText (ClientResponse response) {
        response.getEntity(String.class)
    }

    private ClientResponse get(Client client, String uri,Map components=[:], Map headers=[:], Map params=[:]){
        makeRequest(client,requestBuilder(client,uri, components, headers, params)) {
            get(ClientResponse.class);
        }
    }

    private makeContent(content) {
        if (content instanceof Closure) {
            xmlContent(content)
        } else {
            content
        }
    }

    private xmlContent(Closure clos) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        if (xmlDeclaration) {
            xml.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8')
        }
        clos.delegate = xml
        clos.call()
        writer.toString()
    }

    private ClientResponse post(Client client, String uri,Map components=[:], Map headers=null, Map params=null, Object clos=null){
        def content = makeContent(clos)
        makeRequest(client,requestBuilder(client,uri, components, headers, params)) {
            post(ClientResponse.class,content);
        }
    }

    public getMilestones() {
        responseJson(get(projectRest, NEW_MILESTONE))
    }

    public getIssues(milestone = null, state = null, nextUrl = null) {
        def path
        if (nextUrl) {
            path = nextUrl
        } else {
            path= ISSUES_PATH
        }

        def params = [:]
        if (milestone) {
            params.milestone = milestone.number.toString()
        }
        if (state) {
            params.state = state.toString()
        }
        def response = get(projectRest,path,[:], params)
        def nextpage = extractNextPageUrl(response)
        if (nextpage) {
            return responseJson(response) + (getIssues(milestone, state, nextpage))
        } else {
            return responseJson(response)
        }
    }

    public getLabel(label) {
        def response = get(projectRest,NEW_LABEL_PATH,[LABEL:label])
        if(response.status==404){
            return null
        }
        responseJson(response)
    }

    public createLabel(label, color = null) {
        def data = [name: label]
        if (color) {
            data.color = color
        }
        def jsondata = serializeJson(data)
        def response = post(projectRest,NEW_LABEL_PATH,null,null,null,jsondata)
        responseJson(response)
    }

    public createIssue(title, body, milestone, labels) {
        labels.each { l ->
            if (!getLabel(l)) {
                createLabel(l)
            }
        }
        def payload = [title: title, body: body, labels: labels]
        //skip milestone

        def response = post(projectRest,ISSUES_PATH,null,null,null,serializeJson(payload))
        return responseJson(response)
    }

    public addComment(issuenum, comment) {
        def content = [body: comment]
        def response = post(projectRest,ISSUE_COMMENTS_PATH,[ISSUE:issuenum],null,null,serializeJson(content))
        return responseJson(response)
    }
}
