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
 * $Id: SelectRed5LocationDialog.java 3 2009-04-01 22:59:12Z carlsz $
 */
package com.googlecode.idea.red5;

import static com.googlecode.idea.red5.Red5Bundle.message;
import static com.googlecode.idea.red5.Red5Constants.RED5_CONFIG_DIRECTORY_NAME;
import static com.googlecode.idea.red5.Red5Constants.RED5_LIB_DIRECTORY_NAME;
import static com.googlecode.idea.red5.Red5Utils.getDefaultLocation;
import static com.googlecode.idea.red5.Red5PersistentData.VERSION_DOT_EIGHT;
import static com.googlecode.idea.red5.Red5PersistentData.VERSION_DOT_SIX;
import static com.googlecode.idea.red5.Red5PersistentData.VERSION_DOT_SEVEN;
import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentDataEditor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import static com.intellij.openapi.util.IconLoader.getIcon;
import com.intellij.ui.DocumentAdapter;
import static org.apache.commons.lang.StringUtils.EMPTY;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import java.awt.CardLayout;
import java.io.File;
import static java.io.File.separator;
import static java.io.File.separatorChar;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class SelectRed5LocationDialog extends ApplicationServerPersistentDataEditor<Red5PersistentData> {

    private static final Logger logger = Logger.getInstance(SelectRed5LocationDialog.class.getName());

    private JPanel panel;
    private TextFieldWithBrowseButton homeDir;
    private JLabel versionLabel;
    private JLabel errorLabel;
    private JLabel versionTitleLabel;
    private JPanel infoPanel;

    public SelectRed5LocationDialog() {
        errorLabel.setIcon(getIcon("/runConfigurations/configurationWarning.png"));
        updateInfoPanel(true);
        initChooser(homeDir, message("chooser.title.red5.home.directory"), message("chooser.description.red5.home.directory"));
        homeDir.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
            public void textChanged(DocumentEvent event) {
                update();
            }
        });
    }

    private void updateInfoPanel(final boolean valid) {
        ((CardLayout) infoPanel.getLayout()).show(infoPanel, valid ? "valid" : "error");
    }

    private static void initChooser(TextFieldWithBrowseButton field, String title, String description) {
        field.setText(getDefaultLocation());
        field.getTextField().setEditable(true);
        field.addBrowseFolderListener(title, description, null, new FileChooserDescriptor(false, true, false, false, false, false));
    }

    private void update() {
        versionLabel.setText(getVersion());
        updateErrorLabel();
    }

    private void updateErrorLabel() {
        boolean hasError = false;
        try {
            checkDirectories();
        }
        catch (ConfigurationException e) {
            errorLabel.setText(e.getMessage());
            hasError = true;
        }
        updateInfoPanel(!hasError);
    }

    private String getVersion() {
        String home = homeDir.getText();
        if (home.length() == 0) {
            return EMPTY;
        }
        @NonNls final String pathToRed5Jar =
                new StringBuilder().append(home).append(separator).append("red5.jar").toString();
        File red5 = new File(pathToRed5Jar);
        if (red5.exists()) {
            try {
                JarFile jar = new JarFile(pathToRed5Jar);
                Manifest manifest = jar.getManifest();
                Attributes attrs = manifest.getAttributes("");
                if (attrs != null) {
                    String version = attrs.getValue("Red5-Version");
                    if (version.startsWith("0.7")) {
                        logger.debug("Setting to version 0.7.");
                        return VERSION_DOT_SEVEN;
                    } else if (version.startsWith("0.6")) {
                        logger.debug("Setting to version 0.6.");
                        return VERSION_DOT_SIX;
                    }
                }
            } catch (IOException e) {
                logger.error("No version found! Returning the default.");
            }
        }
        return VERSION_DOT_EIGHT;
    }

    private void checkDirectories() throws ConfigurationException {
        File home = new File(homeDir.getText()).getAbsoluteFile();
        checkIsDirectory(home);
        checkIsDirectory(new File(home, RED5_CONFIG_DIRECTORY_NAME));
        checkIsDirectory(new File(home, RED5_LIB_DIRECTORY_NAME));
    }

    private static void checkIsDirectory(File file) throws ConfigurationException {
        if (!file.isDirectory()) {
            throw new ConfigurationException(message("message.text.cant.find.directory", file.getAbsolutePath()));
        }
    }

    protected void resetEditorFrom(Red5PersistentData data) {
        homeDir.setText(data.RED5_HOME.replace('/', separatorChar));
        update();
    }

    protected void applyEditorTo(Red5PersistentData data) throws ConfigurationException {
        String version = versionLabel.getText();
        if (version.length() == 0) {
            version = VERSION_DOT_EIGHT;
        }
        File home = new File(homeDir.getText()).getAbsoluteFile();
        data.RED5_HOME = home.getAbsolutePath().replace(separatorChar, '/');
        data.VERSION = version;
    }

    @NotNull
    protected JComponent createEditor() {
        return panel;
    }

    protected void disposeEditor() {
        // Do nothing...
    }

    protected JComponent createCenterPanel() {
        return panel;
    }
}
