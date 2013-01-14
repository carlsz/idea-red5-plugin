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
 * $Id: Red5DeploymentProvider.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.javaee.deployment.DeploymentProvider;
import com.intellij.javaee.deployment.DeploymentModel;
import com.intellij.javaee.deployment.DeploymentMethod;
import com.intellij.javaee.serverInstances.J2EEServerInstance;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.facet.JavaeeFacet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.facet.pointers.FacetPointer;

public class Red5DeploymentProvider implements DeploymentProvider {

    public void doDeploy(Project project, J2EEServerInstance j2EEServerInstance, DeploymentModel deploymentModel) {
        
    }

    public DeploymentModel createNewDeploymentModel(CommonModel commonModel, FacetPointer<JavaeeFacet> javaeeFacetFacetPointer) {
        return null;
    }

    public SettingsEditor<DeploymentModel> createAdditionalDeploymentSettingsEditor(CommonModel commonModel, JavaeeFacet javaeeFacet) {
        return null;
    }

    public void startUndeploy(J2EEServerInstance j2EEServerInstance, DeploymentModel deploymentModel) {

    }

    public void updateDeploymentStatus(J2EEServerInstance j2EEServerInstance, DeploymentModel deploymentModel) {

    }

    public String getHelpId() {
        return null;
    }

    public DeploymentMethod[] getAvailableMethods() {
        return null;
    }
}