package familytree.api;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.familytree.api.ApiGatewayResponse;
import com.familytree.api.CreatePersonHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;

import org.junit.Before;
import org.junit.Test;

public class CreatePersonHandlerTest {
    private CreatePersonHandler handler;
    IDataSource dataSource = new DummyDataSource();

    @Before
    public void setup() {
        handler = new CreatePersonHandler();
        handler.setDataSource(dataSource);
    }

    @Test
    public void validInput() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("body",
                "{\"userId\":\"asdf\",\"firstName\":\"Tersterli\",\"lastName\":\"Test\",\"phoneNumber\":\"12341234\",\"work\":\"asdfadf\",\"hobbies\":null,\"birthDate\":\"2019-02-01\",\"relationToUser\":\"GRANDMOTHER\"}");
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertTrue(response.getStatusCode() == 200);
        assertTrue(response.getHeaders().equals(headers));
        assertTrue(!response.isIsBase64Encoded());
    }

    @Test
    public void invalidInput() {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("body",
                "{\"firstName\":\"Tersterli\",\"lastName\":\"Test\",\"phoneNumber\":\"12341234\",\"work\":\"asdfadf\",\"hobbies\":null,\"birthDate\":\"2019-02-01\",\"relationToUser\":\"GRANDMOTHER\"}");
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertTrue(response.getStatusCode() == 500);
    }
}