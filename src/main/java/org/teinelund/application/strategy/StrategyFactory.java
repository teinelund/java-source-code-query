package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.commandline.OptionType;
import org.teinelund.application.controller.domain.MavenPomFile;

import java.util.List;
import java.util.Set;

public interface StrategyFactory {
    public Strategy createHelpStrategy(CommandLineOptions options);
    public Strategy createVersionStrategy();
    public Strategy createBoundsStrategy(Set<MavenPomFile> mavenPomFiles, Set<OptionType> optionTypes);
    public Strategy createInBoundStrategy(Set<MavenPomFile> mavenPomFiles, Set<OptionType> optionTypes);
    public Strategy createOutBoundStrategy(Set<MavenPomFile> mavenPomFiles, Set<OptionType> optionTypes);
}
