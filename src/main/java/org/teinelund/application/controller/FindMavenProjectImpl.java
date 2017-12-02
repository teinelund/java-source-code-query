package org.teinelund.application.controller;

import org.teinelund.application.controller.domain.MavenPomFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    private List<MavenPomFile> mavenPomFiles;

    public FindMavenProjectImpl(List<String> pathNameList, PomFileReader pomFileReader) {
        this.pathNameList = pathNameList;
        this.pomFileReader = pomFileReader;
        this.mavenPomFiles = new LinkedList<>();
        for (String pathName : pathNameList) {
            try {
                Set<MavenPomFile> mavenPomFiles = findMavenProjects(pathName);
                if (mavenPomFiles != null && !mavenPomFiles.isEmpty()) {
                    this.mavenPomFiles.addAll(mavenPomFiles);
                }
            }
            catch (IOException e) {
                System.err.println("IOException for path name " + pathName + ". " + e.getMessage());
                System.err.println(e);
            }
        }
    }

    @Override
    public List<MavenPomFile> getMavenPomFiles() {
        return this.mavenPomFiles;
    }

    Set<MavenPomFile> findMavenProjects(String pathName) throws IOException {
        Path path = Paths.get(pathName);
        Set<MavenPomFile> set = findMavenProjects(path);
        return set;
    }

    Set<MavenPomFile> findMavenProjects(Path directory) throws IOException {
        Set<MavenPomFile> set = new HashSet<>();
        if (Files.isDirectory(directory)) {
            IsMavenProjectResponse response = isMavenProject(directory);
            if (response.getMavenProjectStatus() == MavenProjectStatus.LEGAL_MAVEN_PROJECT) {
                if (containsJavaSourceFiles(response.getPathToSrcDirectory())) {
                    MavenPomFile mavenPomFile = this.pomFileReader.readPomFile(response.getPathToPomXmlFile());
                    set.add(mavenPomFile);
                    int y = 0;
                }
            }
            else {
                //It is not a Maven project. Keep searching...
                File[] files = directory.toFile().listFiles();
                for (File file : files) {
                    if (Files.isDirectory(file.toPath())) {
                        Set<MavenPomFile> otherSet = findMavenProjects(file.toPath());
                        set.addAll(otherSet);
                        int x = 0;
                    }
                }
            }
        }
        return set;
    }

    Path getPathToSrcDirectory() {
        return this.pathToSrcDirectory;
    }

    Path getPathToPomXmlFile() {
        return this.pathToPomXmlFile;
    }

    IsMavenProjectResponse isMavenProject(Path directory) throws IOException {
        IsMavenProjectResponse response = null;
        File[] files = directory.toFile().listFiles();
        Path pathToPomXmlFile = null;
        Path pathToSrcDirectory = null;
        for (File file : files) {
            if ("pom.xml".equals(file.getName())) {
                pathToPomXmlFile = file.toPath();
            }
            if ("src".equals(file.getName())) {
                if (Files.isDirectory(file.toPath())) {
                    pathToSrcDirectory = file.toPath();
                }
            }
        }
        if (pathToPomXmlFile != null && pathToSrcDirectory != null) {
            response = IsMavenProjectResponse.legalMavenProject(pathToPomXmlFile, pathToSrcDirectory);
        }
        else {
            response = IsMavenProjectResponse.notAMavenProject();
        }
        return response;
    }

    enum MavenProjectStatus {LEGAL_MAVEN_PROJECT, NOT_A_MAVEN_PROJECT; }

    static class IsMavenProjectResponse {
        private Path pathToPomXmlFile;
        private Path pathToSrcDirectory;
        private MavenProjectStatus mavenProjectStatus;

        IsMavenProjectResponse() {
            mavenProjectStatus = MavenProjectStatus.NOT_A_MAVEN_PROJECT;
        }

        IsMavenProjectResponse(Path pathToPomXmlFile, Path pathToSrcDirectory) {
            mavenProjectStatus = MavenProjectStatus.LEGAL_MAVEN_PROJECT;
            this.pathToPomXmlFile = pathToPomXmlFile;
            this.pathToSrcDirectory = pathToSrcDirectory;
        }

        public static IsMavenProjectResponse notAMavenProject() {
            return new IsMavenProjectResponse();
        }

        public static IsMavenProjectResponse legalMavenProject(Path pathToPomXmlFile, Path pathToSrcDirectory) {
            return new IsMavenProjectResponse(pathToPomXmlFile, pathToSrcDirectory);
        }

        public MavenProjectStatus getMavenProjectStatus() {
            return this.mavenProjectStatus;
        }

        public Path getPathToPomXmlFile() {
            return this.pathToPomXmlFile;
        }

        public Path getPathToSrcDirectory() {
            return this.pathToSrcDirectory;
        }

    }


    boolean containsJavaSourceFiles(Path dir) {
        File[] files = dir.toFile().listFiles();
        for (File file : files) {
            if (Files.isDirectory(file.toPath())) {
                if (containsJavaSourceFiles(file.toPath())) {
                    return true;
                }
            }
            else if (file.getName().endsWith(".java")) {
                return true;
            }
        }
        return false;
    }




}
