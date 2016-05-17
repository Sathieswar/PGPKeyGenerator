/*
 *  Copyright (C) 2016
 *
 *  The program(s) herein may be used and/or copied only with the
 *  written permission of the author or in accordance with the terms
 *  and conditions stipulated in the agreement/contract under which the
 *  program(s) have been supplied.
 *
 * Date        	 Author             Changes
 * May 17, 2016  Sathieswar         Created
 */
package com.sathieswar.pgpkeygenerator.ui.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.sathieswar.pgpkeygenerator.utils.GeneratePGPKeyPair;

public class NewPGPKeyConfigurationView extends ViewPart {

	public NewPGPKeyConfigurationView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		Label emailAddressLabel = new Label(container, SWT.NONE);
		emailAddressLabel.setText("Email Address:");

		final Text emailAddressText = new Text(container, SWT.NONE);
		emailAddressText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));

		Label passPhraseLabel = new Label(container, SWT.NONE);
		passPhraseLabel.setText("PassPhrase:");

		final Text passPhraseText = new Text(container, SWT.PASSWORD);
		passPhraseText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));

		new Label(container, SWT.NONE);

		Button generateKeyPairButton = new Button(container, SWT.NONE);
		generateKeyPairButton.setText("Generate Key Pair");
		generateKeyPairButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!emailAddressText.getText().trim().equals("")
						&& !passPhraseText.getText().trim().equals("")) {

					DirectoryDialog directoryDialog = new DirectoryDialog(
							container.getShell());
					String outputPath = directoryDialog.open();

					if (!outputPath.equals("")) {
						boolean isGenerated = new GeneratePGPKeyPair()
								.createPGPKeyPair(emailAddressText.getText(),
										passPhraseText.getText(), outputPath);

						if (isGenerated) {
							MessageDialog.openInformation(container.getShell(),
									"Successfull",
									"Key pair successfully exported to the output path...!!!");

							resetFields(emailAddressText, passPhraseText);

						} else {
							MessageDialog.openError(container.getShell(),
									"Error",
									"Error occurred generating key pair...!!!");
						}
					} else {
						MessageDialog.openError(container.getShell(),
								"Invalid Path", "Invalid output path...!!!");
					}

				} else {
					MessageDialog.openError(container.getShell(),
							"Empty Fields", "Fields cannot be empty...!!!");
				}
			}

			private void resetFields(final Text emailAddressText,
					final Text passPhraseText) {
				emailAddressText.setText("");
				passPhraseText.setText("");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	@Override
	public void setFocus() {

	}

}
