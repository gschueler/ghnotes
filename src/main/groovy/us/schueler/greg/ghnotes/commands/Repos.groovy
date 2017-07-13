package us.schueler.greg.ghnotes.commands

import com.simplifyops.toolbelt.Command
import us.schueler.greg.ghnotes.Github
import us.schueler.greg.ghnotes.options.GithubOptions

/**
 * @author greg
 * @since 4/14/17
 */

@Command()
class Repos {
    @Command()
    public void list(GithubOptions opts) {
        def gh = new Github(opts.user, opts.password, opts.org, opts.project)
        gh.cachedir = new File("githubcache")
        if (!gh.cachedir.exists()) {
            gh.cachedir.mkdirs()
        }

        gh.getRepos(visibility: 'public').each { result ->
            if (result.full_name) {
                println result.full_name
            }
//            if(command.path) {
//                println "https://github.com/${github.org}/${github.project}/raw/${it.name}/${command.path}"
//            }
        }
    }
}
