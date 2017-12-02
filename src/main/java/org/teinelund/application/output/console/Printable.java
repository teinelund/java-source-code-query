package org.teinelund.application.output.console;

import org.teinelund.application.strategy.domain.MavenProject;

import java.util.List;

public interface Printable {
    public void print(List<MavenProject> mavenProjects);
}
