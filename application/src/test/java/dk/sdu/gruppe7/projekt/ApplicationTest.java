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
    private static final String ALL_MODULES_FILE = "/Users/benjaminmlynek/CBSE-G7/application/src/test/resources/updates_w_all.xml";
    private static final String NO_MODULES_FILE = "/Users/benjaminmlynek/CBSE-G7/application/src/test/resources/updates_w_none.xml";
    private static final String MOB_MODULE_FILE = "/Users/benjaminmlynek/CBSE-G7/application/src/test/resources/updates_w_mob.xml";
    private static final String UPDATES_XML_LOCATION = "/Users/benjaminmlynek/CBSE-G7/application/src/test/resources/updates.xml";

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

    public void testLoadingAndUnloading() throws InterruptedException, IOException {
        // pass if there are merely no warnings/exceptions
        /* Example of using Jelly Tools (additional test dependencies required) with gui(true):
        new ActionNoBlock("Help|About", null).performMenu();
        new NbDialogOperator("About").closeByButton();
         */
        
        List<IProcess> processors = new CopyOnWriteArrayList<>();
        List<IRender> renders = new CopyOnWriteArrayList<>();
        
        copy(get(NO_MODULES_FILE), get(UPDATES_XML_LOCATION), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        assertEquals(0, processors.size());
        assertEquals(0, renders.size());
        
        copy(get(MOB_MODULE_FILE), get(UPDATES_XML_LOCATION), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        assertEquals(1, processors.size());
        assertEquals(1, renders.size());
        
        copy(get(ALL_MODULES_FILE), get(UPDATES_XML_LOCATION), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        assertEquals(13, processors.size());
        assertEquals(11, renders.size());
        
        copy(get(MOB_MODULE_FILE), get(UPDATES_XML_LOCATION), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        assertEquals(1, processors.size());
        assertEquals(1, renders.size());
        
        copy(get(NO_MODULES_FILE), get(UPDATES_XML_LOCATION), REPLACE_EXISTING);
        waitForUpdate(processors, renders);
        
        assertEquals(0, processors.size());
        assertEquals(0, renders.size());
        
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
