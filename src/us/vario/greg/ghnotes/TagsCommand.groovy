package us.vario.greg.ghnotes

import com.beust.jcommander.Parameter

/**
 * Created by greg on 1/30/15.
 */
class TagsCommand {
    boolean debug
    @Parameter(names = '-name')
    boolean name
    @Parameter(names = '-path')
    String path
    @Override
    public String toString() {
        return "tags";
    }
}
