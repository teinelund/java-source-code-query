package org.teinelund.application.controller;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.commandline.OptionType;
import org.teinelund.application.strategy.Strategy;
import org.teinelund.application.strategy.StrategyFactory;
import org.teinelund.application.strategy.StrategyFactoryImpl;

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
    public void selectStrategy() {
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
        List<MavenProject> mavenProjects = findMavenProject.getMavenProjects();
        for (MavenProject mavenProject : mavenProjects) {
            System.out.println("  path: " + mavenProject.getPath().toString());
            System.out.println("  [" + mavenProject.getGroupId() + " : " + mavenProject.getArtifactId() + " : " + mavenProject.getVersion() + "]");
        }
    }
}
