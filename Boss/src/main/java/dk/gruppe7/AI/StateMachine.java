/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.AI;

public class StateMachine {
    State currentState;
    
    public StateMachine(){
        
    }
    
    public State getState(){
        return currentState;
    }
    
    public void setState(State state){
        this.currentState = state;
    }
    
    public void process(){
        currentState.getProcessor().run();
    }
    
}
