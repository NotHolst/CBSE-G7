/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.AI;

import java.util.concurrent.Callable;


public class State {
    private String name;
    private Runnable processor;
    
    public State(String name, Runnable processor){
        this.name = name;
        this.processor = processor;
    }

    public String getName() {
        return name;
    }

    public Runnable getProcessor() {
        return processor;
    }
    
    
}
