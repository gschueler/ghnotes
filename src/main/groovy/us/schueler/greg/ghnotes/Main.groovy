package us.schueler.greg.ghnotes

import com.simplifyops.toolbelt.ToolBelt
import com.simplifyops.toolbelt.input.jewelcli.JewelInput
import us.schueler.greg.ghnotes.commands.Notes
import us.schueler.greg.ghnotes.commands.Raw
import us.schueler.greg.ghnotes.commands.Repos
import us.schueler.greg.ghnotes.commands.Tags

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/11/14
 * Time: 5:26 PM
 */
class Main {
    Github github

    public static void main(String[] args) {
        ToolBelt.belt('ghnotes')
                .defaultHelpCommands()
                .ansiColorOutput(true)
                .add(new Notes())
                .add(new Repos())
//                .add(new Raw())
//                .add(new Tags())
                .commandInput(new JewelInput())
                .buckle()
                .runMain(args, true)
    }

}
