package org.teinelund.application.output.console;

import org.teinelund.application.strategy.domain.MavenProject;

import java.util.List;
import java.util.Set;

public interface Printable {
    public void print(Set<MavenProject> mavenProjects);
}
