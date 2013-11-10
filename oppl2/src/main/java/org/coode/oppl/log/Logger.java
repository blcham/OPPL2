package org.coode.oppl.log;

import java.util.logging.Level;

import org.coode.oppl.ConstraintSystem;
import org.coode.oppl.Renderable;

public class Logger {
    java.util.logging.Logger logger;

    public Logger(String className) {
        logger = java.util.logging.Logger.getLogger(className);
    }

    public void log(String message) {
        logger.log(Level.WARNING, message);
    }

    public void info(String message) {
        logger.log(Level.INFO, message);
    }

    public void fine(String message) {
        logger.log(Level.FINE, message);
    }

    public void log(String message, Object o) {
        logger.log(Level.WARNING, message, o);
    }

    public void info(String message, Object o) {
        logger.log(Level.INFO, message, o);
    }

    public void info(Object o) {
        logger.log(Level.INFO, "", o);
    }

    public void fine(String message, Object o) {
        logger.log(Level.FINE, message, o);
    }

    public void log(String message, Renderable c, ConstraintSystem cv, Object o) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.log(Level.WARNING, message + c.render(cv), o);
        }
    }

    public void info(String message, Renderable c, ConstraintSystem cv, Object o) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, message + c.render(cv), o);
        }
    }

    public void info(Renderable c, ConstraintSystem cv) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, c.render(cv));
        }
    }

    public void fine(String message, Object... o) {
        logger.log(Level.FINE, message, o);
    }
}
