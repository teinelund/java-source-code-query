package org.teinelund.application.controller;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.commandline.OptionType;
import org.teinelund.application.strategy.HelpStragey;
import org.teinelund.application.strategy.Strategy;
import org.teinelund.application.strategy.VersionStrategy;

import java.util.Set;

public class ControllerImpl implements Controller {

    private CommandLineOptions options;
    private Strategy strategy;

    public ControllerImpl(CommandLineOptions options) {
        this.options = options;
    }

    @Override
    public void selectStrategy() {
        Set<OptionType> optionTypes = options.getOptionTypes();
        if (optionTypes.contains(OptionType.HELP)) {
            this.strategy = new HelpStragey(options);
            strategy.process();
        }
        else if (optionTypes.contains(OptionType.VERSION)) {
            this.strategy = new VersionStrategy();
            strategy.process();
        }
    }
}
