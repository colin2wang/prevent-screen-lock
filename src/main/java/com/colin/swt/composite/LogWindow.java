package com.colin.swt.composite;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.colin.swt.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LogWindow extends Group {
	
	private static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();
	private static final ch.qos.logback.classic.Logger ROOT_LOGGER = LOGGER_CONTEXT.getLogger(Logger.ROOT_LOGGER_NAME);
	private static final Logger LOGGER = LoggerFactory.getLogger(LogWindow.class);
	
	private static final List<String> LOG_LEVELS = Arrays.asList("ERROR", "WARN", "INFO", "DEBUG", "TRACE");
	
	private Text text;

	/**
	 * Create the composite.
	 */
	public LogWindow(Composite parent, int style) {
		super(parent, style);
		setText("Logs");
		setLayout(new GridLayout(1, false));
		
		text = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData textGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		textGridData.heightHint = 100;
		text.setLayoutData(textGridData);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label lblLogLevel = new Label(composite, SWT.NONE);
		lblLogLevel.setText("Log Level");
		
		Combo cmbLogLevel = new Combo(composite, SWT.READ_ONLY);
		cmbLogLevel.setItems(LOG_LEVELS.toArray(new String[0]));
		cmbLogLevel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Level logLevel = Level.toLevel(cmbLogLevel.getText());
				ROOT_LOGGER.setLevel(logLevel);
				LOGGER.info("Change Log Level to {}", logLevel);
			}
		});
		cmbLogLevel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		selectLogLevel(cmbLogLevel);
		
		Button btnEnableLog = new Button(composite, SWT.CHECK);
		btnEnableLog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LogWindowAppender.setEnableLogs(btnEnableLog.getSelection());
			}
		});
		btnEnableLog.setSelection(Constants.ENABLE_LOGS);
		btnEnableLog.setText("Enable Log");
		
		Button btnCleanLog = new Button(composite, SWT.NONE);
		btnCleanLog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
			}
		});
		btnCleanLog.setText("Clean Log");

		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					String message = LogWindowAppender.LOG_CACHE.take();
					Display.getDefault().syncExec(() -> {
						text.append(message);
					});
				} catch (InterruptedException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}, 500, 100);
	}

	private void selectLogLevel(Combo cmbLogLevel) {
		String logLevelStr = ROOT_LOGGER.getLevel().toString();
		cmbLogLevel.select(LOG_LEVELS.indexOf(logLevelStr));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
