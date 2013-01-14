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
 * $Id: Red5Constants.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.util.descriptors.ConfigFileMetaData;
import com.intellij.util.descriptors.ConfigFileVersion;
import static com.googlecode.idea.red5.Red5Bundle.message;
import org.jetbrains.annotations.NonNls;
import org.jdom.Comment;

public class Red5Constants {

    @NonNls public static final String CONTEXT_XML_TEMPLATE_FILE_NAME = "context.xml";
    @NonNls public static final String RED5_LIB_DIRECTORY_NAME = "lib";
    @NonNls public static final String RED5_CONFIG_DIRECTORY_NAME = "conf";
    @NonNls public static final String RED5_WORK_DIRECTORY_NAME = "work";
    @NonNls public static final String SCRATCHDIR_NAME = "_scratchdir";
    @NonNls public static final String SERVER_XML = "server.xml";
    @NonNls public static final String TOMCAT_VERSION_5x = "5.x";
    @NonNls public static final String WEB_XML = "web.xml";

    public static final Comment CONTEXT_COMMENT = new Comment(
            message("comment.text.context.generated.by.idea")
    );
        
    public static final ConfigFileVersion[] DESCRIPTOR_VERSIONS = {
            new ConfigFileVersion(TOMCAT_VERSION_5x, CONTEXT_XML_TEMPLATE_FILE_NAME)
    };

    public static final ConfigFileMetaData CONTEXT_XML_META_DATA =
            new ConfigFileMetaData(message("red5.deployment.descriptor.title"),
                    "context.xml", "META-INF", DESCRIPTOR_VERSIONS, null, true, true, true);

    private Red5Constants() {}
}