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
        commander.addCommand("issues", issuescmd)
        commander.parse(args)

        if(command.help){
            commander.setProgramName("ghnotes")
            commander.usage()
            return
        }

        def gh = new Github(command.user, command.password, command.org, command.project)
        if ("issues" == commander.getParsedCommand()) {
            new Issues(github: gh).formatIssues(issuescmd)
        } else {
            System.err.println("subcommands available: issues")
        }

    }
}
