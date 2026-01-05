import com.cybershrek.tools.http.HandyClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandyClientTest {


    @Test
    public void testGET() {
        assertEquals(200, new HandyClient()
                .url("https://example.com")
                .GET()
                .statusCode());
    }

    @Test
    public void testPOST() {
        assertEquals(405, new HandyClient()
                .url("https://example.com")
                .body("{}")
                .POST()
                .statusCode());
    }
}
