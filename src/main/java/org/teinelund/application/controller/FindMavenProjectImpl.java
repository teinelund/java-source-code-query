package org.teinelund.application.controller;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * The aim for this class is to find Maven projects that
 * 1) has a 'pom.xml' file
 * 2) has a 'src'-folder with '*.java' files in it.
 *
 * @author Henrik Teinelund
 */
class FindMavenProjectImpl implements FindMavenProject {

    private List<String> pathNameList;
    private PomFileReader pomFileReader;
    private Path pathToPomXmlFile;
    private Path pathToSrcDirectory;
    private List<MavenProject> mavenProjects;

    public FindMavenProjectImpl(List<String> pathNameList, PomFileReader pomFileReader) {
        this.pathNameList = pathNameList;
        this.pomFileReader = pomFileReader;
        this.mavenProjects = new LinkedList<>();
        for (String pathName : pathNameList) {
            try {
                List<MavenProject> mavenProjectList = findMavenProjects(pathName);
                if (mavenProjectList != null && !mavenProjectList.isEmpty()) {
                    mavenProjects.addAll(mavenProjectList);
                }
            }
            catch (IOException e) {
                System.err.println("IOException for path name " + pathName + ". " + e.getMessage());
                System.err.println(e);
            }
        }
    }

    @Override
    public List<MavenProject> getMavenProjects() {
        return this.mavenProjects;
    }

    List<MavenProject> findMavenProjects(String pathName) throws IOException {
        Path path = Paths.get(pathName);
        List<MavenProject> list = findMavenProjects(path);
        return list;
    }

    List<MavenProject> findMavenProjects(Path directory) throws IOException {
        List<MavenProject> list = new LinkedList<>();
        if (Files.isDirectory(directory)) {
            MavenProjectStatus response = isMavenProject(directory);
            if (response == MavenProjectStatus.LEGAL_MAVEN_PROJECT) {
                if (containsJavaSourceFiles(getPathToSrcDirectory())) {
                    MavenProject mavenProject = this.pomFileReader.readPomFile(this.pathToPomXmlFile);
                    list.add(mavenProject);
                }
            }
            else {
                //It is not a Maven project. Keep searching...
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                    for (Path file : stream) {
                        if (Files.isDirectory(file)) {
                            list.addAll(findMavenProjects(file));
                        }
                    }
                } catch (IOException | DirectoryIteratorException x) {
                    // IOException can never be thrown by the iteration.
                    // In this snippet, it can only be thrown by newDirectoryStream.
                    System.err.println(x);
                }
            }
        }
        return list;
    }

    Path getPathToSrcDirectory() {
        return this.pathToSrcDirectory;
    }

    MavenProjectStatus isMavenProject(Path directory) throws IOException {
        MavenProjectStatus mavenProjStatus = null;
        Files.list(directory).forEach(this::checkPathForMavenEntity);
        if (pathToPomXmlFile != null && pathToSrcDirectory != null) {
            mavenProjStatus = MavenProjectStatus.LEGAL_MAVEN_PROJECT;
        }
        else {
            mavenProjStatus = MavenProjectStatus.NOT_A_MAVEN_PROJECT;
        }
        return mavenProjStatus;
    }

    void checkPathForMavenEntity(Path file) {
        if ("pom.xml".equals(file.getFileName().toString())) {
            pathToPomXmlFile = file.toAbsolutePath();
        }
        if ("src".equals(file.getFileName().toString())) {
            if (Files.isDirectory(file)) {
                pathToSrcDirectory = file;
            }
        }
    }

    enum MavenProjectStatus {LEGAL_MAVEN_PROJECT, NOT_A_MAVEN_PROJECT; }


    boolean containsJavaSourceFiles(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (Files.isDirectory(file)) {
                    if (containsJavaSourceFiles(file)) {
                        return true;
                    }
                }
                else if (file.getFileName().toString().endsWith(".java")) {
                    return true;
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        return false;
    }




}
