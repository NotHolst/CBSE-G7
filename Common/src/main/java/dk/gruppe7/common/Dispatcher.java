package dk.gruppe7.common;

import dk.gruppe7.common.data.ActionEventHandler;
import dk.gruppe7.common.data.KeyEventHandler;
import dk.gruppe7.common.data.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Dispatcher {
    // Maps all ActionEventHandlers associated with every registered event superclass.
    private static HashMap<Integer, ArrayList<Object>> mapOfEventHandlerObjects = new HashMap<>();
    
    // Maps links between anonymous event classes and event superclasses.
    private static HashMap<Integer, Integer> mapOfEventLinks = new HashMap<>();
    
    // Maps KeyPress event handler fields to their respective virtual keycodes. 
    private static HashMap<Integer, KeyEventHandler> mapOfCachedKeyPressedHandlers = new HashMap<>();
    
    // Maps KeyRelease event handler fields to their respective virtual keycodes. 
    private static HashMap<Integer, KeyEventHandler> mapOfCachedKeyReleasedHandlers = new HashMap<>();
    
    // Triggers all handlers registered to the event type being posted and hands off the event object to the handlers themselves.
    public static void post(Object event, World world) {
        if(!isEventValid(event)) {
            return;
        }
        
        addEventTypeIfUnknown(event);
        
        ArrayList<Object> tempListOfHandlers = (mapOfEventHandlerObjects.containsKey(event.getClass().hashCode())) ?
                mapOfEventHandlerObjects.get(event.getClass().hashCode()) :
                mapOfEventHandlerObjects.get(mapOfEventLinks.get(event.getClass().hashCode()));
        
        tempListOfHandlers.forEach((handler) -> {
            if(handler instanceof ActionEventHandler) {
                ((ActionEventHandler)handler).call(event, world);
            } else if(handler instanceof KeyEventHandler) {
                int identifier = Objects.hash(handler, (event instanceof KeyPressedEvent) ? ((KeyPressedEvent)event).getVirtualKeyCode() : ((KeyReleasedEvent)event).getVirtualKeyCode());
                
                if(mapOfCachedKeyPressedHandlers.containsKey(identifier) || mapOfCachedKeyReleasedHandlers.containsKey(identifier)) {
                    mapOfCachedKeyPressedHandlers.getOrDefault(identifier, mapOfCachedKeyReleasedHandlers.get(identifier)).call(event);
                    return;
                }
                
                int handlerKeyCode = getFieldValue("virtualKeyCode", (KeyEventHandler) handler, KeyEventHandler.class);
                
                if(event instanceof KeyPressedEvent) {
                    mapOfCachedKeyPressedHandlers.put(Objects.hash(handler, handlerKeyCode), (KeyEventHandler) handler);
                } else if (event instanceof KeyReleasedEvent) {
                    mapOfCachedKeyPressedHandlers.put(Objects.hash(handler, handlerKeyCode), (KeyEventHandler) handler);
                }
                
                if(mapOfCachedKeyPressedHandlers.containsKey(identifier) || mapOfCachedKeyReleasedHandlers.containsKey(identifier)) {
                    mapOfCachedKeyPressedHandlers.getOrDefault(identifier, mapOfCachedKeyReleasedHandlers.get(identifier)).call(event);
                }
            }
        });
    }
    
    // Discovers and registers all handlers declared in object.
    public static void subscribe(Object subscriber) {
        getAllHandlers(subscriber).forEach(pair -> subscribe(pair.getFirst(), pair.getSecond()));
    }
    
    // Registers a new handler with a event type.
    public static void subscribe(Class<?> klass , Object handler) {
        if(!isEventValid(klass)) {
            return;
        }
        
        addEventTypeIfUnknown(klass);
        
        mapOfEventHandlerObjects.get(klass.hashCode()).add(handler);
    }
    
    // Discovers and unregisters all handlers declared in object.
    public static void unsubscribe(Object subscriber) {
        getAllHandlers(subscriber).forEach(pair -> unsubscribe(pair.getFirst(), pair.getSecond()));
    }
    
    // Unregisters a specific handler from a event type.
    public static void unsubscribe(Class<?> klass, Object handler) {
        if(!isEventValid(klass)) {
            return;
        }
        
        addEventTypeIfUnknown(klass);
        
        mapOfEventHandlerObjects.get(klass.hashCode()).remove(handler);
    }
    
    // Checks whether a event handler is annotated as an event handler and throws IllegalArgumentException in case it isn't.
    private static boolean isHandlerValid(Field handler) {
        if(handler.getType().isPrimitive()) {
            return false;
        }
        
        if(!handler.getType().isAnnotationPresent(EventHandler.class)) {
            System.err.println(handler.getType() + " -> Handler object that was passed is not a valid event handler. Is the event handler class annotated as an actual event handler?");
            return false;
        }
        
        return true;
    }
    
    // Checks whether a event object is annotated as an event and throws IllegalArgumentException in case it isn't.
    private static boolean isEventValid(Object event) {
        if(!event.getClass().isAnonymousClass()) {
            if(!event.getClass().isAnnotationPresent(Event.class)) {
                System.err.println("Event object that was passed is not a valid event. Is the event class annotated as an actual event?");
                return false;
            }
        } else {
            if(!event.getClass().getSuperclass().isAnnotationPresent(Event.class)) {                
                System.err.println("Event object that was passed is not a valid event. Is the event class annotated as an actual event?");
                return false;
            }
        }
        
        return true;
    }
    
    // Checks whether a event class is annotated as an event and throws IllegalArgumentException in case it isn't.
    private static boolean isEventValid(Class<?> klass) {
        if(!klass.isAnnotationPresent(Event.class)) {
            System.err.println("Event class that was passed is not a valid event. Is the event class annotated as an actual event?");
            return false;
        }
        
        return true;
    }
    
    // Registers new event types based on the event objects that pass through the dispatcher.
    // It also links anonymous events created w/ double braced initialization with their respective superclasses.
    private static void addEventTypeIfUnknown(Object event) {         
        int hashcodeSimple = event.getClass().hashCode();
        
        if(mapOfEventHandlerObjects.containsKey(hashcodeSimple)) {
            return;
        }
        
        int hashcodeSuper = event.getClass().getSuperclass().hashCode();
        
        if(mapOfEventLinks.containsKey(hashcodeSimple) && mapOfEventHandlerObjects.containsKey(hashcodeSuper)) {
            return;
        }
    
        if(event.getClass().isAnonymousClass()) {
            mapOfEventHandlerObjects.putIfAbsent(hashcodeSuper, new ArrayList<>());
            mapOfEventLinks.put(hashcodeSimple, hashcodeSuper);
        } else {
            mapOfEventHandlerObjects.putIfAbsent(hashcodeSimple, new ArrayList<>());
        }
    }
    
    // Registers new event types based on the event classes that pass through the dispatcher.
    private static void addEventTypeIfUnknown(Class<?> klass) {
        int hashcodeSimple = klass.hashCode();
        
        if(mapOfEventHandlerObjects.containsKey(klass.hashCode())) {
            return;
        }
        
        mapOfEventHandlerObjects.putIfAbsent(hashcodeSimple, new ArrayList<>());
    }
    
    // Finds and returns a value from a field in given object through reflection.
    public static <T> T getFieldValue(String fieldName, Object instance, Class baseClass) {
        Field reflectedField = null;
        boolean originalAccessFlag = false;
        Object returnValue = null;
        
        try {
            reflectedField = baseClass.getDeclaredField(fieldName);
            originalAccessFlag = reflectedField.isAccessible();
            
            reflectedField.setAccessible(true);

            returnValue = reflectedField.get(instance);
            
            reflectedField.setAccessible(originalAccessFlag);
        } catch (IllegalAccessException | NoSuchFieldException | ClassCastException e) {
            System.err.printf("%s was thrown while reflectively resolving for handlers declared by subscriber! -> %s \n", e.getClass().getSimpleName(), instance.getClass().getSimpleName());
        }
        
        return (T) returnValue;
    };
    
    // Gets all handlers declared in object passed as the argument through reflection.
    private static ArrayList<Pair<Class, ActionEventHandler>> getAllHandlers(Object subscriber) {
        ArrayList<Pair<Class, ActionEventHandler>> listOfHandlers = new ArrayList<>();
        
        for(Field field : subscriber.getClass().getDeclaredFields()) {
            if(isHandlerValid(field)) { 
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Class klass = ((Class)parameterizedType.getActualTypeArguments()[0]);
                
                Object event = getFieldValue(field.getName(), subscriber, subscriber.getClass());
                
                listOfHandlers.add(new Pair(klass, event));
            }
        }
        
        return listOfHandlers;
    }
}
