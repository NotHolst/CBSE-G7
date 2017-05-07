package dk.gruppe7.common;

import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.eventtypes.KeyReleasedEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.openide.util.Exceptions;

/**
 *
 * @author Mikkel
 */
public class GameInputProcessor {

    private GameData gameData;

    private NativeKeyListener gameKeyListener = new NativeKeyListener() {

        @Override
        public void nativeKeyTyped(NativeKeyEvent nke) {
            // System.err.println("Not supported!");
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nke) {
            try {
                //System.out.printf("KeyPress caught : {%d} \n", nke.getRawCode());
                Dispatcher.post(new KeyPressedEvent(nke.getKeyCode()), null);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nke) {
            try {
                //System.out.printf("KeyRelease caught : {%d} \n", nke.getRawCode());
                Dispatcher.post(new KeyReleasedEvent(nke.getKeyCode()), null);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    };

    public GameInputProcessor(GameData gameData) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        //logger.setLevel(Level.WARNING);
        logger.setLevel(Level.OFF);
        
        this.gameData = gameData;

        try {
            GlobalScreen.unregisterNativeHook();
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            System.err.println("Failed to register native hook!");
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    public void start() {
        GlobalScreen.addNativeKeyListener(gameKeyListener);
    }

    public void stop() {
        GlobalScreen.removeNativeKeyListener(gameKeyListener);
    }
}