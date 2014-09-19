package us.vario.greg.ghnotes

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 12:05 PM
 */
class Issues {
    Github github
    IssuesCommand command
    public static void formatIssuesMarkdown(List issues) {
        if (issues) {
            println "## Issues\n"
        }
        issues.each { t ->
            println "* [${t.title}](${t.html_url})"
            // println "* [${t.title}](${t.html_url}) (${t.labels*.name.join(', ')})"
        }
    }

    public static void formatContributorsMarkdown(Map users) {
        if (users) {
            println "## Contributors\n"
        }
        users.each { author ->
            println "* ${author.value}" + (author.value != author.key ? " (${author.key})" : '')
        }
    }
    public static Map<String,String> commitAuthors(List commits){
        def authors=new HashMap<String,String>()
        ['author','committer'].each{type->
            commits.findAll{it}.each{commit->
                String key,user
                if(commit[type]){
                    key=commit[type].login
                }
                if(commit.commit?.getAt(type)?.name){
                    user= commit.commit[type].name
                }else{
                    user=key
                }
                if(key){
                    authors[key]=user
                }
            }
        }
        authors
    }

    public Map getIssueUsers(List issues) {
        def users=[:]
        issues.each{i->
            if(i.user?.login && i.user?.type=='User'){
                users[i.user.login]=i.user.login
            }else{
                System.err.println("skip user ${i.user}")
            }
        }
        users
    }
    public List getIssueCommits(List issues) {
        issues.collect{github.getCommitsForIssue(it.number)}.flatten()
    }
    public Map<String,String> getIssueContributors(List issues){
        HashMap<String,String> names=[:]
        def icommits=getIssueCommits(issues)
        def iauthors = commitAuthors(icommits)
        if (iauthors) {
            names.putAll(iauthors)
        }
        //def issueUsers=getIssueUsers(issues)
//        if(issueUsers){
//            names.putAll(issueUsers)
//        }
        def pulls = issues.findAll{it.pull_request?.html_url}//pull_request may be present but html_url is null
        if(pulls){
            pulls.each{ Map pull->
                if (github.debug) {
                    System.err.println("pull request: ${pull.number}")
                }
                def commits=github.getCommitsForPull(pull.number)
                def authors=commitAuthors(commits)
                if(authors){
                    names.putAll(authors)
                }
            }
        }
        names
    }
    public void formatContributors(){
        def list = listIssues()
        def authors = getIssueContributors(list)
        formatContributorsMarkdown(authors)
    }

    public void format() {
        def list = listIssues()
        def authors=getIssueContributors(list)
        if (command.debug) {
            println authors
        } else {
            formatContributorsMarkdown(authors)
            println()
            formatIssuesMarkdown(list)
        }
    }
    public void formatIssues() {
        def list = listIssues()
        if(command.debug){
            println list
        }else{
            formatIssuesMarkdown(list)
        }
    }
    public List listIssues() {
        def mslist = github.getMilestones()
        if(command.debug){
            System.err.println("milestones: "+mslist)
        }
        def ms = mslist.find { it.title == command.milestone }
        if (!ms) {
            System.err.println("couldn't find milestone title: ${command.milestone}, milestone titles: ${mslist*.title}")
            return
        }
        if(command.debug){
            System.err.println("getIssues("+ms.subMap(['title','url','number'])+","+command.state+")")
        }
        def issues = github.getIssues(ms, command.state)

        if (command.tags) {
            if(command.debug){
                System.err.println("Searching for tags: "+command.tags)
            }
            issues = issues.findAll { it.labels*.name.containsAll(command.tags) }
        }
        //println "milestone: ${ms.title} state=${state}${tags?' tag='+tags:''} has " + issues.size() + ' issues'
        issues
    }
}
