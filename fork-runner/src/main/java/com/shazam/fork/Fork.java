/*
 * Copyright 2014 Shazam Entertainment Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.shazam.fork;

import com.shazam.fork.system.adb.Adb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.shazam.fork.injector.ConfigurationInjector.setConfiguration;
import static com.shazam.fork.injector.ForkRunnerInjector.forkRunner;
import static com.shazam.fork.injector.system.AdbInjector.adb;
import static com.shazam.fork.utils.Utils.millisSinceNanoTime;
import static java.lang.System.nanoTime;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatPeriod;

public final class Fork {
    private static final Logger logger = LoggerFactory.getLogger(Fork.class);

    private final ForkRunner forkRunner;
    private final File output;
    private final Configuration configuration;
    private final Adb adb;

    public Fork(Configuration configuration) {
        this.output = configuration.getOutput();
        this.configuration = configuration;
        setConfiguration(configuration);
        this.forkRunner = forkRunner();
        this.adb = adb();
    }

    public boolean run() {
		long startOfTestsMs = nanoTime();
		try {
		    if (configuration.isTerminatingAdb()) { //add a new parameter for this one
                deleteDirectory(output);
            }
            //noinspection ResultOfMethodCallIgnored
            output.mkdirs();
		    configuration.getForkReportOutput().mkdirs();
            return forkRunner.run();
		} catch (Exception e) {
            logger.error("Error while running Fork", e);
			return false;
		} finally {
            long duration = millisSinceNanoTime(startOfTestsMs);
            logger.info(formatPeriod(0, duration, "'Total time taken:' H 'hours' m 'minutes' s 'seconds'"));
            if (configuration.isTerminatingAdb()) {
                adb.terminate();
            }
		}
	}
}
