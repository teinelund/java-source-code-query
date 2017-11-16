package org.teinelund.application.controller;

import java.nio.file.Path;

public interface MavenProject {
    public Path getPath();
    public String getArtifactId();
    public String getGroupId();
    public String getVersion();
}
