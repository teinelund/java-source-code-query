package org.teinelund.application.controller;

import org.teinelund.application.ApplicationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class PomFileReaderImpl implements PomFileReader {

    private static Pattern parentStartPattern;
    private static Pattern parentEndPattern;
    private static Pattern artifactIdPattern;
    private static Pattern groupIdPattern;
    private static Pattern versionPattern;
    private static Pattern dependenciesPattern;
    private static Pattern buildPattern;

    {
        parentStartPattern = Pattern.compile("^\\s*<parent>.*\\s*");
        parentEndPattern = Pattern.compile("^\\s*</parent>.*\\s*");
        artifactIdPattern = Pattern.compile("^\\s*<artifactId>(.*)</artifactId>.*\\s*");
        groupIdPattern = Pattern.compile("^\\s*<groupId>(.*)</groupId>.*\\s*");
        versionPattern = Pattern.compile("^\\s*<version>(.*)</version>.*\\s*");
        dependenciesPattern = Pattern.compile("^\\s*<dependencies>.*\\s*");
        buildPattern = Pattern.compile("^\\s*<build>.*\\s*");
    }

    @Override
    public MavenProject readPomFile(Path mavenPomFilePath) throws IOException {
        if ( ! "pom.xml".equals(mavenPomFilePath.getFileName().toString())) {
            throw new ApplicationException("file \"" + mavenPomFilePath + "\" is not a pom.xml file.");
        }
        BufferedReader br = Files.newBufferedReader(mavenPomFilePath);
        List<String> list = br.lines().collect(Collectors.toList());
        return fetchPomFileInformtion(mavenPomFilePath, list);
    }

    MavenProject fetchPomFileInformtion(Path mavenPomFilePath, List<String> pomFileLines) throws IOException {
        boolean isInsideParentTag = false;
        String artifactId = "";
        String groupId = "";
        String version = "";
        for (String line : pomFileLines) {
            Matcher matcher;
            if (isInsideParentTag) {
                matcher = parentEndPattern.matcher(line);
                if (matcher.matches()) {
                    isInsideParentTag = false;
                }
                continue;
            }
            matcher = parentStartPattern.matcher(line);
            if (matcher.matches()) {
                isInsideParentTag = true;
                continue;
            }
            matcher = groupIdPattern.matcher(line);
            if (matcher.matches() && matcher.groupCount() > 0) {
                groupId = matcher.group(1);
                continue;
            }
            matcher = artifactIdPattern.matcher(line);
            if (matcher.matches() && matcher.groupCount() > 0) {
                artifactId = matcher.group(1);
                continue;
            }
            matcher = versionPattern.matcher(line);
            if (matcher.matches() && matcher.groupCount() > 0) {
                version = matcher.group(1);
            }
            matcher = dependenciesPattern.matcher(line);
            if (matcher.matches()) {
                break;
            }
            matcher = buildPattern.matcher(line);
            if (matcher.matches()) {
                break;
            }
        }
        return new MavenProjectImpl(mavenPomFilePath, groupId, artifactId, version);
    }
}
