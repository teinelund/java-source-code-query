package org.teinelund.application.strategy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.extension.TemporaryFolder;
import org.teinelund.application.extension.TemporaryFolderExtension;
import org.teinelund.application.output.console.Printable;
import org.teinelund.application.strategy.domain.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TemporaryFolderExtension.class)
class AbstractStrategyTest {

    private static AbstractStrategy abstractStrategy;
    private TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeAll
    public static void init() {
        abstractStrategy = new AbstractStrategyStub(null, new PrintableStub());
    }

    @Test
    public void findSrcFolderWherePathEndsWithSrc() throws IOException {
        // Initialize
        File srcFile = temporaryFolder.newFolder("project", "src");
        // Test
        abstractStrategy.findSrcFolder(srcFile.toPath());
        // Verify
        assertNotNull(abstractStrategy.getPathToSrcDirectory());
    }

    @Test
    public void findSrcFolderWherePathDoesNotEndsWithSrc() throws IOException {
        // Initialize
        File srcFile = temporaryFolder.newFolder("project", "target");
        // Test
        abstractStrategy.findSrcFolder(srcFile.toPath());
        // Verify
        assertNull(abstractStrategy.getPathToSrcDirectory());
    }

    @Test
    public void iterateSrcFolder() throws IOException {
        // Initialize
        // Test
        abstractStrategy.iterateSrcFolder(null, createProject());
        List<Path> paths = abstractStrategy.getJavaSourceFiles();
        // Verify
        assertNotNull(paths);
        assertFalse(paths.isEmpty());
        assertTrue(contains("project" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "domain" + File.separator + "Document.java", paths));
        assertTrue(contains("project" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "domain" + File.separator + "Invoice.java", paths));
        assertTrue(contains("project" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "Application.java", paths));
        assertTrue(contains("project" + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator + "ApplicationTest.java", paths));



    }

    Path createProject() throws IOException {
        File projectFile = temporaryFolder.newFolder("project");
        temporaryFolder.newFolder("project", "src", "main", "java", "domain");
        temporaryFolder.newFile("project" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "domain" + File.separator + "Document.java");
        temporaryFolder.newFile("project" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "domain" + File.separator + "Invoice.java");
        temporaryFolder.newFile("project" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "Application.java");
        temporaryFolder.newFolder("project", "target");
        temporaryFolder.newFolder("project", "src", "test", "java");
        temporaryFolder.newFile("project" + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator + "ApplicationTest.java");
        temporaryFolder.newFolder("project", "src", "main", "resource");
        temporaryFolder.newFile("project" + File.separator + "src" + File.separator + "main" + File.separator + "resource" + File.separator + "environment.properties");
        return projectFile.toPath();
    }

    boolean contains(String fileName, List<Path> paths) {
        for (Path path : paths) {
            if (path.toString().endsWith(fileName))
                return true;
        }
        return false;
    }

}

class AbstractStrategyStub extends AbstractStrategy {

    public AbstractStrategyStub(List<MavenPomFile> mavenPomFiles, Printable printable) {
        super(mavenPomFiles, printable);
    }


    @Override
    public void processSourceCode(MavenProject mavenProject, Path javaSourceFilePath, List<String> sourceCodeList) {

    }
}

class PrintableStub implements Printable {

    @Override
    public void print(List<MavenProject> mavenProjects) {

    }
}