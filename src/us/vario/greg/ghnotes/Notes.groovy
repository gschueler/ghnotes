package us.vario.greg.ghnotes

import com.beust.jcommander.JCommander

/**
 * $INTERFACE is ...
 * User: greg
 * Date: 2/11/14
 * Time: 5:26 PM
 */
class Notes {
    Github github
    public static void main(String[] args){
        def command = new NotesCommand()
        def commander = new JCommander(command);
        def issuescmd = new IssuesCommand()
        def rawcmd = new RawCommand()
        commander.addCommand("issues", issuescmd)
        commander.addCommand("raw", rawcmd)
        commander.parse(args)

        if(command.help){
            commander.setProgramName("ghnotes")
            commander.usage()
            return
        }
        if(command.debug){
            System.err.println("args: ")
            System.err.println(command + issuescmd)
        }
        issuescmd.debug=command.debug

        def gh = new Github(command.user, command.password, command.org, command.project)
        gh.debug=command.debug
        if ("issues" == commander.getParsedCommand()) {
            new Issues(github: gh).formatIssues(issuescmd)
        }else if ("raw" == commander.getParsedCommand()) {
            new Raw(github: gh).formatRaw(rawcmd)
        } else {
            System.err.println("subcommands available: issues")
        }

    }
}
