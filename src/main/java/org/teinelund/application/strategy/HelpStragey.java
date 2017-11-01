package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;

public class HelpStragey implements Strategy {

    private CommandLineOptions options;

    public HelpStragey(CommandLineOptions options) {
        this.options = options;
    }

    @Override
    public void process() {
        String header = "Java Source Code Query.\n\n";
        String footer = "Web: https://github.com/teinelund/java-source-code-query.";
        this.options.createHelpFormatter(header, footer);
    }
}
