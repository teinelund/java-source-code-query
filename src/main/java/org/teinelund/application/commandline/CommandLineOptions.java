package org.teinelund.application.commandline;

import org.apache.commons.cli.HelpFormatter;

import java.util.List;
import java.util.Set;

public interface CommandLineOptions {

    public final String OPTION_HELP_SHORT = "h";
    public final String OPTION_HELP_LONG = "help";
    public final String OPTION_VERSION_SHORT = "v";
    public final String OPTION_VERSION_LONG = "version";
    public final String OPTION_BOUNDS_SHORT = "b";
    public final String OPTION_BOUNDS_LONG = "bounds";
    public final String OPTION_INBOUND_SHORT = "i";
    public final String OPTION_INBOUND_LONG = "inbound";
    public final String OPTION_OUTBOUND_SHORT = "o";
    public final String OPTION_OUTBOUND_LONG = "outbound";
    public final String OPTION_PROJECT_PATH_SHORT = "p";
    public final String OPTION_PROJECT_PATH_LONG = "project-path";
    public final String HELP_TEXT = "Type \"-" + OPTION_HELP_SHORT + "\" or \"--" + OPTION_HELP_LONG + "\" to display documentation.";

    public Set<OptionType> getOptionTypes();

    public List<String> getCommandLineOptionValue(OptionType optionType);

    public void createHelpFormatter(String header, String footer);

}
