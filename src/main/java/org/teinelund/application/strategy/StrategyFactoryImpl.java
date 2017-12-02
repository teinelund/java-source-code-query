package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.output.console.MavenProjectPrinter;

import java.util.List;

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
    public Strategy createBoundsStrategy(List<MavenPomFile> mavenPomFiles) {
        return new BoundsStrategy(mavenPomFiles, new MavenProjectPrinter());
    }
}
