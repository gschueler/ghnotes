package us.schueler.greg.ghnotes.commands

import com.lexicalscope.jewel.cli.CommandLineInterface
import com.simplifyops.toolbelt.Command
import com.simplifyops.toolbelt.CommandOutput
import us.schueler.greg.ghnotes.Github
import us.schueler.greg.ghnotes.NotesGenerator
import us.schueler.greg.ghnotes.options.GithubOrgOptions
import us.schueler.greg.ghnotes.options.IssuesOptions
import us.schueler.greg.ghnotes.options.GithubOptions

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 12:05 PM
 */
@Command(description = "Generate Release Notes for a Github Milestone")
class Notes {


    @CommandLineInterface(application = 'format')
    interface NotesOptions extends GithubOptions, GithubOrgOptions, IssuesOptions {

    }

    @Command()
    public void format(NotesOptions opts, CommandOutput output) {
        def gh = new Github(opts.user, opts.password, opts.org, opts.project)
        gh.cachedir = new File("githubcache")
        if (!gh.cachedir.exists()) {
            gh.cachedir.mkdirs()
        }
        gh.debug = opts.debug
        def generator = new NotesGenerator(github: gh)
        generator.format(opts)
    }

}
