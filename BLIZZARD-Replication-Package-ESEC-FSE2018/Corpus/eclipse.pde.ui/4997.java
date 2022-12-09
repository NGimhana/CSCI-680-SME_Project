/*******************************************************************************
 * Copyright (c) 2009, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.ui.shared.target;

import org.eclipse.pde.core.target.ITargetLocation;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.pde.internal.core.target.ProfileBundleContainer;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.SWTFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;

/**
 * Wizard page for creating a profile (installation) bundle container.
 *
 * @see AddBundleContainerWizard
 * @see AddBundleContainerSelectionPage
 * @see ITargetLocation
 */
public class EditProfileContainerPage extends EditDirectoryContainerPage {

    private Button fUseDefaultConfig;

    private Label fConfigLabel;

    private Combo fConfigLocation;

    private Button fConfigBrowse;

    private Button fConfigVariables;

    /**
	 * Dialog settings key for the most recent config location
	 */
    //$NON-NLS-1$
    private static final String SETTINGS_CONFIG_1 = "config1";

    /**
	 * Dialog settings key for the second most recent config location
	 */
    //$NON-NLS-1$
    private static final String SETTINGS_CONFIG_2 = "config2";

    /**
	 * Dialog settings key for the third most recent config location
	 */
    //$NON-NLS-1$
    private static final String SETTINGS_CONFIG_3 = "config3";

    public  EditProfileContainerPage() {
        //$NON-NLS-1$
        super(null, "EditProfileContainer");
    }

    public  EditProfileContainerPage(ITargetLocation container) {
        //$NON-NLS-1$
        super(container, "EditProfileContainer");
    }

    @Override
    protected String getDefaultTitle() {
        if (fContainer != null) {
            return Messages.EditProfileContainerPage_3;
        }
        return Messages.AddProfileContainerPage_0;
    }

    @Override
    protected String getDefaultMessage() {
        return Messages.AddProfileContainerPage_1;
    }

    @Override
    protected void createLocationArea(Composite parent) {
        super.createLocationArea(parent);
        Composite configComp = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL, 0, 0);
        if (fContainer == null) {
            PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IHelpContextIds.LOCATION_ADD_INSTALLATION_WIZARD);
        } else {
            PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IHelpContextIds.LOCATION_EDIT_INSTALLATION_WIZARD);
        }
        fUseDefaultConfig = new Button(configComp, SWT.CHECK | SWT.RIGHT);
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        fUseDefaultConfig.setLayoutData(gd);
        fUseDefaultConfig.setFont(parent.getFont());
        fUseDefaultConfig.setText(Messages.AddProfileContainerPage_2);
        fUseDefaultConfig.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateConfigEnablement();
                containerChanged(0);
            }
        });
        fConfigLabel = SWTFactory.createLabel(configComp, Messages.AddProfileContainerPage_3, 1);
        ((GridData) fConfigLabel.getLayoutData()).horizontalIndent = 15;
        fConfigLocation = SWTFactory.createCombo(configComp, SWT.BORDER, 1, getConfigComboItems());
        fConfigLocation.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                // If the text is a combo item, immediately try to resolve, otherwise wait in case they type more
                boolean isItem = false;
                String[] items = fConfigLocation.getItems();
                for (int i = 0; i < items.length; i++) {
                    if (fConfigLocation.getText().equals(items[i])) {
                        isItem = true;
                        break;
                    }
                }
                containerChanged(isItem ? 0 : TYPING_DELAY);
            }
        });
        Composite buttonComp = SWTFactory.createComposite(configComp, 2, 2, GridData.CENTER, 0, 0);
        gd = (GridData) buttonComp.getLayoutData();
        gd.horizontalAlignment = SWT.RIGHT;
        fConfigBrowse = SWTFactory.createPushButton(buttonComp, Messages.AddProfileContainerPage_4, null);
        fConfigBrowse.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell());
                dialog.setFilterPath(fConfigLocation.getText());
                dialog.setText(Messages.AddProfileContainerPage_5);
                dialog.setMessage(Messages.AddProfileContainerPage_6);
                String result = dialog.open();
                if (result != null)
                    fConfigLocation.setText(result);
            }
        });
        fConfigVariables = SWTFactory.createPushButton(buttonComp, Messages.EditProfileContainerPage_1, null);
        fConfigVariables.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
                dialog.open();
                String variable = dialog.getVariableExpression();
                if (variable != null) {
                    fConfigLocation.setText(fConfigLocation.getText() + variable);
                }
            }
        });
    }

    private String[] getConfigComboItems() {
        List<String> previousConfigs = new ArrayList(4);
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String location = settings.get(SETTINGS_CONFIG_1);
            if (location != null) {
                previousConfigs.add(location);
            }
            location = settings.get(SETTINGS_CONFIG_2);
            if (location != null) {
                previousConfigs.add(location);
            }
            location = settings.get(SETTINGS_CONFIG_3);
            if (location != null) {
                previousConfigs.add(location);
            }
        }
        //$NON-NLS-1$
        previousConfigs.add("${eclipse_home}/configuration");
        return previousConfigs.toArray(new String[previousConfigs.size()]);
    }

    @Override
    public void storeSettings() {
        super.storeSettings();
        if (fConfigLocation.isEnabled()) {
            String newLocation = fConfigLocation.getText().trim();
            String[] items = fConfigLocation.getItems();
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(newLocation)) {
                    // Already have this location stored
                    return;
                }
            }
            IDialogSettings settings = getDialogSettings();
            if (settings != null) {
                String location = settings.get(SETTINGS_CONFIG_2);
                if (location != null) {
                    settings.put(SETTINGS_CONFIG_3, location);
                }
                location = settings.get(SETTINGS_CONFIG_1);
                if (location != null) {
                    settings.put(SETTINGS_CONFIG_2, location);
                }
                settings.put(SETTINGS_CONFIG_1, newLocation);
            }
        }
    }

    @Override
    protected void initializeInputFields(ITargetLocation container) {
        if (container instanceof ProfileBundleContainer) {
            String configLocation = ((ProfileBundleContainer) container).getConfigurationLocation();
            if (configLocation == null) {
                fUseDefaultConfig.setSelection(true);
                //$NON-NLS-1$
                fConfigLocation.setText(//$NON-NLS-1$
                "");
            } else {
                fUseDefaultConfig.setSelection(false);
                fConfigLocation.setText(configLocation);
            }
        } else {
            fUseDefaultConfig.setSelection(true);
            //$NON-NLS-1$
            fConfigLocation.setText("");
        }
        updateConfigEnablement();
        // Call super last as it will update the bundle tree
        super.initializeInputFields(container);
    }

    private void updateConfigEnablement() {
        boolean isDefault = fUseDefaultConfig.getSelection();
        fConfigLabel.setEnabled(!isDefault);
        fConfigLocation.setEnabled(!isDefault);
        fConfigBrowse.setEnabled(!isDefault);
        fConfigVariables.setEnabled(!isDefault);
    }

    @Override
    protected boolean validateInput() {
        boolean valid = super.validateInput();
        if (valid) {
            if (fConfigLocation.isEnabled()) {
                // Check if the text field is blank
                if (fConfigLocation.getText().trim().length() == 0) {
                    setMessage(Messages.EditProfileContainerPage_2);
                    return false;
                }
                // Resolve any variables
                String locationString = null;
                try {
                    locationString = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(fConfigLocation.getText().trim());
                } catch (CoreException e) {
                    setMessage(e.getMessage(), IMessageProvider.WARNING);
                    return true;
                }
                File configLocation = new File(locationString);
                // Check that the directory exists
                if (!configLocation.isDirectory()) {
                    setMessage(Messages.AddProfileContainerPage_8, IMessageProvider.WARNING);
                } else {
                    setMessage(getDefaultMessage());
                }
            }
        }
        return valid;
    }

    @Override
    protected ITargetLocation createContainer(ITargetLocation previous) throws CoreException {
        return getTargetPlatformService().newProfileLocation(fInstallLocation.getText(), fConfigLocation.isEnabled() ? fConfigLocation.getText() : null);
    }
}
