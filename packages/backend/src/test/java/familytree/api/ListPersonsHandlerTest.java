package familytree.api;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.familytree.api.ApiGatewayResponse;
import com.familytree.api.ListPersonsHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;

import org.junit.Before;
import org.junit.Test;

public class ListPersonsHandlerTest {
    private ListPersonsHandler handler;
    IDataSource dataSource = new DummyDataSource();

    @Before
    public void setup() {
        handler = new ListPersonsHandler();
        handler.setDataSource(dataSource);
    }

    @Test
    public void validInput() {
        Map<String, Object> input = new HashMap<String, Object>();
        Map<String, Object> queryStringParameters = new HashMap<String, Object>();
        queryStringParameters.put("userId", "user_id");
        input.put("queryStringParameters", queryStringParameters);
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertTrue(response.getStatusCode() == 200);
    }

    @Test
    public void invalidInput() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("queryStringParameters", null);
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertTrue(response.getStatusCode() == 500);
    }
}