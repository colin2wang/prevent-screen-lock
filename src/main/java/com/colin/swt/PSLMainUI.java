package com.colin.swt;

import com.colin.swt.composite.KeyPressComposite;
import com.colin.swt.composite.LogWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class PSLMainUI {

	protected Shell shell;

	public static void main(String[] args) {
		try {
			PSLMainUI window = new PSLMainUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText("Prevent Screen Lock");
		shell.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmNewItem_0 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_0.setText("Main");
		KeyPressComposite sessionComposite = new KeyPressComposite(tabFolder, SWT.NONE);
		tbtmNewItem_0.setControl(sessionComposite);
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("New Item");
		

		LogWindow logWindow = new LogWindow(shell, SWT.NONE);
		logWindow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

	}

}
