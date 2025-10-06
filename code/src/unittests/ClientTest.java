package unittests;

import networkio.Client;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    Client client;

    @Before
    public void setUp() throws IOException {
        client = new Client();
    }

    @Test
    public void testClientConstructor() {
        try {
            Assertions.assertNotNull(client, "The networkio.Client instance should not be null.");
        } catch (Exception e) {
            fail("Exception thrown during networkio.Client construction: " + e.getMessage());
        }
    }
}
