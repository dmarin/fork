package com.shazam.fork.runner.listeners;

import com.android.ddmlib.testrunner.TestIdentifier;
import com.shazam.fork.model.Device;
import com.shazam.fork.model.Pool;
import com.shazam.fork.model.TestCaseEvent;
import com.shazam.fork.system.io.FileManager;
import com.shazam.fork.system.io.FileType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * Pulls dumpsys file from the devices
 */
public class DumpsysTestRunListener extends NoOpITestRunListener {

    private final Device device;
    private final FileManager fileManager;
    private final Pool pool;
    private final Logger logger = LoggerFactory.getLogger(DumpsysTestRunListener.class);
    private final TestCaseEvent testCase;

    public DumpsysTestRunListener(Device device, FileManager fileManager, Pool pool, TestCaseEvent testCase) {
        this.device = device;
        this.fileManager = fileManager;
        this.pool = pool;
        this.testCase = testCase;
    }

    @Override
    public void testRunEnded(long elapsedTime, Map<String, String> runMetrics) {
        TestIdentifier testIdentifier = new TestIdentifier(testCase.getTestClass(), testCase.getTestMethod());
        String remoteBatteryFile = "";//hardcoded for the time been;
        String remoteGraphicsFile = ""; //hardcoded for the time been;
        //But it can be taken from the test case
//        String batteryFile = RemoteFileManager.remoteBatteryDumpsysFileFor(testIdentifier);
//        String graphicsFile = RemoteFileManager.remoteGraphicsDumpsysFileFor(testIdentifier);
        final File batteryFile = fileManager.createFile(FileType.DUMPSYS, pool, device, testIdentifier);
        try {
            String parentDirectory = batteryFile.getParent();
            device.getDeviceInterface().pullFile(remoteBatteryFile, parentDirectory + File.pathSeparator + "battery.txt");
            device.getDeviceInterface().pullFile(remoteGraphicsFile, parentDirectory + File.pathSeparator + "graphics.txt");
        } catch (Exception e) {
            logger.error("Something went wrong while pulling coverage file", e);
        }
    }
}
