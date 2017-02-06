package us.schueler.greg.ghnotes.commands

import us.schueler.greg.ghnotes.Github
import us.schueler.greg.ghnotes.options.TagsOptions

/**
 * Created by greg on 1/30/15.
 */
class Tags {
    Github github
    public void format(TagsOptions command){

        github.getTags().each{
            if(command.name){
                println it.name
            }
            if(command.path) {
                println "https://github.com/${github.org}/${github.project}/raw/${it.name}/${command.path}"
            }
        }
    }
}
