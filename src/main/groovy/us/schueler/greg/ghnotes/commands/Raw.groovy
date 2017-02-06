package us.schueler.greg.ghnotes.commands

import us.schueler.greg.ghnotes.Github
import us.schueler.greg.ghnotes.options.RawOptions

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 1:34 PM
 */
class Raw {
    Github github
    void formatRaw(RawOptions command){
        println github.getJson(command.path)
    }
}
