package us.schueler.greg.ghnotes.options

import com.lexicalscope.jewel.cli.Option
import com.lexicalscope.jewel.cli.Unparsed


/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 11:45 AM
 */
interface GithubOptions {
    @Unparsed
    List<String> getParameters()

    @Option(shortName = "u", description = "Github Username (optional)")
    String getUser()

    @Option(shortName = "p", description = "Github Password/authkey (optional)")
    String getPassword()

    @Option(shortName = "o", description = "Github Organization name")
    String getOrg()

    @Option(shortName = "p", description = "Github Project name")
    String getProject()

}
