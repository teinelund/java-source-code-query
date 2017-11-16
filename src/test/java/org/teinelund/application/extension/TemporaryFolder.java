package org.teinelund.application.extension;

import java.io.File;
import java.io.IOException;

public class TemporaryFolder {


    /**
     * Returns a new fresh file with the given name under the temporary folder.
     */
    public File newFile(String fileName) throws IOException {
        return TempFolderSingleton.newFile(fileName);
    }

    /**
     * Returns a new fresh file with a random name under the temporary folder.
     */
    public File newFile() throws IOException {
        return TempFolderSingleton.newFile();
    }

    /**
     * Returns a new fresh folder with the given path under the temporary
     * folder.
     */
    public File newFolder(String path) throws IOException {
        return TempFolderSingleton.newFolder(path);
    }

    /**
     * Returns a new fresh folder with the given paths under the temporary
     * folder. For example, if you pass in the strings {@code "parent"} and {@code "child"}
     * then a directory named {@code "parent"} will be created under the temporary folder
     * and a directory named {@code "child"} will be created under the newly-created
     * {@code "parent"} directory.
     */
    public File newFolder(String... paths) throws IOException {
        return TempFolderSingleton.newFolder(paths);
    }

    /**
     * Returns a new fresh folder with a random name under the temporary folder.
     */
    public File newFolder() throws IOException {
        return TempFolderSingleton.newFolder();
    }

    /**
     * @return the location of this temporary folder.
     */
    public File getRoot() {
        return TempFolderSingleton.getRoot();
    }

    /**
     * Delete all files and folders under the temporary folder. Usually not
     * called directly, since it is automatically applied by the method afterEach and TemporaryFolderExtension.
     *
     * @throws AssertionError if unable to clean up resources
     * and deletion of resources is assured.
     */
    public void delete() {
        TempFolderSingleton.delete();
    }


}
