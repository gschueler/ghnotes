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

    @Option(shortName = "u", description = "Github Username")
    String getUser()

    @Option(shortName = "p", description = "Github Password/authkey")
    String getPassword()


}
