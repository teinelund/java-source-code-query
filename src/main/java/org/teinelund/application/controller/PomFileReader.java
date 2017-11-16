package org.teinelund.application.controller;

import java.io.IOException;
import java.nio.file.Path;

public interface PomFileReader {
    public MavenProject readPomFile(Path mavenPomFilePath) throws IOException;
}
