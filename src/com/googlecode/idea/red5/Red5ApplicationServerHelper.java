/*
 * Copyright (c) 2009 Carl Sziebert
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * $Id: Red5ApplicationServerHelper.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import static com.googlecode.idea.red5.Red5Bundle.message;
import com.intellij.javaee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.javaee.appServerIntegrations.ApplicationServerInfo;
import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentData;
import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentDataEditor;
import com.intellij.javaee.appServerIntegrations.CantFindApplicationServerJarsException;
import static org.apache.commons.lang.StringUtils.EMPTY;

import java.io.File;
import static java.io.File.separatorChar;
import java.util.ArrayList;

public class Red5ApplicationServerHelper implements ApplicationServerHelper {

    public ApplicationServerInfo getApplicationServerInfo(ApplicationServerPersistentData persistentData) throws CantFindApplicationServerJarsException {
        Red5PersistentData data = (Red5PersistentData) persistentData;
        File homeDir = new File(data.RED5_HOME.replace('/', separatorChar)).getAbsoluteFile();
        File libDir = new File(homeDir, "lib");
        if (!libDir.isDirectory()) {
            throw new CantFindApplicationServerJarsException(message("message.text.cant.find.directory", libDir.getAbsolutePath()));
        }
        ArrayList<File> files = new ArrayList<File>();
        File red5Jar = new File(homeDir, "red5.jar");
        if (red5Jar.exists()) {
            files.add(red5Jar);
        }
        File[] filesInLib = libDir.listFiles();
        if (filesInLib != null) {
            for (File file : filesInLib) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }
        String version = data.VERSION;
        if (version == null) {
            version = EMPTY;
        }
        return new ApplicationServerInfo(
                files.toArray(new File[files.size()]), message("default.application.server.name", version));
    }

    public ApplicationServerPersistentData createPersistentDataEmptyInstance() {
        return new Red5PersistentData();
    }

    public ApplicationServerPersistentDataEditor createConfigurable() {
        return new SelectRed5LocationDialog();
    }
}