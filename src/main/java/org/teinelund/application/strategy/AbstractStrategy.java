package org.teinelund.application.strategy;

import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.output.console.Printable;
import org.teinelund.application.strategy.domain.MavenProject;
import org.teinelund.application.strategy.domain.MavenProjectImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractStrategy implements Strategy {

    private List<MavenProject> mavenProjects;
    private List<MavenPomFile> mavenPomFiles;
    private Path pathToSrcDirectory;
    private List<Path> javaSourceFiles;
    private Printable printable;

    public AbstractStrategy(List<MavenPomFile> mavenPomFiles, Printable printable) {
        this.mavenPomFiles = mavenPomFiles;
        this.mavenProjects = new LinkedList<>();
        this.printable = printable;
    }


    @Override
    public void process() throws IOException {
        for (MavenPomFile mavenPomFile : mavenPomFiles) {
            Path mavenProjectPath = mavenPomFile.getPath().getParent();
            this.pathToSrcDirectory = null;
            Files.list(mavenProjectPath).forEach(this::findSrcFolder);
            if (this.pathToSrcDirectory != null) {
                iterateSrcFolder(mavenPomFile, this.pathToSrcDirectory);
            }
        }
        for (MavenProject mavenProject : this.mavenProjects) {
            for (Path javaSourceFilePath : mavenProject.getJavaSourceCodePaths()) {
                BufferedReader reader = Files.newBufferedReader(javaSourceFilePath);
                List<String> javaSourceCodeContent = new LinkedList<>();
                String line = null;
                do {
                    line = reader.readLine();
                    if (line != null) {
                        javaSourceCodeContent.add(line);
                    }
                } while (line != null);
                reader.close();
                processSourceCode(mavenProject, javaSourceFilePath, javaSourceCodeContent);
            }
        }
        printable.print(mavenProjects);
    }

    void findSrcFolder(Path file) {
        if ("src".equals(file.getFileName().toString())) {
            if (Files.isDirectory(file)) {
                this.pathToSrcDirectory = file;
            }
        }
    }

    void iterateSrcFolder(MavenPomFile mavenPomFile, Path folder) {
        this.javaSourceFiles = new LinkedList<>();
        iterateFolder(folder);
        MavenProject mavenProject = new MavenProjectImpl(mavenPomFile, javaSourceFiles);
        this.mavenProjects.add(mavenProject);
    }

    void iterateFolder(Path folder) {
        try {
            Files.list(folder).forEach(this::findJavaSourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void findJavaSourceFile(Path path) {
        if (Files.isDirectory(path)) {
            iterateFolder(path);
        }
        else if (path.toString().endsWith(".java")) {
            this.javaSourceFiles.add(path);
        }
    }

    public abstract void processSourceCode(MavenProject mavenProject, Path javaSourceFilePath, List<String> sourceCodeList);

    public Path getPathToSrcDirectory() {
        return this.pathToSrcDirectory;
    }

    public List<Path> getJavaSourceFiles() {
        return this.javaSourceFiles;
    }

}
