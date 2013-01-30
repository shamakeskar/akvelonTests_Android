package com.linkedin.android.tests.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linkedin.android.utils.Logger;

/**
 * Class for load and store {@code Map} of {@code Action}.
 * 
 * @author alexander.makarov
 * @created Jan 17, 2013 8:37:47 PM
 */
public class Registry {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    private final Map<String, Action> actions = new ConcurrentHashMap<String, Action>();

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Returns {@code Map} actions.
     * 
     * @return {@code Map} actions.
     */
    public Map<String, Action> getActions() {
        return actions;
    }

    /**
     * Add specified method to <b>actions</b>.
     * 
     * @param method
     *            Method to invoke from the {@code Action}.
     */
    private void register(final Method method) {
        final String methodName = method.getName() + "()";
        final int parametersCount = method.getParameterTypes().length;

        final TestAction testAction = method.getAnnotation(TestAction.class);

        final Action action = new Action() {
            StringBuilder represent = new StringBuilder("{\"").append(testAction.value())
                    .append("\": ").append(method.getDeclaringClass().getSimpleName()).append('#')
                    .append(method.getName()).append("()}");

            @Override
            public void testAction(Object... payload) throws Throwable {
                // Log that action started.
                StringBuilder log = new StringBuilder(">>> Run action '").append(testAction.value());
                if (payload != null) {
                    String payloadString = Arrays.toString(payload);
                    payloadString = payloadString.substring(1, payloadString.length() - 1);
                    log.append("' with payload '").append(payloadString);
                }
                log.append("':");
                Logger.d(log.toString());
                // Try invoke method with action.
                try {
                    method.invoke(null, (parametersCount == 0 ? null : payload));
                } catch (IllegalArgumentException exc) {
                    final String msg = methodName + " cannot take String parameter(s): " + exc;
                    IllegalStateException e = new IllegalStateException(msg);
                    Logger.e(msg, e);
                    throw e;
                } catch (IllegalAccessException exc) {
                    final String msg = methodName
                            + " is not accessible or wrong number of parameters: " + exc;
                    IllegalStateException e = new IllegalStateException(msg);
                    Logger.e(msg, e);
                    throw e;
                } catch (InvocationTargetException exc) {
                    Throwable e = exc.getCause();
                    if (e == null) {
                        e = exc;
                    }
                    Logger.e("", e);
                    throw e;
                }
            }

            @Override
            public String toString() {
                return represent.toString();
            }
        };

        Action previous = actions.put(testAction.value(), action);
        if (previous != null) {
            throw new IllegalArgumentException(action + "\nalready assigned: " + previous);
        }
    }

    /**
     * Find in specified class all methods with {@code TestAction} annotation
     * and add it to <b>actions</b>.
     * 
     * @param classObject
     *            Class with actions.
     */
    public void register(final Class<? extends Object> classObject) {
        String actionNames = "";
        try {
            for (final Method method : classObject.getMethods()) {
                if (method.isAnnotationPresent(TestAction.class)) {
                    register(method);
                    actionNames += method.getName() + ", ";
                }
            }
        } catch (Exception e) {
            Logger.e("Cannot to register class '" + classObject.getSimpleName() + "'", e);
        }
        Logger.v("Registered class '" + classObject.getSimpleName() + "' with actions: "
                + actionNames);
    }

    /**
     * Process {@code Action} by specified {@code RegistryKey}.
     * 
     * @param key
     *            RegistryKey of the action in scenario.
     * @throws Throwable
     * @throws IllegalStateException
     *             if there is a programming error such as.
     */
    public void processAction(RegistryKey key) throws Throwable {
        if (key == null) {
            throw new IllegalStateException("Name of action for run is empty!");
        }
        String actionName = key.getKey();
        if (actionName != null && actionName.length() > 0) {
            Action action = actions.get(actionName);
            if (action == null) {
                Exception exc = new Exception("Action \"" + actionName
                        + "\" has not been registered.");
                Logger.e("", exc);
                throw exc;
            }
            action.testAction(key.getPayload());
        }
    }

    /**
     * Process {@code Action}s from list of {@code RegistryKey}s.
     * 
     * @param keys
     *            List {@code <RegistryKey>} of action keys in scenario.
     * @throws Throwable
     * @throws IllegalStateException
     *             if there is a programming error.
     */
    public void processActions(List<RegistryKey> keys) throws Throwable {
        if (keys == null) {
            throw new IllegalStateException("List actions for run is empty!");
        }
        assert keys != null : "No IllegalStateException thrown";
        for (final RegistryKey key : keys) {
            processAction(key);
        }
    }

    /**
     * Process {@code Action}s by specified {@code RegistryKey}s.
     * 
     * @param names
     *            RegistryKey of action names in scenario.
     * @throws Throwable
     * @throws IllegalStateException
     *             if there is a programming error.
     */
    public void processActions(RegistryKey... names) throws Throwable {
        processActions(Arrays.asList(names));
    }
}
