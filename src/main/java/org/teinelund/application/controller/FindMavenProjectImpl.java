package org.teinelund.application.controller;

import org.teinelund.application.controller.domain.MavenPomFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The aim for this class is to find Maven projects that contains
 * - a 'pom.xml' file.
 * - a folder with the name 'src'.
 * - the folder 'src' contains java source file/files.
 *
 * @author Henrik Teinelund
 */
class FindMavenProjectImpl implements FindMavenProject {

    private Set<String> projectPathNames;
    private PomFileReader pomFileReader;
    private Path pathToPomXmlFile;
    private Path pathToSrcDirectory;
    private Set<MavenPomFile> mavenPomFiles;

    public FindMavenProjectImpl(Set<String> projectPathNames, PomFileReader pomFileReader) {
        this.projectPathNames = projectPathNames;
        this.pomFileReader = pomFileReader;
        this.mavenPomFiles = new HashSet<>();
        for (String projectPathName : projectPathNames) {
            try {
                Set<MavenPomFile> mavenPomFiles = findMavenProjects(projectPathName);
                if (mavenPomFiles != null && !mavenPomFiles.isEmpty()) {
                    this.mavenPomFiles.addAll(mavenPomFiles);
                }
            }
            catch (IOException e) {
                System.err.println("IOException for path name " + projectPathName + ". " + e.getMessage());
                System.err.println(e);
            }
        }
    }

    /**
     * Returns the set of found legal Maven Projects. That is, all Maven Projects containing
     * - a 'pom.xml' file.
     * - a folder with the name 'src'.
     * - the folder 'src' contains java source file/files.
     * @return Set<MavenPomFile> is the set of legal Maven Projects.
     */
    @Override
    public Set<MavenPomFile> getMavenPomFiles() {
        return this.mavenPomFiles;
    }

    Set<MavenPomFile> findMavenProjects(String projectPathName) throws IOException {
        Path path = Paths.get(projectPathName);
        Set<MavenPomFile> set = findMavenProjects(path);
        return set;
    }

    /**
     * The set of Maven Pom Files will be Maven Project that contains
     * - a 'pom.xml' file.
     * - folder with the name 'src'.
     * - the folder 'src' contains at least one '*.java' file.
     *
     * @param directory
     * @return
     * @throws IOException
     */
    Set<MavenPomFile> findMavenProjects(Path directory) throws IOException {
        Set<MavenPomFile> set = new HashSet<>();
        if (Files.isDirectory(directory)) {
            IsMavenProjectResponse response = isMavenProject(directory);
            if (response.getMavenProjectStatus() == MavenProjectStatus.LEGAL_MAVEN_PROJECT) {
                if (containsJavaSourceFiles(response.getPathToSrcDirectory())) {
                    MavenPomFile mavenPomFile = this.pomFileReader.readPomFile(response.getPathToPomXmlFile());
                    set.add(mavenPomFile);
                }
            }
            else {
                //It is not a Maven project. Keep searching...
                File[] files = directory.toFile().listFiles();
                for (File file : files) {
                    if (Files.isDirectory(file.toPath())) {
                        Set<MavenPomFile> otherSet = findMavenProjects(file.toPath());
                        set.addAll(otherSet);
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

    /**
     * A legal Maven Project, in this application, is a project which contains
     * - a file named pom.xml
     * - a folder with the name src
     *
     * @param directory to be investigated if it is a legal Maven Project.
     * @return IsMavenProjectResponse
     * @throws IOException
     */
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

    /**
     * Container class that holds Maven Project Status (legal maven project or not) and
     * path to pom xml file and path to src folder.
     */
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
