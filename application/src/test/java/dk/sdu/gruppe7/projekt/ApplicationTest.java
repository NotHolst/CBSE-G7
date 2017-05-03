package dk.sdu.gruppe7.projekt;

import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import java.io.IOException;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Lookup;

public class ApplicationTest extends NbTestCase {
    
    // Add your own path.
    private static final String ADD_ALL_MODULES = "PATH/updates_all.xml";
    private static final String REMOVE_MOB_UPDATES_FILE = "PATH/updates_noMob.xml";
    private static final String UPDATES_FILE = "PATH/updates.xml";

    public static Test suite() {
        return NbModuleSuite.createConfiguration(ApplicationTest.class).
                gui(false).
                failOnMessage(Level.WARNING). // works at least in RELEASE71
                failOnException(Level.INFO).
                enableClasspathModules(false). 
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public ApplicationTest(String n) {
        super(n);
    }

    public void testApplication() throws InterruptedException, IOException {
        // pass if there are merely no warnings/exceptions
        /* Example of using Jelly Tools (additional test dependencies required) with gui(true):
        new ActionNoBlock("Help|About", null).performMenu();
        new NbDialogOperator("About").closeByButton();
         */
        
        // Setup
        List<IProcess> processors = new CopyOnWriteArrayList<>();
        List<IRender> renders = new CopyOnWriteArrayList<>();
        waitForUpdate(processors, renders);
        
        // Pre asserts
        // Size should be 0 because no modules installed
        assertEquals("No processors", 0, processors.size());
        assertEquals("No renders", 0, renders.size());
        
        // Test
        // Load all modules via Update Center
        copy(get(ADD_ALL_MODULES), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        // Assert
        // The amount of IProcess in the lookup should be 13 when all the modules are loaded.
        // The amoount of IRender in the lookup should be 11 when all the modules are loaded.
        assertEquals("13 processors", 13, processors.size());
        assertEquals("11 renders", 11, renders.size());
        
        // Test
        // Unload Mob module via Update Center
        copy(get(REMOVE_MOB_UPDATES_FILE), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        // Assert
        // The amount of IProcess and IRender should now be 12 and 10, because the Mob module has been unloaded.
        assertEquals("12 processors", 12, processors.size());
        assertEquals("10 renders", 10, renders.size());
        
        // Test
        // Load all modules via Update Center
        copy(get(ADD_ALL_MODULES), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        // Assert
        // The amount of IProcess in the lookup should be 13 when all the modules are loaded.
        // The amoount of IRender in the lookup should be 11 when all the modules are loaded.
        assertEquals("No processors", 0, processors.size());
        assertEquals("No renders", 0, renders.size());
        
    }
    
    private void waitForUpdate(List<IProcess> processors, List<IRender> renders) throws InterruptedException {
        // Needs time for SilentUpdate to install all modules.
        Thread.sleep(10000);
        
        processors.clear();
        renders.clear();
        
        processors.addAll(Lookup.getDefault().lookupAll(IProcess.class));
        renders.addAll(Lookup.getDefault().lookupAll(IRender.class));
    }

}
