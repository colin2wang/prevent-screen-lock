package com.colin.swt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BrowserDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	public BrowserDialog(Shell parent, int style) {
		super(parent, style | SWT.CLOSE | SWT.RESIZE);
		setText("Browser");
	}

	/**
	 * Open the dialog.
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(1024, 768);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Browser browser = new Browser(shell, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		browser.setUrl("https://www.baidu.com");

	}
}
