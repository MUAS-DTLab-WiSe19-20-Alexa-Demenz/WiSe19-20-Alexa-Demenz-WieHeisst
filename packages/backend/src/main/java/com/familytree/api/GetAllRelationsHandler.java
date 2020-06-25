package com.familytree.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.familytree.alexa.PhrasesAndConstants;

public class GetAllRelationsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetAllRelationsHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("received: " + input);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");

        try {
            // send the response back
            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(PhrasesAndConstants.RELATIONS.values())
                    .setHeaders(headers).build();

        } catch (Exception ex) {
            LOG.error("Error in deleting Person: " + ex);

            // send the error response back
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody("Error in getting Relations: " + input)
                    .setHeaders(headers).build();
        }

    }
}
