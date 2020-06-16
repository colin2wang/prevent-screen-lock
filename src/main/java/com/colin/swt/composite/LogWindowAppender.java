package com.colin.swt.composite;

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

public class LogWindowAppender<E> extends UnsynchronizedAppenderBase<E> {
	
	protected static final BlockingDeque<String> LOG_CACHE = new LinkedBlockingDeque<>(500);
	
	private static boolean enableLogs = Constants.ENABLE_LOGS;

    /**
     * It is the encoder which is ultimately responsible for writing the event to
     * an {@link OutputStream}.
     */
    protected Encoder<E> encoder;

    /**
     * All synchronization in this class is done via the lock object.
     */
    protected final ReentrantLock lock = new ReentrantLock(false);

    /**
     * This is the {@link OutputStream outputStream} where output will be written.
     */
    boolean immediateFlush = true;

    public void setLayout(Layout<E> layout) {
        LayoutWrappingEncoder<E> lwe = new LayoutWrappingEncoder<>();
        lwe.setLayout(layout);
        lwe.setContext(context);
        this.encoder = lwe;
    }

    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }

        subAppend(eventObject);
    }


    /**
     * Actual writing occurs here.
     * <p>
     * Most subclasses of <code>WriterAppender</code> will need to override this
     * method.
     * 
     * @since 0.9.0
     */
    protected void subAppend(E event) {
        if (!enableLogs || !isStarted()) {
            return;
        }
        if (event instanceof DeferredProcessingAware) {
            ((DeferredProcessingAware) event).prepareForDeferredProcessing();
        }

        byte[] byteArray = this.encoder.encode(event);
        LOG_CACHE.add(new String(byteArray));
    }

    public Encoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

    public boolean isImmediateFlush() {
        return immediateFlush;
    }

    public void setImmediateFlush(boolean immediateFlush) {
        this.immediateFlush = immediateFlush;
    }
    
    public static void setEnableLogs(boolean enableLogs) {
		LogWindowAppender.enableLogs = enableLogs;
	}
}