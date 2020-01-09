package sample.cloud.stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.messaging.Message;
import org.springframework.test.context.TestPropertySource;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(
        properties = {"spring.cloud.function.definition = generate_flux|process"}
)
class SimpleStreamApplicationTest extends AbstractTest {
    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private SimpleStreamApplication handlers;
    @Autowired
    private FunctionCatalog catalog;

    // A way to test processor function only with direct
    // access to the function
    @Test
    public void testGeneratorAndProcessor() {
        final String testStr = "test";
        handlers.emitData(testStr);

        Object eventObj;
        final Message<byte[]> message = outputDestination.receive(1000);

        assertNotNull(message, "processing timeout");
        eventObj = message.getPayload();

        assertEquals(new String((byte[]) eventObj), testStr.toUpperCase());
    }

    // A way to test a workflow with internal function composition
    // declared through spring.cloud.function.definition
    @Test
    public void testProcessor() {
        final String testStr = "test";

        final Function<String, String> function = catalog.lookup("process");
        assertNotNull(function, "The function was not found");

        final String result = function.apply(testStr);
        assertEquals(result, testStr.toUpperCase());
    }
}
