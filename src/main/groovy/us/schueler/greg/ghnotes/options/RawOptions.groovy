package us.schueler.greg.ghnotes.options

import com.lexicalscope.jewel.cli.Option

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/12/14
 * Time: 1:34 PM
 */
interface RawOptions {
    @Option(shortName = 'p')
    String getPath()
}
