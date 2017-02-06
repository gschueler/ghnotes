package us.schueler.greg.ghnotes.options

import com.lexicalscope.jewel.cli.Option


/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 12:04 PM
 */
interface IssuesOptions {
    @Option(longName = "milestone", shortName = 'm', description = "Project Milestone name")
    String getMilestone()

    @Option(longName = "state", shortName = 's', description = "Issue state to find")
    String getState()

    @Option(shortName = "t", description = "Issue tags to match, comma-separated")
    List<String> getTags()

    boolean isTags()

    @Option()
    boolean getDebug()

}
