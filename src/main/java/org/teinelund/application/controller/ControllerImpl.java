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
        List<String> projects = options.getCommandLineOptionValue(OptionType.PROJECT);
        PomFileReader reader = new PomFileReaderImpl();
        FindMavenProject findMavenProject = new FindMavenProjectImpl(projects, reader);
        List<MavenPomFile> mavenPomFiles = findMavenProject.getMavenPomFiles();
        //for (MavenPomFile mavenProject : mavenPomFiles) {
        //    System.out.println("  path: " + mavenProject.getPath().toString());
        //    System.out.println("  [" + mavenProject.getGroupId() + " : " + mavenProject.getArtifactId() + " : " + mavenProject.getVersion() + "]");
        //}
        if (optionTypes.contains(OptionType.BOUNDS)) {
            this.strategy = this.stategyFactory.createBoundsStrategy(mavenPomFiles);
        }
        this.strategy.process();
    }
}
