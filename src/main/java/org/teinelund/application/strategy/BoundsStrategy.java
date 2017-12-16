package org.teinelund.application.strategy;

import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.output.console.Printable;
import org.teinelund.application.strategy.domain.BoundProperty;
import org.teinelund.application.strategy.domain.MavenProject;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class BoundsStrategy extends AbstractStrategy {

    public BoundsStrategy(Set<MavenPomFile> mavenPomFiles, Printable printable) {
        super(mavenPomFiles, printable);
    }

    @Override
    public void processSourceCode(MavenProject mavenProject, Path javaSourceFilePath, List<String> sourceCodeList) {
        Set<BoundProperty> boundProperties = new HashSet<>();

        for (String line : sourceCodeList) {
            if (line.contains("System.out."))
                boundProperties.add(BoundProperty.OUTBOUND_SYSTEM);
            if (line.contains("System.err."))
                boundProperties.add(BoundProperty.OUTBOUND_SYSTEM);
            if (line.contains("System.in."))
                boundProperties.add(BoundProperty.INBOUND_SYSTEM);
            if (line.contains("main") && line.contains("void") && line.contains("static") && line.contains("String[]"))
                boundProperties.add(BoundProperty.INBOUND_VOID_STATIC_MAIN);
            if (line.contains("import javax.ws.rs"))
                boundProperties.add(BoundProperty.INBOUND_REST);
            if (line.contains("javax.ws.rs.GET") || line.contains("@GET")
                    || line.contains("javax.ws.rs.POST") || line.contains("@POST")
                    || line.contains("javax.ws.rs.PUT") || line.contains("@PUT")
                    || line.contains("javax.ws.rs.DELETE") || line.contains("@DELETE"))
                boundProperties.add(BoundProperty.INBOUND_REST);
            if (line.contains("import org.slf4j"))
                boundProperties.add(BoundProperty.OUTBOUND_LOGGING);
            if (line.contains("org.slf4j.Logger") || line.contains("Logger"))
                boundProperties.add(BoundProperty.OUTBOUND_LOGGING);
            if (line.contains("import org.springframework.web.client"))
                boundProperties.add(BoundProperty.OUTBOUND_REST);
            if (line.contains("org.springframework.web.client.RestTemplate") || line.contains("RestTemplate"))
                boundProperties.add(BoundProperty.OUTBOUND_REST);
        }
        mavenProject.setBoundProperties(javaSourceFilePath, boundProperties);

    }

}
