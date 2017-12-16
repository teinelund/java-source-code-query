package org.teinelund.application.controller;

import org.teinelund.application.controller.domain.MavenPomFile;

import java.util.List;
import java.util.Set;

public interface FindMavenProject {
    public Set<MavenPomFile> getMavenPomFiles();
}
