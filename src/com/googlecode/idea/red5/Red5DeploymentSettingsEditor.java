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
 * $Id: Red5DeploymentSettingsEditor.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.facet.pointers.FacetPointersManager;
import com.intellij.javaee.deployment.DeploymentModel;
import com.intellij.javaee.facet.JavaeeFacet;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.Factory;
import static com.googlecode.idea.red5.Red5Utils.getConfiguredContextPaths;
import org.jetbrains.annotations.NotNull;
import static org.apache.commons.lang.StringUtils.EMPTY;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Collection;

public class Red5DeploymentSettingsEditor extends SettingsEditor<DeploymentModel> {

    private JPanel panel;
    private JComboBox contextPath;

    public Red5DeploymentSettingsEditor(final CommonModel configuration, final JavaeeFacet facet) {
        super(new Factory<DeploymentModel>() {
            public Red5ModuleDeploymentModel create() {
                final FacetPointersManager manager = FacetPointersManager.getInstance(facet.getModule().getProject());
                return new Red5ModuleDeploymentModel(configuration, manager.create(facet));
            }
        });
    }

    protected void resetEditorFrom(DeploymentModel settings) {
        Red5Model model = (Red5Model) settings.getServerModel();
        updateContextPaths(model);
        setSelectedContextPath(((Red5ModuleDeploymentModel) settings).CONTEXT_PATH, true);
    }

    protected void applyEditorTo(DeploymentModel settings) throws ConfigurationException {
        ((Red5ModuleDeploymentModel) settings).CONTEXT_PATH = getSelectedContextPath();
    }

    @NotNull
    protected JComponent createEditor() {
        return panel;
    }

    protected void disposeEditor() {
        // Do nothing...
    }

    private String getSelectedContextPath() {
        final String item = (String) contextPath.getEditor().getItem();
        return (item != null) ? item : EMPTY;
    }

    private void setSelectedContextPath(String ctxPath, boolean addIfNotFound) {
        int itemCount = contextPath.getItemCount();
        for (int idx = 0; idx < itemCount; idx++) {
            String path = (String) contextPath.getItemAt(idx);
            if (ctxPath.equals(path)) {
                contextPath.setSelectedIndex(idx);
                return;
            }
        }
        if (addIfNotFound) {
            contextPath.addItem(ctxPath);
            contextPath.setSelectedItem(ctxPath);
        }
    }

    private void updateContextPaths(Red5Model configuration) {
        final String selectedContextPath = getSelectedContextPath();
        contextPath.removeAllItems();
        try {
            Collection<String> configuredContextPaths = getConfiguredContextPaths(configuration);
            for (String path : configuredContextPaths) {
                contextPath.addItem(path);
            }
        } catch (RuntimeConfigurationException e) {
            // Do nothing...
        }
        setSelectedContextPath(selectedContextPath, true);
    }
}