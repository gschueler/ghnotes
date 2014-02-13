package us.vario.greg.ghnotes

import com.beust.jcommander.Parameter

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 12:04 PM
 */
class IssuesCommand {
    @Parameter(names = "-milestone",description = "Project Milestone name", required = true)
    String milestone
    @Parameter(names = "-state", description = "Issue state to find", required = true)
    String state
    @Parameter(names = "-tags",description = "Issue tags to match, comma-separated")
    List<String> tags
    boolean debug
    @Override
    public String toString() {
        return "issues " +
                "-milestone '" + milestone + '\'' +
                " -state '" + state + '\'' +
                (tags?
                " -tags " + tags  : '')
        ;
    }
}
