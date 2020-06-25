package com.familytree.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.familytree.model.DynamoDataSource;
import com.familytree.model.IDataSource;
import com.familytree.model.Person;

public class ListPersonsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(ListPersonsHandler.class);

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

            Map<String, Object> queryStringParameters = (Map<String, Object>) input.get("queryStringParameters");
            LOG.info("queryStringParameters=: " + queryStringParameters);
            // create the Person object for post
            List<Person> persons = dataSource.getPersonByUserId(queryStringParameters.get("userId").toString());

            // send the response back
            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(persons).setHeaders(headers).build();

        } catch (Exception ex) {
            LOG.error("Error in deleting Person: " + ex);

            // send the error response back
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody("Error in listing Persons: " + input)
                    .setHeaders(headers).build();
        }

    }
}
