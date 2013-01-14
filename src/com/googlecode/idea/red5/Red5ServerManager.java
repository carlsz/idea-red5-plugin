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
 * $Id: Red5ServerManager.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import static com.googlecode.idea.red5.Red5Bundle.message;
import com.intellij.facet.FacetTypeId;
import com.intellij.javaee.appServerIntegrations.AppServerDeployedFileUrlProvider;
import com.intellij.javaee.appServerIntegrations.AppServerIntegration;
import com.intellij.javaee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.javaee.deployment.DeploymentProvider;
import com.intellij.javaee.facet.JavaeeFacet;
import com.intellij.javaee.facet.JavaeeFacetUtil;
import com.intellij.javaee.openapi.ex.AppServerIntegrationsManager;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.util.Collection;

public class Red5ServerManager extends AppServerIntegration {

    public static final Icon ICON = IconLoader.getIcon("/resources/red5-sm.png");
    private final Red5DeploymentProvider deploymentProvider = new Red5DeploymentProvider();
    private ApplicationServerHelper applicationServerHelper = new Red5ApplicationServerHelper();

    public static Red5ServerManager getInstance() {
        return AppServerIntegrationsManager.getInstance().getIntegration(Red5ServerManager.class);
    }

    @NotNull
    @NonNls
    public String getComponentName() {
        return "#com.googlecode.idea.red5.Red5ServerManager";
    }

    public String getPresentableName() {
        return message("red5.server.name");
    }

    public static Icon getIcon() {
        return ICON;
    }

    public DeploymentProvider getDeploymentProvider() {
        return deploymentProvider;
    }

    public ApplicationServerHelper getApplicationServerHelper() {
        return applicationServerHelper;
    }

    @NotNull
    public Collection<FacetTypeId<? extends JavaeeFacet>> getSupportedFacetTypes() {
        return JavaeeFacetUtil.getInstance().getSingletonCollection(WebFacet.ID);
    }

    @NotNull
    public AppServerDeployedFileUrlProvider getDeployedFileUrlProvider() {
        return Red5UrlMapping.getInstance();
    }
}