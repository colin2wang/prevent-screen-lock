package com.colin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Utility class for simulating user actions such as key presses and mouse movements.
 * Uses Java AWT Robot to perform automated input events.
 */
public class UserActionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserActionUtil.class);

    /**
     * Simulates pressing and releasing a single key identified by its key code.
     *
     * @param keycode the key code constant from KeyEvent (e.g., KeyEvent.VK_SCROLL_LOCK)
     */
    public static void pressSingleKeyByNumber(int keycode) {
        LOGGER.info("Fire key event, key code: {}", keycode);
        try {
            Robot robot = new Robot();
            robot.keyPress(keycode);
            robot.keyRelease(keycode);
            robot.delay(500);
        } catch (AWTException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    /**
     * Checks if a locking key (Num Lock, Caps Lock, or Scroll Lock) is currently activated.
     *
     * @param keycode the key code constant from KeyEvent
     * @return true if the key is in locked/activated state, false otherwise
     */
    public static boolean isKeyLocked(int keycode) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        switch (keycode) {
            case KeyEvent.VK_NUM_LOCK:
                return toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK);
            case KeyEvent.VK_CAPS_LOCK:
                return toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
            case KeyEvent.VK_SCROLL_LOCK:
                return toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
            default:
                // For non-locking keys, always return false
                return false;
        }
    }

    /**
     * Moves the mouse cursor to the specified screen coordinates.
     * If coordinates are null, moves to default position (right edge, middle height).
     *
     * @param x the x coordinate on screen, or null for default
     * @param y the y coordinate on screen, or null for default
     */
    public static void mouseMoveToXY(Integer x, Integer y) {
        LOGGER.info("Fire mouse move event, x: {}, y: {}", x, y);
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Robot robot = new Robot();
            if (x == null) {
                x = toolkit.getScreenSize().width;
            }
            if (y == null) {
                y = toolkit.getScreenSize().height / 2;
            }
            robot.mouseMove(x, y);
            robot.delay(100);
        } catch (AWTException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }
}
