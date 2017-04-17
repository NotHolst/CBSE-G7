package dk.gruppe7.common;

import dk.gruppe7.common.data.ActionEventHandler;
import dk.gruppe7.common.data.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;

public class Dispatcher {
    /**
     * Maps all ActionEventHandlers associated with every registered event superclass.
     */
    private static HashMap<Integer, ArrayList<ActionEventHandler>> mapOfEventHandlers = new HashMap<>();
    
    /**
     * Maps links between anonymous event classes and event superclasses.
     */
    private static HashMap<Integer, Integer> mapOfEventLinks = new HashMap<>();
    
    /**
     * Triggers all handlers registered to the event type being posted and hands off the event object to the handlers themselves.
     */
    public static void post(Object event, World world) {
        isEventValid(event);
        addEventTypeIfUnknown(event);
        
        int hashcode = event.getClass().hashCode();
        if(mapOfEventHandlers.containsKey(hashcode)) {
            mapOfEventHandlers.get(hashcode).forEach(handler -> handler.call(event, world));
        } else {
            mapOfEventHandlers.get(mapOfEventLinks.get(hashcode)).forEach(handler -> handler.call(event, world));
        }
    }
    
    /**
     * Discovers and registers all handlers declared in object.
     */
    public static void subscribe(Object subscriber) {
        getAllHandlers(subscriber).forEach(pair -> subscribe(pair.getFirst(), pair.getSecond()));
    }
    
    /**
     * Registers a new handler with a event type.
     */
    public static void subscribe(Class<?> klass , ActionEventHandler handler) {
        isEventValid(klass);
        addEventTypeIfUnknown(klass);
        
        mapOfEventHandlers.get(klass.hashCode()).add(handler);
    }
    
    /**
     * Discovers and unregisters all handlers declared in object.
     */
    public static void unsubscribe(Object subscriber) {
        getAllHandlers(subscriber).forEach(pair -> unsubscribe(pair.getFirst(), pair.getSecond()));
    }
    
    /**
     * Unregisters a specific handler from a event type.
     */
    public static void unsubscribe(Class<?> klass, ActionEventHandler handler) {
        isEventValid(klass);
        addEventTypeIfUnknown(klass);
        
        mapOfEventHandlers.get(klass.hashCode()).remove(handler);
    }
    
    /**
     * Checks whether a event object is annotated as an event and throws IllegalArgumentException in case it isn't.
     */
    private static void isEventValid(Object event) {
        if(!event.getClass().isAnonymousClass()) {
            if(!event.getClass().isAnnotationPresent(Event.class)) {
                throw new IllegalArgumentException("Event object that was passed is not a valid event. Is the event class annotated as an actual event?");
            }
        } else {
            if(!event.getClass().getSuperclass().isAnnotationPresent(Event.class)) {                
                throw new IllegalArgumentException("Event object that was passed is not a valid event. Is the event class annotated as an actual event?");
            }
        }
    }
    
    /**
     * Checks whether a event class is annotated as an event and throws IllegalArgumentException in case it isn't.
     */
    private static void isEventValid(Class<?> klass) {
        if(!klass.isAnnotationPresent(Event.class)) {
            throw new IllegalArgumentException("Event object that was passed is not a valid event. Is the event class annotated as an actual event?");
        }
    }
    
    /**
     * Registers new event types based on the event objects that pass through the dispatcher.
     * It also links anonymous events created w/ double braced initialization with their respective superclasses.
     */
    private static void addEventTypeIfUnknown(Object event) {         
        int hashcodeSimple = event.getClass().hashCode();
        
        if(mapOfEventHandlers.containsKey(hashcodeSimple)) {
            return;
        }
        
        int hashcodeSuper = event.getClass().getSuperclass().hashCode();
        
        if(mapOfEventLinks.containsKey(hashcodeSimple) && mapOfEventHandlers.containsKey(hashcodeSuper)) {
            return;
        }
    
        if(event.getClass().isAnonymousClass()) {
            mapOfEventHandlers.putIfAbsent(hashcodeSuper, new ArrayList<>());
            mapOfEventLinks.put(hashcodeSimple, hashcodeSuper);
        } else {
            mapOfEventHandlers.putIfAbsent(hashcodeSimple, new ArrayList<>());
        }
    }
    
    /**
     * Registers new event types based on the event classes that pass through the dispatcher.
     */
    private static void addEventTypeIfUnknown(Class<?> klass) {
        int hashcodeSimple = klass.hashCode();
        
        if(mapOfEventHandlers.containsKey(klass.hashCode())) {
            return;
        }
        
        mapOfEventHandlers.putIfAbsent(hashcodeSimple, new ArrayList<>());
    }
    
    /**
     * Gets all handlers declared in object passed as the argument through reflection.
     */
    private static ArrayList<Pair<Class, ActionEventHandler>> getAllHandlers(Object subscriber) {
        ArrayList<Pair<Class, ActionEventHandler>> listOfHandlers = new ArrayList<>();
        
        try {
            for(Field field : subscriber.getClass().getDeclaredFields()) {
                if(field.getType() == ActionEventHandler.class) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Class klass = ((Class)parameterizedType.getActualTypeArguments()[0]);
                    
                    Field reflectedField = subscriber.getClass().getDeclaredField(field.getName());
                    reflectedField.setAccessible(true);
                    
                    ActionEventHandler event = (ActionEventHandler) reflectedField.get(subscriber);
                    
                    reflectedField.setAccessible(false);
                    
                    listOfHandlers.add(new Pair(klass, event));
                }
            }
            
        } catch (IllegalAccessException | NoSuchFieldException | ClassCastException e) {
            System.err.printf("%s was thrown while reflectively resolving for handlers declared by subscriber! -> %s \n", e.getClass().getSimpleName(), subscriber.getClass().getSimpleName());
        }
        
        return listOfHandlers;
    }
}
