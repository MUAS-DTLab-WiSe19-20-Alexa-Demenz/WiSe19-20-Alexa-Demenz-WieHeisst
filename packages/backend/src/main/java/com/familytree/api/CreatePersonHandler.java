package com.familytree.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.familytree.model.DynamoDataSource;
import com.familytree.model.IDataSource;
import com.familytree.model.Person;

public class CreatePersonHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CreatePersonHandler.class);

    IDataSource dataSource = new DynamoDataSource();

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("received: " + input);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");

        try {
            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

            // create the Person object for post
            Person person = new Person();

            person.setUserId(body.get("userId").asText());
            if (body.has("firstName"))
                person.setFirstName(body.get("firstName").asText());
            if (body.has("lastName"))
                person.setLastName(body.get("lastName").asText());
            if (body.has("phoneNumber"))
                person.setPhoneNumber(body.get("phoneNumber").asText());
            if (body.has("work"))
                person.setWork(body.get("work").asText());
            if (body.has("birthDate"))
                person.setBirthDate(body.get("birthDate").asText());
            if (body.has("relationToUser"))
                person.setRelationToUser(body.get("relationToUser").asText());

            dataSource.save(person);

            // send the response back
            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(person).setHeaders(headers).build();

        } catch (Exception ex) {
            LOG.error("Error in saving Person: " + ex);

            // send the error response back
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody("Error in saving Person: " + input)
                    .setHeaders(headers).build();
        }

    }
}
