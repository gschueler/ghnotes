package us.schueler.greg.ghnotes.options

import com.lexicalscope.jewel.cli.Option

/**
 * @author greg
 * @since 7/13/17
 */
interface GithubOrgOptions {

    @Option(shortName = "o", description = "Github Organization name")
    String getOrg()


    @Option(shortName = "p", description = "Github Project name")
    String getProject()
}