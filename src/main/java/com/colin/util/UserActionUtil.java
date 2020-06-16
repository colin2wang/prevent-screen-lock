package com.colin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class UserActionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserActionUtil.class);

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
