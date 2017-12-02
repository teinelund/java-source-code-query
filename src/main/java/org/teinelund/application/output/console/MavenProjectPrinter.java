package org.teinelund.application.output.console;

import org.teinelund.application.strategy.domain.MavenProject;

import java.util.List;

public class MavenProjectPrinter implements Printable {
    @Override
    public void print(List<MavenProject> mavenProjects) {
        for (MavenProject mavenProject : mavenProjects) {
            System.out.println(mavenProject.getMavenPomFile().getGroupId() + " : " + mavenProject.getMavenPomFile().getArtifactId());
            System.out.println("  " + mavenProject.getMavenPomFile().getPath().toAbsolutePath().toString());
            System.out.println("  " + mavenProject.getAllBoundaries().toString());
        }
    }
}
