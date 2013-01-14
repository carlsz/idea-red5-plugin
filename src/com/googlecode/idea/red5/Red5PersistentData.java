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
 * $Id: Red5PersistentData.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import com.intellij.javaee.appServerIntegrations.DefaultPersistentData;
import com.intellij.openapi.util.InvalidDataException;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import static org.apache.commons.lang.StringUtils.EMPTY;

public class Red5PersistentData extends DefaultPersistentData {

    public String RED5_HOME = EMPTY;
    public String VERSION = VERSION_DOT_EIGHT;

    @NonNls public static final String VERSION_DOT_SIX = "0.6";
    @NonNls public static final String VERSION_DOT_SEVEN = "0.7";
    @NonNls public static final String VERSION_DOT_EIGHT = "0.8";

    public Red5PersistentData() {
        RED5_HOME = Red5Utils.getDefaultLocation();
    }

    public void readExternal(final Element element) throws InvalidDataException {
        super.readExternal(element);
        if (VERSION.startsWith("0.7")) {
            VERSION = Red5PersistentData.VERSION_DOT_SEVEN;
        } else if (VERSION.startsWith("0.6")) {
            VERSION = Red5PersistentData.VERSION_DOT_SIX;
        }
    }
}