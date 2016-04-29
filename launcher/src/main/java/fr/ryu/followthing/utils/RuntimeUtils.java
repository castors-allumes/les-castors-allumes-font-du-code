package fr.ryu.followthing.utils;

import org.apache.log4j.Logger;
import org.fest.util.Strings;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.fest.util.Strings.isEmpty;

/**
 * Created by Ryuko on 21/12/2014.
 */
public class RuntimeUtils {
    private RuntimeUtils() {
    }

    private static final Logger LOG = Logger.getLogger(RuntimeUtils.class);

    public static <E extends RuntimeException> void isNotNull(Object o, Class<E> classEx, String message) {
        if (isNull(o)) {
            throwRuntimeException(classEx, message);
        }
    }

    public static void isNotNull(Object o) {
        if (isNull(o)) {
            throwRuntimeException("Object is null");
        }
    }

    public static <E extends RuntimeException> void isNotEmpty(final String s, Class<E> classEx, String message) {
        if (isEmpty(s)) {
            throwRuntimeException(classEx, message);
        }
    }

    public static void isNotEmpty(final String s) {
        if (isEmpty(s)) {
            throwRuntimeException("String is null or empty");
        }
    }

    protected static void throwRuntimeException(String message) {
        throw new RuntimeException(message);
    }

    protected static <E extends RuntimeException> void throwRuntimeException(Class<E> classEx, String message) {
        isNotNull(classEx, RuntimeException.class, "Exception class must be specified");

        try {
            throw classEx.getConstructor(String.class).newInstance(message);
        } catch (InstantiationException e) {
            LOG.error(e);
        } catch (IllegalAccessException e) {
            LOG.error(e);
        } catch (InvocationTargetException e) {
            LOG.error(e);
        } catch (NoSuchMethodException e) {
            LOG.error(e);
        }
        throw new RuntimeException(format("Fail to throw exception of instance %s with message", classEx.getName(), message));
    }


}
