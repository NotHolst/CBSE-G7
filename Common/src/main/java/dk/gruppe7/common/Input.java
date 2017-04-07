package dk.gruppe7.common;

import dk.gruppe7.common.data.KeyEventHandler;
import dk.gruppe7.common.data.Pair;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mikkel
 */
public class Input {

    private HashMap<Integer, Pair<Boolean, ArrayList<KeyEventHandler>>> keys = new HashMap<>();

    public void registerKeyEventHandler(int keycode, KeyEventHandler handler) {
        if (keys.containsKey(keycode)) {
            Pair temp = keys.get(keycode);

            if (!((ArrayList<KeyEventHandler>) temp.getSecond()).contains(handler)) {
                ((ArrayList<KeyEventHandler>) temp.getSecond()).add(handler);
            }

            keys.replace(keycode, temp);
        } else {
            Pair temp = new Pair();

            ArrayList<KeyEventHandler> listOfHandlers = new ArrayList<>();
            listOfHandlers.add(handler);

            temp.setFirst(false);
            temp.setSecond(listOfHandlers);

            keys.put(keycode, temp);
        }
    }

    public void unregisterKeyEventHandler(int keycode, KeyEventHandler handler) {
        if (keys.containsKey(keycode)) {
            Pair temp = keys.get(keycode);
            ((ArrayList<KeyEventHandler>) temp.getSecond()).remove(handler);
        }
    }

    public void unregisterKeyEventHandler(KeyEventHandler... handler) {
        for (Integer key : keys.keySet()) {
            Pair temp = keys.get(key);

            for (KeyEventHandler c : handler) {
                ((ArrayList<KeyEventHandler>) temp.getSecond()).remove(c);
            }
        }
    }

    public void unregisterKeyEventHandler(int... keycode) {
        for (int kc : keycode) {
            if (keys.containsKey(kc)) {
                Pair temp = keys.get(kc);
                ((ArrayList<KeyEventHandler>) temp.getSecond()).clear();
            }
        }
    }

    public void setKey(int keycode, boolean newValue) throws Exception {
        if (keys.containsKey(keycode)) {
            if (!keys.get(keycode).getFirst().equals(newValue)) {
                Pair temp = keys.get(keycode);
                temp.setFirst(newValue);
                keys.replace(keycode, temp);

                for (KeyEventHandler handler : (ArrayList<KeyEventHandler>) temp.getSecond()) {
                    handler.call(newValue);
                }
            }
        } else {
            //System.err.printf("Keycode {%d} isn't associated with any events! \n", keycode);
        }
    }

    //Holst vil gerne have noget lignende dette:
    //GetKey        Returns true while the user holds down the key identified by name. Think auto fire.
    //GetKeyDown    Returns true during the frame the user starts pressing down the key identified by name.
    //GetKeyUp      Returns true during the frame the user releases the key identified by name.
    
    @Deprecated
    public void registerKey(int keycode) {
        if(!keys.containsKey(keycode)) {
            Pair temp = new Pair();
            
            temp.setFirst(false);
            temp.setSecond(new ArrayList<KeyEventHandler>());
            
            keys.put(keycode, temp);
        }
    }
    
    @Deprecated
    public boolean getKey(int keycode) {
        if (keys.containsKey(keycode)) {
            return keys.get(keycode).getFirst();
        }

        System.err.printf("Unrecognized keycode : %d \n", keycode);
        return false;
    }

    @Deprecated
    public boolean isUp(int keycode) {
        return getKey(keycode) != true;
    }

    @Deprecated
    public boolean isDown(int keycode) {
        return getKey(keycode) == true;
    }
}
