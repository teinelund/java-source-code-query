package org.teinelund.application.strategy;

import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.output.console.Printable;
import org.teinelund.application.strategy.domain.MavenProject;
import org.teinelund.application.strategy.domain.MavenProjectImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class AbstractStrategy implements Strategy {

    private Set<MavenPomFile> mavenPomFiles;
    private Printable printable;

    public AbstractStrategy(Set<MavenPomFile> mavenPomFiles, Printable printable) {
        this.mavenPomFiles = mavenPomFiles;
        this.printable = printable;
    }


    /**
     * For each MavenPomFile:
     * - find the folder with the name 'src'.
     * - In that folder, find all java source files.
     * - create a MavenProject object, and store the MavenPomFile object and the set of java source file in it.
     * - add the MavenProject object to the set manvenProjects.
     *
     * For each MavenProject in the set mavenProjects
     * - for each java source file in the set of java source files in MavenProject
     *   - read the java source file int a list of strings.
     *   - invoke processSourceCode
     * Invoke the print method of the Printable object.
     * @throws IOException
     */
    @Override
    public void process() throws IOException {
        Set<MavenProject> mavenProjects = new HashSet<>();
        for (MavenPomFile mavenPomFile : mavenPomFiles) {
            Path mavenProjectPath = mavenPomFile.getPath().getParent();
            Path pathToSrcFolder = findSrcFolder(mavenProjectPath);
            if (pathToSrcFolder != null) {
                mavenProjects.add(iterateSrcFolder(mavenPomFile, pathToSrcFolder));
            }
        }
        for (MavenProject mavenProject : mavenProjects) {
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

    Path findSrcFolder(Path mavenProjectFolderPath) {
        File[] filesInMavenProjectFoldert = mavenProjectFolderPath.toFile().listFiles();
        for (File file : filesInMavenProjectFoldert) {
            if ("src".equals(file.getName())) {
                if (Files.isDirectory(file.toPath())) {
                    return file.toPath();
                }
            }
        }
        return null;
    }

    MavenProject iterateSrcFolder(MavenPomFile mavenPomFile, Path folder) {
        Set<Path> javaSourceFilePaths = new HashSet<>();
        findJavaSourceFiles(folder, javaSourceFilePaths);
        MavenProject mavenProject = new MavenProjectImpl(mavenPomFile, javaSourceFilePaths);
        return mavenProject;
    }

    void findJavaSourceFiles(Path folder, Set<Path> javaSourceFilePaths) {
        File[] files = folder.toFile().listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findJavaSourceFiles(file.toPath(), javaSourceFilePaths);
            } else if (file.getName().endsWith(".java")) {
                javaSourceFilePaths.add(file.toPath());
            }
        }
    }

    public abstract void processSourceCode(MavenProject mavenProject, Path javaSourceFilePath, List<String> sourceCodeList);

}
