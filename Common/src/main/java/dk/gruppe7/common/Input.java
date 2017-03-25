package dk.gruppe7.common;

import dk.gruppe7.common.data.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 *
 * @author Mikkel
 */
public class Input {

    private HashMap<Integer, Pair<Boolean, ArrayList<Callable>>> keys = new HashMap<>();

    public void registerKeyEvent(int keycode, Callable callable) {
        if (keys.containsKey(keycode)) {
            Pair temp = keys.get(keycode);

            if (!((ArrayList<Callable>) temp.getSecond()).contains(callable)) {
                ((ArrayList<Callable>) temp.getSecond()).add(callable);
            }

            keys.replace(keycode, temp);
        } else {
            Pair temp = new Pair();

            ArrayList<Callable> listOfCallables = new ArrayList<>();
            listOfCallables.add(callable);

            temp.setFirst(false);
            temp.setSecond(listOfCallables);

            keys.put(keycode, temp);
        }
    }

    public void unregisterKeyEvent(int keycode, Callable callable) {
        if (keys.containsKey(keycode)) {
            Pair temp = keys.get(keycode);
            ((ArrayList<Callable>) temp.getSecond()).remove(callable);
        }
    }

    public void unregisterKeyEvent(Callable... callable) {
        for (Integer key : keys.keySet()) {
            Pair temp = keys.get(key);

            for (Callable c : callable) {
                ((ArrayList<Callable>) temp.getSecond()).remove(c);
            }
        }
    }

    public void unregisterKeyEvent(int... keycode) {
        for (int kc : keycode) {
            if (keys.containsKey(kc)) {
                Pair temp = keys.get(kc);
                ((ArrayList<Callable>) temp.getSecond()).clear();
            }
        }
    }

    public void setKey(int keycode, boolean newValue) throws Exception {
        if (keys.containsKey(keycode)) {
            if (!keys.get(keycode).getFirst().equals(newValue)) {
                Pair temp = keys.get(keycode);
                temp.setFirst(newValue);
                keys.replace(keycode, temp);

                for (Callable callable : (ArrayList<Callable>) temp.getSecond()) {
                    callable.call();
                }
            }
        } else {
            System.err.printf("Keycode {%d} isn't associated with any events! \n", keycode);
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
            temp.setSecond(new ArrayList<Callable>());
            
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
