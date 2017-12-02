package org.teinelund.application.controller;

import org.teinelund.application.controller.domain.MavenPomFile;

import java.io.IOException;
import java.nio.file.Path;

public interface PomFileReader {
    public MavenPomFile readPomFile(Path mavenPomFilePath) throws IOException;
}
