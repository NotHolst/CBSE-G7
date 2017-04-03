package dk.gruppe7.core;

import dk.gruppe7.common.GameData;
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
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nke) {
            try {
                //System.out.printf("KeyPress caught : {%d} \n", nke.getRawCode());
                gameData.getInput().setKey(nke.getRawCode(), true);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nke) {
            try {
                //System.out.printf("KeyRelease caught : {%d} \n", nke.getRawCode());
                gameData.getInput().setKey(nke.getRawCode(), false);
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