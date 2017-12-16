package org.teinelund.application.output.console;

import org.teinelund.application.strategy.domain.MavenProject;

import java.util.List;
import java.util.Set;

public class MavenProjectPrinter implements Printable {
    @Override
    public void print(Set<MavenProject> mavenProjects) {
        for (MavenProject mavenProject : mavenProjects) {
            System.out.println(mavenProject.getMavenPomFile().getGroupId() + " : " + mavenProject.getMavenPomFile().getArtifactId());
            System.out.println("  " + mavenProject.getMavenPomFile().getPath().toAbsolutePath().toString());
            System.out.println("  " + mavenProject.getAllBoundaries().toString());
        }
    }
}
