package us.schueler.greg.ghnotes

import com.simplifyops.toolbelt.Command
import com.simplifyops.toolbelt.CommandOutput
import us.schueler.greg.ghnotes.options.GithubOptions
import us.schueler.greg.ghnotes.options.IssuesOptions

/**
 * @author greg
 * @since 2/6/17
 */
class NotesGenerator {
    Github github

    public static void formatIssuesMarkdown(List issues, ms = null) {
        if (issues) {
            println "## Issues\n"
            if (ms) {
                println "[Milestone ${ms.title}](${ms.html_url})\n"
            }
        }
        issues.each { t ->
            println "* [${t.title.replaceAll(/([<>\[\]])/, '\\\\$1')}](${t.html_url.replaceAll(/([<>\(\)])/, '\\\\$1' + '')})"
            // println "* [${t.title}](${t.html_url}) (${t.labels*.name.join(', ')})"
        }
    }

    public static void formatContributorsMarkdown(Map users, label = "Contributors") {
        if (users) {
            println "## ${label}\n"
        }
        users.collect { author ->
            "${author.value}" + (author.value != author.key ? " (${author.key})" : '')
        }.sort().each { text ->
            println "* ${text}"
        }
    }

    public static void formatDescriptionMarkdown(Map milestone, label = "Contributors") {
        println "## Notes\n"
        if (milestone.description) {
            println milestone.description
            println ''
        } else {
            println "(Enter notes here)"
        }
    }

    public static Map<String, String> commitAuthors(List commits) {
        def authors = new HashMap<String, String>()
        ['author', 'committer'].each { type ->
            commits.each { commit ->
                String key, user
                if (commit[type]) {
                    key = commit[type].login
                }
                if (commit.commit?.getAt(type)?.name) {
                    user = commit.commit[type].name
                }
                if (key && !user) {
                    user = key
                } else if (user && !key) {
                    key = user
                }
                if (key) {
                    authors[key] = user
                }
            }
        }
        authors
    }

    public Map getIssueUsers(List issues) {
        def users = [:]
        issues.each { i ->
            if (i.user?.login && i.user?.type == 'User') {
                users[i.user.login] = i.user.login
            } else {
                System.err.println("skip user ${i.user}")
            }
        }
        users
    }

    public List getIssueCommits(List issues) {
        issues.collect { github.getCommitsForIssue(it.number) }.flatten()
    }

    public Map<String, String> getIssueContributors(List issues,List<String> ignoreAuthors=[]) {
        HashMap<String, String> names = [:]
        def icommits = getIssueCommits(issues)
        def iauthors = commitAuthors(icommits)
        if (iauthors) {
            names.putAll(iauthors)
        }
        //def issueUsers=getIssueUsers(issues)
//        if(issueUsers){
//            names.putAll(issueUsers)
//        }
        def pulls = issues.findAll { it.pull_request?.html_url }//pull_request may be present but html_url is null
        if (pulls) {
            pulls.each { Map pull ->
                if (github.debug) {
                    System.err.println("pull request: ${pull.number}")
                }
                def commits = github.getCommitsForPull(pull.number)
                def authors = commitAuthors(commits)
                if (authors) {
                    names.putAll(authors)
                }
            }
        }
        if(ignoreAuthors){
            ignoreAuthors.each{names.remove(it)}
        }
        names
    }

    public Map<String, String> getIssueReporters(List issues) {
        HashMap<String, String> names = [:]
        issues.each {
            names[it.user.login] = it.user.login
        }

        names
    }

    public void formatContributors(IssuesOptions options) {
        def list = listIssues(options)
        def authors = getIssueContributors(list)
        formatContributorsMarkdown(authors)
    }


    public void format(IssuesOptions options) {
        def list = listIssues(options)
        def authors = getIssueContributors(list,options.getIgnoreAuthors())
        def reporters = getIssueReporters(list)
        if (options.debug) {
            println authors
        } else {
            def milestone = getMilestone(options)
            formatDescriptionMarkdown(milestone)
            formatContributorsMarkdown(authors)
            println()
            formatContributorsMarkdown(reporters, "Bug Reporters")
            println()
            formatIssuesMarkdown(list, milestone)
        }
    }

    public void formatIssues(IssuesOptions options) {
        def list = listIssues(options)
        if (options.debug) {
            println list
        } else {
            formatIssuesMarkdown(list, getMilestone(options))
        }
    }

    public Map getMilestone(IssuesOptions options) {
        def mslist = github.getMilestones()
        if (options.debug) {
            System.err.println("milestones: " + mslist)
        }
        def ms = mslist.find { it.title == options.milestone }
        if (!ms) {
            System.err.println(
                    "couldn't find milestone title: ${options.milestone}, milestone titles: ${mslist*.title}"
            )
            return
        }
        if (options.debug) {
            System.err.println("getIssues(" + ms.subMap(['title', 'url', 'number']) + "," + options.state + ")")
        }
        ms
    }

    public List listIssues(IssuesOptions options) {
        def ms = getMilestone(options)
        if (!ms) {
            return
        }
        def issues = github.getIssues(ms, options.state)

        if (options.tags) {
            if (options.debug) {
                System.err.println("Searching for tags: " + options.tags)
            }
            issues = issues.findAll { it.labels*.name.containsAll(options.tags) }
        }
        if(options.ignoreTags){
            if (options.debug) {
                System.err.println("Excluding tags: " + options.getIgnoreTags())
            }
            issues = issues.findAll { !it.labels*.name.any{options.getIgnoreTags().contains(it)} }
        }
        //println "milestone: ${ms.title} state=${state}${tags?' tag='+tags:''} has " + issues.size() + ' issues'
        issues
    }
}
