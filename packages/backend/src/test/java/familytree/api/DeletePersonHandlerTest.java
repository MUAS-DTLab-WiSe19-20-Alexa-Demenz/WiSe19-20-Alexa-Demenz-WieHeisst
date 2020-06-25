package familytree.api;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.familytree.api.ApiGatewayResponse;
import com.familytree.api.DeletePersonHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;

import org.junit.Before;
import org.junit.Test;

public class DeletePersonHandlerTest {
    private DeletePersonHandler handler;
    IDataSource dataSource = new DummyDataSource();

    @Before
    public void setup() {
        handler = new DeletePersonHandler();
        handler.setDataSource(dataSource);
    }

    @Test
    public void validInput() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("body", "{\"userId\":\"user_id\",\"personId\": \"13\"}");
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertTrue(response.getStatusCode() == 200);
    }

    @Test
    public void invalidInput() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("body", "{\"userId\":\"asdf\"}");
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertTrue(response.getStatusCode() == 500);
    }
}