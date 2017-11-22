/*
 * Copyright 2015 Shazam Entertainment Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.shazam.fork.reporter.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;

import static com.shazam.fork.reporter.ForkReporter.Builder.forkReporter;
import static org.apache.commons.io.FileUtils.deleteDirectory;

public class ForkJenkinsReportTask extends DefaultTask {
    private static final String FORK_OUTPUT_DIR = "forkReport";
    private static final String DOWNLOAD_DIR = "json";
    private static final String REPORT_DIR = "html";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @TaskAction
    public void runForkJenkins() {
        ForkJenkinsReportExtension extension = getProject().getExtensions().getByType(ForkJenkinsReportExtension.class);
        File forkOutputDir = new File(extension.output, FORK_OUTPUT_DIR);
        createDir(forkOutputDir, !extension.multimodule);
        File downloadDir = new File(forkOutputDir, DOWNLOAD_DIR);
        createDir(downloadDir);
        File htmlDir = new File(forkOutputDir, REPORT_DIR);
        createDir(htmlDir);

        JenkinsDownloader jenkinsDownloader = new JenkinsDownloader(downloadDir, extension);
        String reportUrlTemplate = jenkinsDownloader.downloadJenkinsFiles();

        forkReporter()
                .withTitle(extension.reportTitle)
                .withInput(downloadDir)
                .withOutput(htmlDir)
                .withBaseUrl(reportUrlTemplate)
                .build()
                .createReport();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDir(File dir) {
        createDir(dir, true);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDir(File dir, boolean shouldDeleteFirst) {
        try {
            if (shouldDeleteFirst) {
                deleteDirectory(dir);
            }
            dir.mkdirs();
        } catch (IOException e) {
            throw new GradleException("Could not create directory: " + dir.toString(), e);
        }
    }
}
