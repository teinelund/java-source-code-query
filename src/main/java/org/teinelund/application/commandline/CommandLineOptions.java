package org.teinelund.application.commandline;

import java.util.List;
import java.util.Set;

public interface CommandLineOptions {

    public Set<OptionType> getOptionTypes();

    public List<String> getCommandLineOptionValue(OptionType optionType);

}
