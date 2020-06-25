package familytree.api;

import static org.junit.Assert.assertTrue;

import com.familytree.alexa.PhrasesAndConstants;
import com.familytree.api.ApiGatewayResponse;
import com.familytree.api.GetAllRelationsHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class GetAllRelationsHandlerTest {
    private GetAllRelationsHandler handler;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        handler = new GetAllRelationsHandler();
    }

    @Test
    public void correctOutput() throws JsonProcessingException {
        ApiGatewayResponse response = handler.handleRequest(null, null);
        assertTrue(response.getStatusCode() == 200);
        assertTrue(response.getBody().equals(objectMapper.writeValueAsString(PhrasesAndConstants.RELATIONS.values())));
    }
}