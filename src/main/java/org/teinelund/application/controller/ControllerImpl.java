package org.teinelund.application.controller;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.commandline.OptionType;
import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.strategy.Strategy;
import org.teinelund.application.strategy.StrategyFactory;
import org.teinelund.application.strategy.StrategyFactoryImpl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

class ControllerImpl implements Controller {

    private CommandLineOptions options;
    private Strategy strategy;
    private StrategyFactory stategyFactory;

    public ControllerImpl(CommandLineOptions options) {
        this.options = options;
        this.stategyFactory = new StrategyFactoryImpl();
    }

    @Override
    public void selectStrategy() throws IOException {
        Set<OptionType> optionTypes = options.getOptionTypes();
        if (optionTypes.contains(OptionType.HELP)) {
            this.strategy = this.stategyFactory.createHelpStrategy(this.options);
            strategy.process();
            return;
        }
        if (optionTypes.contains(OptionType.VERSION)) {
            this.strategy = this.stategyFactory.createVersionStrategy();
            strategy.process();
            return;
        }
        Set<String> projectPathNames = options.getCommandLineOptionValue(OptionType.PROJECT);
        PomFileReader reader = new PomFileReaderImpl();
        FindMavenProject findMavenProject = new FindMavenProjectImpl(projectPathNames, reader);
        Set<MavenPomFile> mavenPomFiles = findMavenProject.getMavenPomFiles();
        if (optionTypes.contains(OptionType.BOUNDS)) {
            this.strategy = this.stategyFactory.createBoundsStrategy(mavenPomFiles, optionTypes);
        }
        else if (optionTypes.contains(OptionType.INBOUND)) {
            this.strategy = this.stategyFactory.createInBoundStrategy(mavenPomFiles, optionTypes);
        }
        else if (optionTypes.contains(OptionType.OUTBOUND)) {
            this.strategy = this.stategyFactory.createOutBoundStrategy(mavenPomFiles, optionTypes);
        }
        this.strategy.process();
    }
}
