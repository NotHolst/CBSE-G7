package dk.sdu.gruppe7.projekt;

import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import java.io.IOException;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static junit.framework.TestCase.assertEquals;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Lookup;

public class ApplicationTest extends NbTestCase {
    
    // Add your own path.
    private static final String COMMONS_ONLY = "CBSE-G7/application/src/test/resources/xmledit/updates_commons.xml";
    private static final String COMMONS_WITH_MOB = "CBSE-G7/application/src/test/resources/xmledit/updates_commons_mob.xml";
    private static final String ALL = "CBSE-G7/application/src/test/resources/xmledit/updates_all";
    private static final String CURRENT_UPDATES_FILE = "ligger i CBSE-G7/application/src/test/resources/xmledit/updates_all";

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
        //Loading only commons
        copy(get(COMMONS_ONLY), get(CURRENT_UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        // Assert
        // The amount of IProcess in the lookup should be 0 
        // The amoount of IRender in the lookup should be 0 
        assertEquals("0 processors", 0, processors.size());
        assertEquals("0 renders", 0, renders.size());
        
        //Loading Mob alone
        copy(get(COMMONS_WITH_MOB), get(CURRENT_UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        // Assert
        // The amount of IProcess in the lookup should be 1 
        // The amoount of IRender in the lookup should be 1 
        assertEquals("1 processors", 1, processors.size());
        assertEquals("1 renders", 1, renders.size());
        
        //Unloading Mob
        copy(get(COMMONS_ONLY), get(CURRENT_UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        // Assert
        // The amount of IProcess in the lookup should be 0 
        // The amoount of IRender in the lookup should be 0 
        assertEquals("0 processors", 0, processors.size());
        assertEquals("0 renders", 0, renders.size());
        
        
    }
    
    private void waitForUpdate(List<IProcess> processors, List<IRender> renders) throws InterruptedException {
        // Needs time for SilentUpdate to install all modules.
        Thread.sleep(8000);
        
        processors.clear();
        renders.clear();
        
        processors.addAll(Lookup.getDefault().lookupAll(IProcess.class));
        renders.addAll(Lookup.getDefault().lookupAll(IRender.class));
    }

}
