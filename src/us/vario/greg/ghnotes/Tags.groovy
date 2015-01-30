package us.vario.greg.ghnotes

/**
 * Created by greg on 1/30/15.
 */
class Tags {
    Github github
    public void format(TagsCommand command){

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
