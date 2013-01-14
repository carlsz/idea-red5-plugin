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
 * $Id: Red5LocalRunConfigurationEditor.java 2 2009-03-31 21:23:51Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Red5LocalRunConfigurationEditor extends SettingsEditor<CommonModel> {

    private JPanel panel;
    private JCheckBox deployInstaller;

    protected void resetEditorFrom(CommonModel commonModel) {
        deployInstaller.setSelected(((Red5Model) commonModel.getServerModel()).DEPLOY_DEMO_INSTALLER);
    }

    protected void applyEditorTo(CommonModel commonModel) throws ConfigurationException {
        ((Red5Model) commonModel.getServerModel()).DEPLOY_DEMO_INSTALLER = deployInstaller.isSelected();
    }

    @NotNull
    protected JComponent createEditor() {
        return panel;
    }

    protected void disposeEditor() {
        // Do nothing...
    }
}