package us.vario.greg.ghnotes

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 12:05 PM
 */
class Issues {
    Github github
    public static void formatMarkdown(List issues) {
        issues.each { t ->
            println "* [${t.title}](${t.html_url})"
            // println "* [${t.title}](${t.html_url}) (${t.labels*.name.join(', ')})"
        }
    }

    public void formatIssues(IssuesCommand command) {
        formatMarkdown(listIssues(command))
    }
    public List listIssues(IssuesCommand command) {

        def mslist = github.getMilestones()
        def ms = mslist.find { it.title == command.milestone }
        if (!ms) {
            System.err.println("couldn't find milestone title: ${command.milestone}, milestone titles: ${mslist*.title}")
            return
        }

        def issues = github.getIssues(ms, command.state)

        if (command.tags) {
            System.err.println("searching for tags: "+command.tags)
            issues = issues.findAll { command.tags.intersect(it.labels*.name).size()>0 }
        }

        //println "milestone: ${ms.title} state=${state}${tags?' tag='+tags:''} has " + issues.size() + ' issues'
        issues
    }
}
