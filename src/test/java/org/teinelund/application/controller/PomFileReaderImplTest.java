package org.teinelund.application.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PomFileReaderImplTest {

    private final String GROUP_ID = "org.teinelund.application";
    private final String ARTIFACT_ID = "java-source-code-query";
    private final String VERSION = "1.0-SNAPSHOT";
    private static PomFileReader reader;
    private static Path pomFilePath;

    @BeforeAll
    public static void initialize() {
        reader = new PomFileReaderImpl();
        pomFilePath = Paths.get("pom.xml");
    }

    @Test
    public void fetchPomFileInformtionWherePomFileContainsGroupId() throws IOException {
        // Initialize
        List<String> list = new LinkedList<>();
        list.add("    <groupId>" + GROUP_ID + "</groupId>");
        // Test
        MavenProject result = ((PomFileReaderImpl) reader).fetchPomFileInformtion(pomFilePath, list);
        // Verify
        assertEquals(GROUP_ID, result.getGroupId());
    }

    @Test
    public void fetchPomFileInformtionWherePomFileContainsGroupIdAndNewLine() throws IOException {
        // Initialize
        List<String> list = new LinkedList<>();
        list.add("    <groupId>" + GROUP_ID + "</groupId>" + System.lineSeparator());
        // Test
        MavenProject result = ((PomFileReaderImpl) reader).fetchPomFileInformtion(pomFilePath, list);
        // Verify
        assertEquals(GROUP_ID, result.getGroupId());
    }

    @Test
    public void fetchPomFileInformtionWherePomFileWithoutParent() throws IOException {
        // Initialize
        // Test
        MavenProject result = ((PomFileReaderImpl) reader).fetchPomFileInformtion(pomFilePath, createLegalPomFileWithoutParent());
        // Verify
        assertEquals(GROUP_ID, result.getGroupId());
        assertEquals(ARTIFACT_ID, result.getArtifactId());
        assertEquals(VERSION, result.getVersion());
    }

    private List<String> createLegalPomFileWithoutParent() {
        List<String> list = new LinkedList<>();
        list.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        list.add("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        list.add("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">");
        list.add("    <modelVersion>4.0.0</modelVersion>");
        list.add("    <groupId>" + GROUP_ID + "</groupId>");
        list.add("    <artifactId>" + ARTIFACT_ID + "</artifactId>");
        list.add("    <packaging>jar</packaging>");
        list.add("    <version>" + VERSION + "</version>");
        list.add("    <name>java-source-code-query</name>");
        list.add("    <url>http://maven.apache.org</url>");
        list.add("    <dependencies>");
        list.add("        <dependency>");
        list.add("            <groupId>commons-cli</groupId>");
        list.add("            <artifactId>commons-cli</artifactId>");
        list.add("            <version>1.4</version>");
        list.add("            <scope>compile</scope>");
        list.add("        </dependency>");
        list.add("    </dependencies>");
        list.add("</project>");
        return list;
    }

    @Test
    public void fetchPomFileInformtionWherePomFileWithParent() throws IOException {
        // Initialize
        // Test
        MavenProject result = ((PomFileReaderImpl) reader).fetchPomFileInformtion(pomFilePath, createLegalPomFileWithParent());
        // Verify
        assertEquals(GROUP_ID, result.getGroupId());
        assertEquals(ARTIFACT_ID, result.getArtifactId());
        assertEquals(VERSION, result.getVersion());
    }

    private List<String> createLegalPomFileWithParent() {
        List<String> list = new LinkedList<>();
        list.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator());
        list.add("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + System.lineSeparator());
        list.add("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">" + System.lineSeparator());
        list.add("    <modelVersion>4.0.0</modelVersion>" + System.lineSeparator());
        list.add("    <parent>" + System.lineSeparator());
        list.add("        <groupId>com.example</groupId>" + System.lineSeparator());
        list.add("        <artifactId>parent</artifactId>" + System.lineSeparator());
        list.add("        <version>0.0.1-SNAPSHOT</version>" + System.lineSeparator());
        list.add("        <relativePath>../Parent/pom.xml</relativePath>" + System.lineSeparator());
        list.add("    </parent>" + System.lineSeparator());
        list.add("    <groupId>" + GROUP_ID + "</groupId>" + System.lineSeparator());
        list.add("    <artifactId>" + ARTIFACT_ID + "</artifactId>" + System.lineSeparator());
        list.add("    <packaging>jar</packaging>" + System.lineSeparator());
        list.add("    <version>" + VERSION + "</version>" + System.lineSeparator());
        list.add("    <name>java-source-code-query</name>" + System.lineSeparator());
        list.add("    <url>http://maven.apache.org</url>" + System.lineSeparator());
        list.add("    <dependencies>" + System.lineSeparator());
        list.add("        <dependency>" + System.lineSeparator());
        list.add("            <groupId>commons-cli</groupId>" + System.lineSeparator());
        list.add("            <artifactId>commons-cli</artifactId>" + System.lineSeparator());
        list.add("            <version>1.4</version>" + System.lineSeparator());
        list.add("            <scope>compile</scope>" + System.lineSeparator());
        list.add("        </dependency>" + System.lineSeparator());
        list.add("    </dependencies>" + System.lineSeparator());
        list.add("</project>" + System.lineSeparator());
        return list;
    }
}