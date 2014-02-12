Github Release Notes Generator
----------

    $ ./gradlew installApp
    $ build/install/ghnotes/bin/ghnotes -h
      Usage: ghnotes [options] [command] [command options]
        Options:
          -h, --help
             Display this help text
             Default: false
        * -org
             Github Organization name
          -password
             Github Password/authkey (optional)
        * -proj
             Github Project name
          -user
             Github Username (optional)
        Commands:
          issues      Usage: issues [options]
              Options:
              * -milestone
                   Project Milestone name
              * -state
                   Issue state to find
                -tags
                   Issue tags to match, comma-separated

Example:

    $ build/install/ghnotes/bin/ghnotes -proj rundeck -org rundeck issues -milestone 2.x -state open
