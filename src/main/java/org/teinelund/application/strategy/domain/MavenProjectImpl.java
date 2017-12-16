package org.teinelund.application.strategy.domain;

import org.teinelund.application.controller.domain.MavenPomFile;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MavenProjectImpl implements MavenProject {

    private MavenPomFile mavenPomFile;
    private Set<Path> javaSourceCodePaths;
    private Map<String, Set<BoundProperty>> boundPropertiesMap;


    public MavenProjectImpl(MavenPomFile mavenPomFile, Set<Path> javaSourceCodePaths) {
        this.mavenPomFile = mavenPomFile;
        this.javaSourceCodePaths = javaSourceCodePaths;
        this.boundPropertiesMap = new HashMap<>();
    }

    @Override
    public MavenPomFile getMavenPomFile() {
        return this.mavenPomFile;
    }

    @Override
    public Set<Path> getJavaSourceCodePaths() {
        return Collections.unmodifiableSet(this.javaSourceCodePaths);
    }

    @Override
    public void setBoundProperties(Path javaSourceCodePath, Set<BoundProperty> boundProperties) {
        if (javaSourceCodePath != null && boundProperties != null && ! boundProperties.isEmpty())
            this.boundPropertiesMap.put(javaSourceCodePath.toAbsolutePath().toString(), boundProperties);
    }

    @Override
    public Set<BoundProperty> getAllBoundaries() {
        Set<BoundProperty> allBoundProperties = new HashSet<>();
        for (Path path : javaSourceCodePaths) {
            Set<BoundProperty> boundProperties = boundPropertiesMap.get(path.toAbsolutePath().toString());
            if (boundProperties != null) {
                allBoundProperties.addAll(boundProperties);
            }
        }
        return allBoundProperties;
    }


}
