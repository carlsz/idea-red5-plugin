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
 * $Id: Red5Model.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import static com.googlecode.idea.red5.Red5Bundle.message;
import static com.googlecode.idea.red5.Red5Utils.DEFAULT_PORT;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.javaee.appServerIntegrations.ApplicationServer;
import com.intellij.javaee.deployment.DeploymentModel;
import com.intellij.javaee.deployment.DeploymentProvider;
import com.intellij.javaee.facet.JavaeeFacetUtil;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.run.configuration.ServerModel;
import com.intellij.javaee.run.execution.DefaultOutputProcessor;
import com.intellij.javaee.run.execution.OutputProcessor;
import com.intellij.javaee.serverInstances.J2EEServerInstance;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Red5Model implements ServerModel {

    public boolean DEPLOY_DEMO_INSTALLER = false;

    private CommonModel commonModel;

    public J2EEServerInstance createServerInstance() throws ExecutionException {
        return new Red5ServerInstance(commonModel);
    }

    public DeploymentProvider getDeploymentProvider() {
        return null;
    }

    public SettingsEditor<CommonModel> getEditor() {
        return new Red5LocalRunConfigurationEditor();
    }

    public OutputProcessor createOutputProcessor(ProcessHandler processHandler, J2EEServerInstance j2EEServerInstance) {
        return new DefaultOutputProcessor(processHandler);
    }

    public List<Pair<String, Integer>> getAddressesToCheck() {
        List<Pair<String, Integer>> result = new ArrayList<Pair<String, Integer>>();
        result.add(new Pair<String, Integer>(commonModel.getHost(), commonModel.getPort()));
        return result;
    }

    public String getDefaultUrlForBrowser() {
        return getUrlForBrowser(true);
    }

    public String getUrlForBrowser(final boolean addContextPath) {
        @NonNls StringBuilder result = new StringBuilder();
        result.append("http://");
        result.append(commonModel.getHost());
        result.append(":");
        result.append(String.valueOf(commonModel.getPort()));
        if (addContextPath) {
            String defaultContext = getDefaultContext();
            if (defaultContext != null && !defaultContext.equals("/")) {
                if (!StringUtil.startsWithChar(defaultContext, '/')) {
                    result.append("/");
                }
                result.append(defaultContext);
            }
        }
        result.append("/");
        return result.toString();
    }

    @Nullable
    private String getDefaultContext() {
        Collection<WebFacet> webFacets = JavaeeFacetUtil.getInstance().getJavaeeFacets(WebFacet.ID, commonModel.getProject());
        for (WebFacet webFacet : webFacets) {
            DeploymentModel deploymentModel = commonModel.getDeploymentModel(webFacet);
            if (deploymentModel instanceof Red5ModuleDeploymentModel) {
                return ((Red5ModuleDeploymentModel) deploymentModel).CONTEXT_PATH;
            }
        }
        return null;
    }

    public void checkConfiguration() throws RuntimeConfigurationException {
        // Do nothing...
    }

    public int getDefaultPort() {
        return DEFAULT_PORT;
    }

    public void setCommonModel(CommonModel commonModel) {
        this.commonModel = commonModel;
    }

    public Red5Model clone() throws CloneNotSupportedException {
        return (Red5Model) super.clone();
    }

    public int getLocalPort() {
        return DEFAULT_PORT;
    }

    public String getHomeDirectory() throws RuntimeConfigurationException {
        ApplicationServer applicationServer = commonModel.getApplicationServer();
        if (applicationServer == null)
            throw new RuntimeConfigurationError(message("exception.text.application.server.not.specified"));
        Red5PersistentData data = ((Red5PersistentData) applicationServer.getPersistentData());
        return FileUtil.toSystemDependentName(data.RED5_HOME);
    }

    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }
}