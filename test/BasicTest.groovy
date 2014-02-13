import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import spock.lang.Specification
import us.vario.greg.ghnotes.Github

class BasicTest extends Specification {

    void "test the github object constructor"() {
        def gh = new Github('myorg', 'myproj')
        expect:
        gh.pathComponents == [ORG: 'myorg', PROJECT: 'myproj']
        gh.restClient != null
        gh.defaultHeaders.size() == 1
        gh.defaultHeaders == ['Content-Type':'application/json']
        gh.defaultAccept == 'application/json'
    }

    void "test a request for milestones"() {
        def gh = new Github('myorg', 'myproj')
        def jsondata = new ByteArrayInputStream('{"data":"value"}'.bytes)

        when:
        ClientResponse response = Mock(ClientResponse) {
            1 * getEntity(InputStream.class) >> jsondata
            2 * getStatus() >> 200
        }
        WebResource.Builder builderMock = Mock(WebResource.Builder) {
            1 * get(ClientResponse.class) >> response
        }
        WebResource resourceMock = Mock(WebResource) {
            1 * accept("application/json") >> builderMock
        }
        gh.restClient = Mock(Client) {
            1 * resource(URI.create('https://api.github.com/repos/myorg/myproj/milestones')) >> resourceMock
        }
        def milestones=gh.getMilestones()

        then:
        assert milestones instanceof Map
        assert milestones.data=='value'
    }

}
