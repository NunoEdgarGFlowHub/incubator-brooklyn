package brooklyn.entity.dns.geoscaling

import static org.testng.AssertJUnit.*

import java.util.LinkedHashSet
import java.util.Set

import org.testng.annotations.Test

import brooklyn.entity.dns.HostGeoInfo


/**
 * {@link GeoscalingScriptGenerator} unit tests.
 */
class GeoscalingScriptGeneratorTest {
    
    private final static Set<HostGeoInfo> HOSTS = new LinkedHashSet<HostGeoInfo>();
    static {
        HOSTS.add(new HostGeoInfo("1.2.3.100", "Server 1", 40.0, -80.0));
        HOSTS.add(new HostGeoInfo("1.2.3.101", "Server 2", 30.0, 20.0));
    }
    
    
    @Test
    public void testScriptGeneration() {
        Date generationTime = new Date(0);
        String generatedScript = GeoscalingScriptGenerator.generateScriptString(generationTime, HOSTS);
        String expectedScript = GeoscalingScriptGenerator.loadResource("/brooklyn/entity/dns/geoscaling/expectedScript.php");
        assertEquals(expectedScript, generatedScript);
    }
    
}
