package com.colin.swt.composite;

import com.colin.util.UserActionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;

public class KeyPressComposite extends Composite {

	private static final Logger LOGGER = LoggerFactory.getLogger(KeyPressComposite.class);

	private boolean isContinue = false;
	private Text txtSleepTime;
	private Button btnStart;
	private Button btnStop;

	/**
	 * Create the composite.
	 */

	public KeyPressComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Sleep Time");
		
		txtSleepTime = new Text(this, SWT.BORDER | SWT.CENTER);
		txtSleepTime.setText("60");
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 100;
		txtSleepTime.setLayoutData(gd_text);
						
						Label lblSeconds = new Label(this, SWT.NONE);
						lblSeconds.setText("Second(s)");
				
				
						btnStart = new Button(this, SWT.NONE);
						GridData gd_btnNewButton = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
						gd_btnNewButton.widthHint = 80;
						btnStart.setLayoutData(gd_btnNewButton);
						btnStart.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {

								btnStop.setEnabled(true);
								int sleep = 60;
								try {
									sleep = Integer.valueOf(txtSleepTime.getText());
								} catch (Exception ex) {
									LOGGER.warn("Sleep Time: {} is not valid, use default: {}", txtSleepTime.getText(), sleep);
								}

								isContinue = true;
								int finalSleep = sleep;
								new Thread(() -> {
									LOGGER.info("Will continuing to press key in every {} second(s).", finalSleep);
									LOGGER.info("Key press Thread: {} start.", Thread.currentThread().getName());
									while (isContinue) {
										try {
											UserActionUtil.pressSingleKeyByNumber(KeyEvent.VK_NUM_LOCK);
											for (int i = 0; i < finalSleep * 2; i++) {
												Thread.sleep(500);
												if (!isContinue) {
													break;
												}
											}

										} catch (InterruptedException ex) {
											LOGGER.error(ex.getMessage(), e);
										}
									}
									LOGGER.info("Key press Thread: {} stop.", Thread.currentThread().getName());
									Display.getDefault().syncExec(() -> {
										btnStart.setEnabled(true);
										btnStop.setEnabled(false);
									});
								}).start();

								btnStart.setEnabled(false);
							}
						});
						btnStart.setText("Open");
				new Label(this, SWT.NONE);
				
				btnStop = new Button(this, SWT.NONE);
				btnStop.setEnabled(false);
				btnStop.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						LOGGER.info("Notify running key press thread to stop.");
						isContinue = false;
					}
				});
				GridData gd_btnStop = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_btnStop.widthHint = 80;
				btnStop.setLayoutData(gd_btnStop);
				btnStop.setText("Stop");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
