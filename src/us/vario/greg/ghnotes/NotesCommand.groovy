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

    @Parameter(names = "-user", description = "Username")
    String user
    @Parameter(names = "-password", description = "Password")
    String password
    @Parameter(names = "-org", description = "Organization name", required = true)
    String org
    @Parameter(names = "-proj", description = "Project name", required = true)
    String project
}
