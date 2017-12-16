package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.commandline.OptionType;
import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.output.console.MavenProjectPrinter;

import java.util.List;
import java.util.Set;

public class StrategyFactoryImpl implements StrategyFactory {
    @Override
    public Strategy createHelpStrategy(CommandLineOptions options) {
        return new HelpStragey(options);
    }

    @Override
    public Strategy createVersionStrategy() {
        return new VersionStrategy();
    }

    @Override
    public Strategy createBoundsStrategy(Set<MavenPomFile> mavenPomFiles, Set<OptionType> optionTypes) {
        return new BoundsStrategy(mavenPomFiles, new MavenProjectPrinter());
    }

    @Override
    public Strategy createInBoundStrategy(Set<MavenPomFile> mavenPomFiles, Set<OptionType> optionTypes) {
        return null;
    }

    @Override
    public Strategy createOutBoundStrategy(Set<MavenPomFile> mavenPomFiles, Set<OptionType> optionTypes) {
        return null;
    }

}
