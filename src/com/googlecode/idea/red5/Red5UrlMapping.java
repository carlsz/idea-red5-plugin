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
 * $Id: Red5UrlMapping.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.javaee.appServerIntegrations.ApplicationServerUrlMapping;
import com.intellij.javaee.deployment.DeploymentModel;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.serverInstances.J2EEServerInstance;
import com.intellij.javaee.web.WebUtil;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.deployment.DeploymentUtil;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.CharFilter;
import com.intellij.openapi.util.text.StringUtil;
import static com.intellij.openapi.util.text.StringUtil.trimEnd;
import static com.intellij.openapi.util.text.StringUtil.trimStart;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.jsp.WebDirectoryElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Red5UrlMapping extends ApplicationServerUrlMapping {

    private static final Red5UrlMapping INSTANCE = new Red5UrlMapping();

    public static Red5UrlMapping getInstance() {
        return INSTANCE;
    }

    @Override
    public VirtualFile findSourceFile(@NotNull final J2EEServerInstance serverInstance,
                                      @NotNull final CommonModel commonModel,
                                      @NotNull final String url) {
        final String baseUrl = ((Red5Model) commonModel.getServerModel()).getUrlForBrowser(false);
        if (!url.startsWith(baseUrl)) {
            return null;
        }
        String relative = trimStart(url.substring(baseUrl.length()), "/");
        int end = StringUtil.findFirst(relative, new CharFilter() {
            public boolean accept(final char ch) {
                return ch == '?' || ch == '#' || ch == ';';
            }
        });
        if (end != -1) {
            relative = relative.substring(0, end);
        }
        relative = trimEnd(relative, "/");
        final Pair<DeploymentModel, String> pair = findDeploymentModel(relative, commonModel);
        if (pair != null) {
            final WebFacet webFacet = (WebFacet) pair.getFirst().getFacet();
            if (webFacet != null) {
                return findInFacet(webFacet, pair.getSecond());
            }
        }
        return null;
    }

    @Nullable
    private static VirtualFile findInFacet(final WebFacet webFacet, final String path) {
        final WebDirectoryElement element = WebUtil.getWebUtil().createWebDirectoryElement(webFacet, path, false);
        return element.getOriginalVirtualFile();
    }

    @Nullable
    private static Pair<DeploymentModel, String> findDeploymentModel(final String relative, final CommonModel commonModel) {
        DeploymentModel defaultModel = null;
        for (DeploymentModel deploymentModel : commonModel.getDeploymentModels()) {
            String contextPath = trimStart(trimEnd(((Red5ModuleDeploymentModel) deploymentModel).CONTEXT_PATH, "/"), "/");
            if (contextPath.length() == 0) {
                defaultModel = deploymentModel;
            } else if (relative.startsWith(contextPath)) {
                return Pair.create(deploymentModel, relative.substring(contextPath.length()));
            }
        }
        return defaultModel != null ? Pair.create(defaultModel, relative) : null;
    }

    public
    @Nullable
    String getUrlForDeployedFile(J2EEServerInstance serverInstance, DeploymentModel deploymentModel, String relativePath) {
        final Red5Model serverModel = (Red5Model) serverInstance.getCommonModel().getServerModel();
        return DeploymentUtil.concatPaths(serverModel.getUrlForBrowser(false),
                ((Red5ModuleDeploymentModel) deploymentModel).CONTEXT_PATH, relativePath);
    }

    private Red5UrlMapping() {}
}