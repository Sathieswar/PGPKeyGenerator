package com.sathieswar.pgpkeygenerator.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		IFolderLayout newPGPKeyConfigurationLayout = layout.createFolder(
				"NewPGPKeyConfiguration", IPageLayout.LEFT, 1.0f,
				layout.getEditorArea());
		newPGPKeyConfigurationLayout
				.addView("com.sathieswar.pgpkeygenerator.ui.views.NewPGPKeyConfigurationView");
	}
}
