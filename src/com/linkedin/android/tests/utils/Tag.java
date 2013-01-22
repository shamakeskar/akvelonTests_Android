package com.linkedin.android.tests.utils;

import java.util.ArrayList;

import junit.framework.Assert;

import com.linkedin.android.tests.data.DataProvider;


/**
 * Class with info about test (tag).
 * 
 * @author alexander.makarov
 * @created Dec 27, 2012 4:45:26 PM
 */
public class Tag {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    // Name of tag.
    private String name = "undefined_name";
    // Id of tag.
    private String id = "undefined_id";
    // Current action index.
    private int currentAction = 0;
    // ArrayList with actions and payloads.
    private ArrayList<RegistryKey> actions;

    // CONSTRUCTORS ---------------------------------------------------------
    public Tag(String name, String[] actions, String[] payloads) {
        this.name = name;
        Assert.assertTrue("Wrong parameters for Tag constructor: actions.length != payloads.length", actions.length == payloads.length);
        this.actions = new ArrayList<RegistryKey>();
        for (int i = 0; i < actions.length; i++) {
            this.actions.add(new RegistryKey(actions[i], payloads[i]));
        }
    }
    
    public Tag(String name, ArrayList<RegistryKey> actions) {
        this.name = name;
        this.actions = actions;
    }
    
    public Tag(String name, String id, ArrayList<RegistryKey> actions) {
        this.name = name;
        this.id = id;
        this.actions = actions;
    }

    // METHODS --------------------------------------------------------------
    /**
     * Runs next method in <b>actions</b>. Increments <b>currentAction</b>.
     * @throws Throwable
     */
    public void processNextAction() throws Throwable {
        if (currentAction != actions.size()) {
            DataProvider.getInstance().getRegistry().processAction(actions.get(currentAction++));
        }
    }
    
    /**
     * Return name of next action.
     * 
     * @return name of next action.
     */
    public String getNextAction() {
        if (currentAction > actions.size()) {
            return null;
        }
        return actions.get(currentAction).getKey();
    }
    
    /**
     * Return payload for current action.
     * 
     * @return payload for current action.
     */
    public Object[] getPayloadsForCurrentAction() {
        return actions.get(currentAction).getPayload();
    }
    
    /**
     * Returns count of actions in Tag.
     * 
     * @return count of actions.
     */
    public int countOfActions() {
        return actions.size();
    }
    
    /**
     * Returns name of tag.
     * 
     * @return name of tag.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns group of tag.
     * 
     * @return group of tag.
     */
    public String getId() {
        return id;
    }
}
