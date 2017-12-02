package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.controller.domain.MavenPomFile;

import java.util.List;

public interface StrategyFactory {
    public Strategy createHelpStrategy(CommandLineOptions options);
    public Strategy createVersionStrategy();
    public Strategy createBoundsStrategy(List<MavenPomFile> mavenPomFiles);
}
