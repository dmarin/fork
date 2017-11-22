package com.shazam.fork.injector.system;

import com.shazam.fork.Configuration;
import com.shazam.fork.system.io.FileManager;

import static com.shazam.fork.injector.ConfigurationInjector.configuration;

public class FileManagerInjector {

    private FileManagerInjector() {}

    public static FileManager fileManager() {
        Configuration configuration = configuration();
        return new FileManager(configuration.getOutput(), configuration.getForkReportOutput());
    }
}
