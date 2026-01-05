import com.cybershrek.tools.HandyResources;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class HandyResourcesTest {

    @Test
    void testText() {
        String text = HandyResources.loadText("test.txt");
        System.out.println(text);
        assertFalse(text.isEmpty());
    }

    @Test
    void testProperties() {
        Properties properties = HandyResources.loadProperties("test.properties");
        assertEquals(2, properties.size());
        assertEquals("Hello", properties.getProperty("test.hello"));
        assertEquals("123", properties.getProperty("test.number"));
    }
}
