package org.teinelund.application.strategy.domain;

import org.teinelund.application.controller.domain.MavenPomFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface MavenProject {
    public MavenPomFile getMavenPomFile();
    public List<Path> getJavaSourceCodePaths();
    public void setBoundProperties(Path javaSourceCodePath, Set<BoundProperty> boundProperties);
    public Set<BoundProperty> getAllBoundaries();
}
