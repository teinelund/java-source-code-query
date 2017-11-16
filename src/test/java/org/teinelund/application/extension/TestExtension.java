package org.teinelund.application.extension;


import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestExtension implements BeforeAllCallback, BeforeEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterEachCallback, AfterAllCallback {

    /**
     * Idea for TemporaryFolderExtension for JUnit 5.
     *
     * Put it in a separate package (or later in a sparate Maven project.
     *
     * TemporaryFolderExtension is public class implementing BeforeEachCallback or BeforeTestExecutionCallback
     * (and the counterpart AfterEachCallback or AfterTestExecutionCallback).
     *
     * TemporaryFolder is a public class with api like the JUnit 4 Rule
     * - newFile
     * - newFolder
     * .. and so on.
     *
     * Both these classes uses a private package class, say TempFolderSingleton, that only have
     * static api. And within these methods files and folders are created and destroyed.
     *
     *
     * @param extensionContext
     * @throws Exception
     */

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.out.println("beforeEach");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        System.out.println("afterAll");
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        System.out.println("afterEach");
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        System.out.println("afterTestExecution");
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        System.out.println("beforeAll");
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        System.out.println("beforeTestExecution");
    }
}
