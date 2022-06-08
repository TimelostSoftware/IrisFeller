package com.timelost.irisfeller.util;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggerUtils {

    public static void log(Logger logger, Level logLevel, String s, Object... args) {
        logger.log(logLevel, args.length > 0 ? String.format(s, args) : s);
    }

    public static void warning(Logger logger, String s, Object... args) {
        log(logger, Level.WARNING, s, args);
    }

    public static void error(Logger logger, String s, Object... args) {
        log(logger, Level.SEVERE, s, args);
    }

    public static void logException(Logger logger, boolean isSevere, Throwable e, int indents) {
        StringBuilder format = new StringBuilder("%s%s");
        for(int i = 0; i < indents; i++)
            format.insert(0, "\t");
        if(isSevere)
            error(logger, format.toString(), e.getClass().getSimpleName(), e.getMessage() != null ? " - " + e.getMessage() : "");
        else
            warning(logger, format.toString(), e.getClass().getSimpleName(), e.getMessage() != null ? " - " + e.getMessage() : "");
    }

    public static void logExceptionStack(Logger logger, boolean isSevere, Throwable e, String s, Object... args) {
        log(logger, isSevere ? Level.SEVERE : Level.WARNING, s, args);
        int indent = 1;
        Throwable throwable = e;
        while(throwable != null) {
            logException(logger, isSevere, throwable, indent++);
            throwable = throwable.getCause();
        }
    }

    public static void logStringStack(Logger logger, boolean isSevere, List<String> strings, String s, Object... args) {
        log(logger, isSevere ? Level.SEVERE : Level.WARNING, s, args);
        StringBuilder builder = new StringBuilder("\t");
        for(String string : strings) {
            if(isSevere)
                error(logger, builder + string);
            else
                warning(logger, builder + string);
            builder.append("\t");
        }
    }
}
