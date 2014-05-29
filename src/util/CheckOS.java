package util;

import java.io.File;


public class CheckOS {

    private File saveDir;
    private String slash;

    /**
     * Checks what the OS to decide where to read/write the save files.
     */
    public void check() {
        String os = System.getProperty("os.name");
        if (os.equals("Windows")) {
            saveDir = new File(System.getProperty("user.home"), "Application Data\\WatchLists");
            slash = "\\";
        } else {
            saveDir = new File(System.getProperty("user.home") + "/WatchLists");
            slash = "/";
        }

        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
    }

    public File getSaveDir() {
        return saveDir;
    }

    public String getSlash() {
        return slash;
    }

}
