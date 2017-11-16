package org.teinelund.application.controller;

import java.io.IOException;
import java.nio.file.Path;

class MavenProjectImpl implements MavenProject {
    private Path path;
    private String artifactId;
    private String groupId;
    private String version;


    public MavenProjectImpl(Path path, String groupId, String artifactId, String version) throws IOException {
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
}
