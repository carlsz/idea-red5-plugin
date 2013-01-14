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
 * $Id: Red5ConfigurationType.java 2 2009-03-31 21:23:51Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.javaee.run.configuration.J2EEConfigurationFactory;
import com.intellij.javaee.run.configuration.J2EEConfigurationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public class Red5ConfigurationType extends J2EEConfigurationType {

    private static final Icon ICON = IconLoader.getIcon("/resources/red5-sm.png");

    protected RunConfiguration createJ2EEConfigurationTemplate(ConfigurationFactory factory,
                                                               Project project,
                                                               boolean isLocal) {
        return J2EEConfigurationFactory.getInstance().createJ2EERunConfiguration(factory,
                project, new Red5Model(), Red5ServerManager.getInstance(), isLocal, new Red5StartupPolicy());
    }

    public String getDisplayName() {
        return Red5Bundle.message("red5.run.configuration.title");
    }

    public String getConfigurationTypeDescription() {
        return Red5Bundle.message("red5.run.configuration.description");
    }

    public Icon getIcon() {
        return ICON;
    }

    @NotNull
    public String getId() {
        return "#com.googlecode.idea.red5.Red5ConfigurationFactory";
    }
}