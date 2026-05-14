package com.colin.swt.composite;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.colin.swt.Constants;

import java.io.OutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Custom Logback appender that captures log messages and stores them in a cache for display in the UI.
 * This appender allows real-time log viewing within the application window.
 */
public class LogWindowAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
	
	protected static final BlockingDeque<String> LOG_CACHE = new LinkedBlockingDeque<>(500);
	
	private static boolean enableLogs = Constants.ENABLE_LOGS;

    /**
     * It is the encoder which is ultimately responsible for writing the event to
     * an {@link OutputStream}.
     */
    protected Encoder<ILoggingEvent> encoder;

    /**
     * All synchronization in this class is done via the lock object.
     */
    protected final ReentrantLock lock = new ReentrantLock(false);

    /**
     * This is the {@link OutputStream outputStream} where output will be written.
     */
    boolean immediateFlush = true;

    /**
     * Sets the layout for formatting log events.
     *
     * @param layout the layout to use for encoding log events
     */
    public void setLayout(Layout<ILoggingEvent> layout) {
        LayoutWrappingEncoder<ILoggingEvent> lwe = new LayoutWrappingEncoder<>();
        lwe.setLayout(layout);
        lwe.setContext(context);
        this.encoder = lwe;
    }

    /**
     * Starts the appender and initializes the encoder if not already set.
     * This method is called by Logback during initialization.
     */
    @Override
    public void start() {
        if (this.encoder == null) {
            // Create a default encoder if none was set
            LayoutWrappingEncoder<ILoggingEvent> lwe = new LayoutWrappingEncoder<>();
            lwe.setContext(context);
            this.encoder = lwe;
        }
        super.start();
    }

    /**
     * Appends a logging event to the cache if the appender is started.
     *
     * @param eventObject the logging event to append
     */
    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!isStarted()) {
            return;
        }

        subAppend(eventObject);
    }


    /**
     * Actual writing occurs here. Encodes the event and adds it to the log cache.
     * <p>
     * Most subclasses of <code>WriterAppender</code> will need to override this
     * method.
     *
     * @param event the logging event to process
     * @since 0.9.0
     */
    protected void subAppend(ILoggingEvent event) {
        if (!enableLogs || !isStarted()) {
            return;
        }
        if (this.encoder == null) {
            return;
        }
        
        try {
            // Prepare the event for deferred processing if needed
            if (event instanceof DeferredProcessingAware) {
                ((DeferredProcessingAware) event).prepareForDeferredProcessing();
            }

            // Encode the event and add it to the cache
            byte[] byteArray = this.encoder.encode(event);
            LOG_CACHE.add(new String(byteArray));
        } catch (Exception e) {
            // Silently ignore encoding errors to avoid infinite loops
        }
    }

    /**
     * Gets the encoder used to format log events.
     *
     * @return the encoder
     */
    public Encoder<ILoggingEvent> getEncoder() {
        return encoder;
    }

    /**
     * Sets the encoder to use for formatting log events.
     *
     * @param encoder the encoder to set
     */
    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    /**
     * Checks if immediate flush is enabled.
     *
     * @return true if immediate flush is enabled, false otherwise
     */
    public boolean isImmediateFlush() {
        return immediateFlush;
    }

    /**
     * Sets whether immediate flush is enabled.
     *
     * @param immediateFlush true to enable immediate flush, false to disable
     */
    public void setImmediateFlush(boolean immediateFlush) {
        this.immediateFlush = immediateFlush;
    }
    
    /**
     * Enables or disables log capture.
     * When disabled, log events will not be added to the cache.
     *
     * @param enableLogs true to enable log capture, false to disable
     */
    public static void setEnableLogs(boolean enableLogs) {
		LogWindowAppender.enableLogs = enableLogs;
	}
}