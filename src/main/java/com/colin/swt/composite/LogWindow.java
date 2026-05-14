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

/**
 * Log window component that displays application logs in real-time.
 * Provides controls to adjust log level, enable/disable logging, and clear log content.
 */
public class LogWindow extends Group {
	
	private static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();
	private static final ch.qos.logback.classic.Logger ROOT_LOGGER = LOGGER_CONTEXT.getLogger(Logger.ROOT_LOGGER_NAME);
	private static final Logger LOGGER = LoggerFactory.getLogger(LogWindow.class);
	
	private static final List<String> LOG_LEVELS = Arrays.asList("ERROR", "WARN", "INFO", "DEBUG", "TRACE");
	
	private Text text;

	/**
	 * Creates the log window composite with log display area and control buttons.
	 *
	 * @param parent the parent composite
	 * @param style  the style flags for the composite
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
				// Change the root logger level when user selects a different log level
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
				// Enable or disable log display based on checkbox state
				LogWindowAppender.setEnableLogs(btnEnableLog.getSelection());
			}
		});
		btnEnableLog.setSelection(Constants.ENABLE_LOGS);
		btnEnableLog.setText("Enable Log");
		
		Button btnCleanLog = new Button(composite, SWT.NONE);
		btnCleanLog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Clear all displayed log messages
				text.setText("");
			}
		});
		btnCleanLog.setText("Clean Log");

		// Schedule a periodic task to retrieve and display log messages from the cache
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					// Poll for log messages from the cache (non-blocking)
					String message = LogWindowAppender.LOG_CACHE.poll();
					if (message != null) {
						// Append the message to the text area in the UI thread
						Display.getDefault().syncExec(() -> {
							text.append(message);
						});
					}
				} catch (Exception e) {
					LOGGER.error("Error while reading log message from cache", e);
				}
			}
		}, 500, 100);
	}

	/**
	 * Sets the initial log level selection in the combo box based on current root logger level.
	 *
	 * @param cmbLogLevel the combo box to update
	 */
	private void selectLogLevel(Combo cmbLogLevel) {
		String logLevelStr = ROOT_LOGGER.getLevel().toString();
		cmbLogLevel.select(LOG_LEVELS.indexOf(logLevelStr));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
