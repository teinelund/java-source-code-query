package org.teinelund.application.controller.domain;

import java.nio.file.Path;

public interface MavenPomFile {
    public Path getPath();
    public String getArtifactId();
    public String getGroupId();
    public String getVersion();
}
