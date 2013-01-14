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
 * $Id: Red5Utils.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import static com.googlecode.idea.red5.Red5Bundle.message;
import static com.googlecode.idea.red5.Red5Constants.SERVER_XML;
import static com.googlecode.idea.red5.Red5Constants.RED5_CONFIG_DIRECTORY_NAME;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.EnvironmentUtil;
import static org.apache.commons.lang.StringUtils.EMPTY;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import static java.io.File.separator;
import static java.io.File.separatorChar;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Red5Utils {

    public static final int DEFAULT_PORT = 5080;
    public static final int DEFAULT_SHUTDOWN_PORT = 5080;

    @NonNls private static final String RED5_ENGINE = "red5Engine";
    @NonNls private static final String RED5_HOME_ENV_PROPERTY = "RED5_HOME";
    @NonNls private static final String CONTEXT_ELEM_NAME = "Context";
    @NonNls private static final String LOCALHOST_DIR = "0.0.0.0";
    @NonNls private static final String PATH_ATTR = "path";
    @NonNls private static final String ROOT_DIR_NAME = "ROOT";
    @NonNls private static final String XML_SUFFIX = ".xml";

    public static String getDefaultLocation() {
        String result = EnvironmentUtil.getEnviromentProperties().get(RED5_HOME_ENV_PROPERTY);
        if (result != null) {
            return result.replace(separatorChar, '/');
        } else {
            return EMPTY;
        }
    }

    public static Collection<String> getConfiguredContextPaths(Red5Model configuration) throws RuntimeConfigurationException {
        Set<String> result = new HashSet<String>();
        try {
            result.addAll(getContextPathsFromServerXML(configuration));
        } catch (ExecutionException e) {
            // Do nothing...
        }
        result.addAll(getContextPathsFromDirectory(configuration));
        return result;
    }

    private static Collection<String> getContextPathsFromDirectory(Red5Model model) throws RuntimeConfigurationException {
        File hostDir = new File(hostDir(model.getHomeDirectory()));
        File[] files = hostDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return StringUtil.endsWithIgnoreCase(pathname.getName(), XML_SUFFIX);
            }
        });
        Set<String> names = new HashSet<String>();
        if (files != null) {
            for (File file : files) {
                String fName = file.getName();
                names.add("/" + fName.substring(0, fName.length() - 4));
            }
        }
        return names;
    }

    @SuppressWarnings({"unchecked"})
    private static Collection<String> getContextPathsFromServerXML(Red5Model model) throws ExecutionException,
            RuntimeConfigurationException {
        String xmlPath = serverXML(model.getHomeDirectory());
        Document document = loadXMLFile(xmlPath);
        Element localHost = findLocalHost(document.getRootElement());
        List<Element> contextElements = localHost.getChildren(CONTEXT_ELEM_NAME);
        Set<String> contexts = new HashSet<String>(contextElements.size());
        for (final Element contextElement : contextElements) {
            String path = contextElement.getAttributeValue(PATH_ATTR);
            if (path != null) {
                contexts.add(path);
            }
        }
        return contexts;
    }

    public static Element findLocalHost(Element parentElement) throws ExecutionException {
        Element localhost = findElementByAttr(parentElement, "Host", "name", LOCALHOST_DIR);
        if (localhost == null) {
            throw new ExecutionException(message("exception.text.server.xml.does.not.contain.virtual.host.localhost"));
        }
        return localhost;
    }

    @SuppressWarnings({"unchecked"})
    @Nullable
    private static Element findElementByAttr(Element parent,
                                             @NonNls String tagName,
                                             @NonNls String attrName,
                                             @NonNls final String attrValue) {
        if (tagName.equalsIgnoreCase(parent.getName())) {
            String path = parent.getAttributeValue(attrName);
            if (path != null) {
                if (path.equalsIgnoreCase(attrValue)) {
                    return parent;
                }
            }
        }
        List<Element> children = parent.getChildren();
        for (final Element child : children) {
            Element elem = findElementByAttr(child, tagName, attrName, attrValue);
            if (elem != null) {
                return elem;
            }
        }
        return null;
    }

    @Nullable
    private static Element findContextInHostDirectory(String baseDirectoryPath, String contextPath) {
        String contextXmlPath = getContextXML(baseDirectoryPath, contextPath);
        try {
            Document contextDocument = loadXMLFile(contextXmlPath);
            return contextDocument.getRootElement();
        } catch (ExecutionException e) {
            // Do nothing...
        }
        return null;
    }

    public static String getContextXML(final String baseDirectoryPath, final String contextPath) {
        final String contextFileName = EMPTY.equals(contextPath) ? ROOT_DIR_NAME : contextPath.substring(1);
        return new StringBuilder(hostDir(baseDirectoryPath))
                .append(separator)
                .append(contextFileName.replace('/', '#').replace('\\', '#'))
                .append(XML_SUFFIX)
                .toString();
    }

    public static String hostDir(String baseDirectoryPath) {
        return new StringBuilder(baseConfigDir(baseDirectoryPath))
                .append(separator)
                .append(RED5_ENGINE)
                .append(separator)
                .append(LOCALHOST_DIR)
                .toString();
    }

    public static Document loadXMLFile(String xmlPath) throws ExecutionException {
        try {
            Document xmlDocument = JDOMUtil.loadDocument(new File(xmlPath));
            if (xmlDocument == null) {
                throw new ExecutionException(message("exception.text.cannot.find.file", xmlPath));
            }
            return xmlDocument;
        } catch (JDOMException e) {
            throw new ExecutionException(message("exception.text.cannot.load.file.bacause.of.1", xmlPath, e.getMessage()));
        } catch (IOException e) {
            throw new ExecutionException(message("exception.text.cannot.load.file.bacause.of.1", xmlPath, e.getMessage()));
        }
    }

    public static String serverXML(String baseDirectoryPath) {
        return new StringBuilder(baseConfigDir(baseDirectoryPath))
                .append(separator)
                .append(SERVER_XML)
                .toString();
    }

    public static String baseConfigDir(String baseDirectoryPath) {
        return new StringBuilder(baseDirectoryPath)
                .append(separator)
                .append(RED5_CONFIG_DIRECTORY_NAME)
                .toString();
    }

    private Red5Utils() {}
}