package org.teinelund.application.controller;

import org.teinelund.application.controller.domain.MavenPomFile;

import java.util.List;

public interface FindMavenProject {
    public List<MavenPomFile> getMavenPomFiles();
}
