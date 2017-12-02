package org.teinelund.application.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.teinelund.application.controller.domain.MavenPomFile;
import org.teinelund.application.controller.domain.MavenPomFileImpl;
import org.teinelund.application.extension.TemporaryFolder;
import org.teinelund.application.extension.TemporaryFolderExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TemporaryFolderExtension.class)
class FindMavenPomFileImplTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder();
    private FindMavenProjectImpl findMavenProject = new FindMavenProjectImpl(new LinkedList<>(), null);

    @Test
    public void isMavenProjectWhereFolderIsLegalMavenProject() throws IOException {
        // Initialize
        Path theProject = createLegalMavenProject("theProject", AddSourceFile.NO_ADDITIONAL_FILES);
        // Test
        FindMavenProjectImpl.IsMavenProjectResponse result = findMavenProject.isMavenProject(theProject);
        // Verify
        assertEquals(FindMavenProjectImpl.MavenProjectStatus.LEGAL_MAVEN_PROJECT, result.getMavenProjectStatus());  //JUnit
    }

    enum AddSourceFile {NO_ADDITIONAL_FILES, PROPERTIES_FILE, JSON_FILE;}

    Path createLegalMavenProject(String projectName, AddSourceFile addSourceFile) throws IOException {
        File theProject = temporaryFolder.newFolder(projectName);
        temporaryFolder.newFolder(projectName, "src", "main");
        temporaryFolder.newFolder(projectName, "target");
        temporaryFolder.newFile(projectName + File.separator + "src" + File.separator +  "main" + File.separator + "Application.java");
        temporaryFolder.newFile(projectName + File.separator + "pom.xml");
        if (addSourceFile != AddSourceFile.PROPERTIES_FILE) {
            temporaryFolder.newFolder(projectName, "src", "resource");
            temporaryFolder.newFile(projectName + File.separator + "src" + File.separator +  "resource" + File.separator + "environment.properties");
        }
        else if (addSourceFile != AddSourceFile.JSON_FILE) {
            temporaryFolder.newFolder("theProject", "src", "application");
            temporaryFolder.newFile("theProject" + File.separator + "src" + File.separator +  "application" + File.separator + "iphone.json");
        }
        return theProject.toPath();
    }

    @Test
    public void isMavenProjectWhereSrcIsMissing() throws IOException {
        // Initialize
        Path theProject = createIllegalMavenProject("theProject", MissingMavenEntity.MISSING_SRC_FOLDER);
        // Test
        FindMavenProjectImpl.IsMavenProjectResponse result = findMavenProject.isMavenProject(theProject);
        // Verify
        assertEquals(FindMavenProjectImpl.MavenProjectStatus.NOT_A_MAVEN_PROJECT, result.getMavenProjectStatus());  //JUnit
    }

    enum MissingMavenEntity {MISSING_SRC_FOLDER, MISSING_POM_FILE, MISSING_JAVA_FILE;}

    Path createIllegalMavenProject(String projectName, MissingMavenEntity missingMavenEntity) throws IOException {
        File theProject = temporaryFolder.newFolder(projectName);
        if (missingMavenEntity != MissingMavenEntity.MISSING_SRC_FOLDER)
            temporaryFolder.newFolder(projectName, "src", "main");
        temporaryFolder.newFolder(projectName, "target");
        if (missingMavenEntity != MissingMavenEntity.MISSING_POM_FILE)
            temporaryFolder.newFile(projectName + File.separator + "pom.xml");
        if (missingMavenEntity != MissingMavenEntity.MISSING_JAVA_FILE && missingMavenEntity != MissingMavenEntity.MISSING_SRC_FOLDER)
            temporaryFolder.newFile(projectName + File.separator + "src" + File.separator +  "main" + File.separator + "Application.java");
        return theProject.toPath();
    }

    @Test
    public void isMavenProjectWherePomXmlFileIsMissing() throws IOException {
        // Initialize
        Path theProject = createIllegalMavenProject("theProject", MissingMavenEntity.MISSING_POM_FILE);
        // Test
        FindMavenProjectImpl.IsMavenProjectResponse result = findMavenProject.isMavenProject(theProject);
        // Verify
        assertEquals(FindMavenProjectImpl.MavenProjectStatus.NOT_A_MAVEN_PROJECT, result.getMavenProjectStatus());  //JUnit
    }

    @Test
    public void containsJavaSourceFilesWhereJavaFileExist() throws IOException {
        // Initialize
        Path theProject = createLegalMavenProject("theProject", AddSourceFile.NO_ADDITIONAL_FILES);
        // Test
        boolean result = findMavenProject.containsJavaSourceFiles(theProject);
        // Verify
        assertTrue(result);  //JUnit
    }

    @Test
    public void containsJavaSourceFilesWhereJavaFilesDoesNotExist() throws IOException {
        // Initialize
        Path theProject = createIllegalMavenProject("theProject", MissingMavenEntity.MISSING_JAVA_FILE);
        // Test
        boolean result = findMavenProject.containsJavaSourceFiles(theProject);
        // Verify
        assertFalse(result);  //JUnit
    }

    @Test
    public void containsJavaSourceFilesWhereJavaFilesExist1() throws IOException {
        // Initialize
        Path theProject = createLegalMavenProject("theProject", AddSourceFile.PROPERTIES_FILE);
        // Test
        boolean result = findMavenProject.containsJavaSourceFiles(theProject);
        // Verify
        assertTrue(result);  //JUnit
    }


    @Test
    public void containsJavaSourceFilesWhereJavaFilesExist2() throws IOException {
        // Initialize
        Path theProject = createLegalMavenProject("theProject", AddSourceFile.JSON_FILE);
        // Test
        boolean result = findMavenProject.containsJavaSourceFiles(theProject);
        // Verify
        assertTrue(result);  //JUnit
    }


    @Test
    public void constructorWherePathNameListIsEmpty() throws IOException {
        // Initialize
        List<String> pathNameList = new LinkedList<>();
        // Test
        FindMavenProjectImplStub stub = new FindMavenProjectImplStub(pathNameList);
        // Verify
        assertTrue(stub.getMavenPomFiles().isEmpty());  //JUnit
    }


    @Test
    public void constructorWherePathNameListContainsOneLegalPath() throws IOException {
        // Initialize
        List<String> pathNameList = new LinkedList<>();
        pathNameList.add(createMavenProjectFolder("theProject", false).toString());
        final int EXPECTED_SIZE = 1;
        // Test
        FindMavenProjectImplStub stub = new FindMavenProjectImplStub(pathNameList);
        // Verify
        assertEquals(EXPECTED_SIZE, stub.getMavenPomFiles().size());  //JUnit
    }

    @Test
    public void constructorWherePathNameListContainsOneNonLegalPath() throws IOException {
        // Initialize
        List<String> pathNameList = new LinkedList<>();
        pathNameList.add(createMavenProjectFolder("invoice", false).toString());
        // Test
        FindMavenProjectImplStub stub = new FindMavenProjectImplStub(pathNameList);
        // Verify
        assertTrue(stub.getMavenPomFiles().isEmpty());
    }

    @Test
    public void constructorWherePathNameListContainsTwoLegalPath() throws IOException {
        // Initialize
        List<String> pathNameList = new LinkedList<>();
        pathNameList.add(createMavenProjectFolder("invoiceResource", false).toString());
        pathNameList.add(createMavenProjectFolder("invoiceDAO", false).toString());
        final int EXPECTED_SIZE = 2;
        // Test
        FindMavenProjectImplStub stub = new FindMavenProjectImplStub(pathNameList);
        // Verify
        assertEquals(EXPECTED_SIZE, stub.getMavenPomFiles().size());  //JUnit
    }


    @Test
    public void constructorWherePathNameListContainsMavenModules() throws IOException {
        // Initialize
        List<String> pathNameList = new LinkedList<>();
        pathNameList.add(createMavenProjectFolder("invoice", true).toString());
        final int EXPECTED_SIZE = 3;
        // Test
        FindMavenProjectImplStub stub = new FindMavenProjectImplStub(pathNameList);
        // Verify
        assertEquals(EXPECTED_SIZE, stub.getMavenPomFiles().size());  //JUnit
    }

    Path createMavenProjectFolder(String projectName, boolean isModule) throws IOException {
        File theProject = temporaryFolder.newFolder(projectName);
        if (isModule) {
            temporaryFolder.newFolder(projectName, "invoiceResource");
            temporaryFolder.newFolder(projectName, "invoiceDAO");
            temporaryFolder.newFolder(projectName, "invoiceBusinessDomain");
        }
        return theProject.toPath();
    }




}

class FindMavenProjectImplStub extends FindMavenProjectImpl {

    Path pathToSrcDirectory;
    boolean containsJavaSourceFile = true;
    Path pathToPomXmlFile;

    public FindMavenProjectImplStub(List<String> pathNameList) {
        super(pathNameList, new PomFileReaderStub());
    }

    @Override
    IsMavenProjectResponse isMavenProject(Path directory) throws IOException {
        this.pathToPomXmlFile = directory;
        containsJavaSourceFile = true;
        if (directory.toString().endsWith("invoice")) {
            return IsMavenProjectResponse.notAMavenProject();
        }
        if (directory.toString().endsWith("invoiceInterface") || directory.toString().endsWith("invoice")) {
            this.containsJavaSourceFile = false;
        }
        return IsMavenProjectResponse.legalMavenProject(this.pathToPomXmlFile, this.pathToSrcDirectory);
    }

    @Override
    boolean containsJavaSourceFiles(Path dir) {
        return this.containsJavaSourceFile;
    }

    @Override
    Path getPathToSrcDirectory() {
        return this.pathToSrcDirectory;
    }

    @Override
    Path getPathToPomXmlFile() {
        return this.pathToPomXmlFile;
    }
}

class PomFileReaderStub implements PomFileReader {

    @Override
    public MavenPomFile readPomFile(Path mavenPomFilePath) throws IOException {
        return new MavenPomFileImpl(mavenPomFilePath, null, null, null);
    }
}