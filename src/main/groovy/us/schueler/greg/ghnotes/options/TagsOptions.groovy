package us.schueler.greg.ghnotes.options

import com.lexicalscope.jewel.cli.Option


/**
 * Created by greg on 1/30/15.
 */
interface TagsOptions {
    @Option(shortName = 'n')
    boolean getName()

    @Option(shortName = 'p')
    String getPath()
}
