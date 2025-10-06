package unittests;

import networkio.MainServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;

/**
 * Team Project Phase  -- unittests.ArgumentTopicTest
 *
 * This class contains JUnit test cases to verify the functionality of the networkio.MainServer class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 17, 2024
 *
 */

public class MainServerTest {

    MainServer server;

    @Before
    public void setUp() throws IOException {
        server = new MainServer();
    }

    @Test
    public void testServerExists() {
        try {
            Assertions.assertNotNull(server, "The Server instance should not be null.");
        } catch (Exception e) {
            fail("Exception thrown during Server construction: " + e.getMessage());
        }
    }

}
