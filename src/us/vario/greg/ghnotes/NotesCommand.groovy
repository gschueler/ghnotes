package us.vario.greg.ghnotes

import com.beust.jcommander.Parameter

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 11:45 AM
 */
class NotesCommand {
    @Parameter
    List<String> parameters = new ArrayList<String>();

    @Parameter(names = ["-h", "--help"], description = "Display this help text", help = true)
    boolean help
    @Parameter(names = "-user", description = "Github Username (optional)")
    String user
    @Parameter(names = "-password", description = "Github Password/authkey (optional)")
    String password
    @Parameter(names = "-org", description = "Github Organization name", required = true)
    String org
    @Parameter(names = "-proj", description = "Github Project name", required = true)
    String project
}
