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

public class DeletePersonHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(DeletePersonHandler.class);

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
            Person person = dataSource.getPersonById(body.get("userId").asText(), body.get("personId").asText());

            dataSource.delete(person);

            // send the response back
            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(person).setHeaders(headers).build();

        } catch (Exception ex) {
            LOG.error("Error in deleting Person: " + ex);

            // send the error response back
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody("Error in deleting Person: " + input)
                    .setHeaders(headers).build();
        }

    }
}
