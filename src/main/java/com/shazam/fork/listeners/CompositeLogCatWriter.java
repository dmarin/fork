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
package com.shazam.fork.listeners;

import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmlib.testrunner.TestIdentifier;

import java.util.List;

class CompositeLogCatWriter implements LogCatWriter {

	private final LogCatWriter[] logCatWriters;

	CompositeLogCatWriter(LogCatWriter... logCatWriters) {
		this.logCatWriters = logCatWriters;
	}

	@Override
	public void writeLogs(TestIdentifier test, List<LogCatMessage> logCatMessages) {
		for (LogCatWriter logCatWriter : logCatWriters) {
			logCatWriter.writeLogs(test, logCatMessages);
		}
	}

}