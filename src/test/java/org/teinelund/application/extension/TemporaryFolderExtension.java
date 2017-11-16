package org.teinelund.application.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TemporaryFolderExtension implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        TempFolderSingleton.beforeEach();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        TempFolderSingleton.afterEach();
    }
}
