package us.vario.greg.ghnotes

import com.beust.jcommander.Parameter

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 12:04 PM
 */
class IssuesCommand {
    @Parameter(names = "-milestone",description = "milestone", required = true)
    String milestone
    @Parameter(names = "-state", description = "state", required = true)
    String state
    @Parameter(names = "-tags",description = "comma-separated tags")
    List<String> tags
}
