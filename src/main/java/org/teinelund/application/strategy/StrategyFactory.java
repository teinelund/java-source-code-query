package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;

public interface StrategyFactory {
    public Strategy createHelpStrategy(CommandLineOptions options);
    public Strategy createVersionStrategy();
}
