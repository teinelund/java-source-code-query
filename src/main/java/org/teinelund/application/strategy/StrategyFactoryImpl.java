package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;

public class StrategyFactoryImpl implements StrategyFactory {
    @Override
    public Strategy createHelpStrategy(CommandLineOptions options) {
        return new HelpStragey(options);
    }

    @Override
    public Strategy createVersionStrategy() {
        return new VersionStrategy();
    }
}
