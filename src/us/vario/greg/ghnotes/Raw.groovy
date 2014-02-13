package us.vario.greg.ghnotes

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 1:34 PM
 */
class Raw {
    Github github
    void formatRaw(RawCommand command){
        println github.getJson(command.path)
    }
}
