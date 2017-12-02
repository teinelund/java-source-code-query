package org.teinelund.application.controller.domain;

import org.teinelund.application.controller.PomFileReader;

import java.io.IOException;
import java.nio.file.Path;

public class MavenPomFileImpl implements MavenPomFile {
    private Path path;
    private String artifactId;
    private String groupId;
    private String version;


    public MavenPomFileImpl(Path path, String groupId, String artifactId, String version) throws IOException {
        this.path = path;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (! (obj instanceof MavenPomFile))
            return false;
        MavenPomFile mavenPomFile = (MavenPomFile) obj;
        boolean result = this.artifactId == null ? mavenPomFile.getArtifactId() == null : this.artifactId.equals(mavenPomFile.getArtifactId());
        result = result & (this.groupId == null ? mavenPomFile.getGroupId() == null : this.groupId.equals(mavenPomFile.getGroupId()));
        result = result & (this.version == null ? mavenPomFile.getVersion() == null : this.version.equals(mavenPomFile.getVersion()));
        result = result & (this.path == null ? mavenPomFile.getPath() == null : this.path.equals(mavenPomFile.getPath()));
        return result;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.artifactId == null ? 0 : this.artifactId.hashCode());
        result = 31 * result + (this.groupId == null ? 0 : this.groupId.hashCode());
        result = 31 * result + (this.version == null ? 0 : this.version.hashCode());
        result = 31 * result + (this.path == null ? 0 : this.path.hashCode());
        return result;
    }
}
