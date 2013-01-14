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
 * $Id: Red5StartupPolicy.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.run.localRun.CommandLineExecutableObject;
import com.intellij.javaee.run.localRun.EnvironmentHelper;
import com.intellij.javaee.run.localRun.ExecutableObject;
import com.intellij.javaee.run.localRun.ExecutableObjectStartupPolicy;
import com.intellij.javaee.run.localRun.ScriptHelper;
import com.intellij.javaee.run.localRun.ScriptsHelper;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import static com.intellij.openapi.util.SystemInfo.isWindows;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.util.Map;

public class Red5StartupPolicy implements ExecutableObjectStartupPolicy {

    private static final Logger logger = Logger.getInstance(Red5StartupPolicy.class.getName());

    @NonNls protected static final String TEMP_FILE_NAME = "temp";

    @NonNls private static final String BOOTSTRAP_JAR_NAME = "bootstrap.jar";
    @NonNls private static final String CATALINA_TMPDIR_ENV_PROPERTY = "CATALINA_TMPDIR";
    @NonNls private static final String JAR_PARAMETER = "-jar";
    @NonNls private static final String JAVA_HOME_ENV_PROPERTY = "JAVA_HOME";
    @NonNls private static final String JAVA_VM_ENV_VARIABLE = "JAVA_OPTS";
    
    public ScriptsHelper getStartupHelper() {
        return null;
    }

    public ScriptsHelper getShutdownHelper() {
        return null;
    }

    public ScriptHelper createStartupScriptHelper(ProgramRunner runner) {
        return new ScriptHelper() {
            public ExecutableObject getDefaultScript(CommonModel commonModel) {
                try {
                    Red5Model model = (Red5Model) commonModel.getServerModel();
                    final File red5Script = getExecutableStartupFile(model);
                    if (red5Script.exists()) {
                        logger.debug("Returning startup shell script.");
                        return new CommandLineExecutableObject(new String[]{red5Script.getAbsolutePath()}, null);
                    } else {
                        logger.debug("Creating Red5 executable on the fly.");
                        return createRed5Executable(commonModel, model, "launch");
                    }
                } catch (RuntimeConfigurationException e) {
                    // Do nothing...
                }
                return null;
            }
        };
    }

    public ScriptHelper createShutdownScriptHelper(ProgramRunner runner) {
        return new ScriptHelper() {
            public ExecutableObject getDefaultScript(CommonModel commonModel) {
                try {
                    Red5Model model = (Red5Model) commonModel.getServerModel();
                    final File red5Script = getExecutableShutdownFile(model);
                    if (red5Script.exists()) {
                        logger.debug("Returning shutdown shell script.");
                        return new CommandLineExecutableObject(new String[]{red5Script.getAbsolutePath()}, null);
                    } else {
                        logger.debug("Creating Red5 executable on the fly.");
                        return createRed5Executable(commonModel, model, "stop");
                    }
                } catch (RuntimeConfigurationException e) {
                    // Do nothing...
                }
                return null;
            }
        };
    }

    public EnvironmentHelper getEnvironmentHelper() {
        return new EnvironmentHelper() {
            public boolean defaultVmVariableNameCanBeChangedByUser() {
                return true;
            }
        };
    }

    private static ExecutableObject createRed5Executable(final CommonModel model,
                                                           final Red5Model tomcatModel,
                                                           final @NonNls String actionName) throws RuntimeConfigurationException {
        final Sdk projectJdk = ProjectRootManager.getInstance(model.getProject()).getProjectJdk();
        final @NonNls String vmExecutablePath = projectJdk == null || !(projectJdk.getSdkType() instanceof JavaSdkType)
                ? "java" : ((JavaSdkType) projectJdk.getSdkType()).getVMExecutablePath(projectJdk);
        return new CommandLineExecutableObject(new String[]{
                vmExecutablePath
        }, null) {
            protected GeneralCommandLine createCommandLine(String[] parameters, final Map<String, String> envVariables) {
                return super.createCommandLine(parameters, envVariables);
            }
        };
    }

    private static File getExecutableStartupFile(final Red5Model model) throws RuntimeConfigurationException {
        return new File(model.getHomeDirectory(), getDefaultStartupScriptName());
    }

    private static File getExecutableShutdownFile(final Red5Model model) throws RuntimeConfigurationException {
        return new File(model.getHomeDirectory(), getDefaultShutdownScriptName());
    }

    @NonNls
    private static String getDefaultStartupScriptName() {
        return isWindows ? "red5.bat" : "red5.sh";
    }

    @NonNls
    private static String getDefaultShutdownScriptName() {
        return isWindows ? "red5-shutdown.bat" : "red5-shutdown.sh";
    }
}