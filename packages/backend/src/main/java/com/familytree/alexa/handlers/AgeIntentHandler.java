package com.familytree.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.response.ResponseBuilder;
import com.familytree.alexa.PhrasesAndConstants;
import com.familytree.model.IDataSource;
import com.familytree.model.Person;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.log4j.Logger;
import static com.amazon.ask.request.Predicates.intentName;

public class AgeIntentHandler implements RequestHandler {

    final IDataSource data;
    private static final Logger LOG = Logger.getLogger(WhoIsIntentHandler.class);

    public AgeIntentHandler(IDataSource dataSource) {
        data = dataSource;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AgeIntent"));
    }

    private Map<String, Slot> getInputMap(HandlerInput input) {
        return ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots();
    }

    private String buildSpeechText(Person person) {
        PhrasesAndConstants.RELATIONS relationship = PhrasesAndConstants.RELATIONS.valueOf(person.getRelationToUser());
        return String.format("%s %s %s %s %s %s %s ", PhrasesAndConstants.RELATIONS.getPronounString(relationship),
                PhrasesAndConstants.RELATIONS.getRelationShipString(relationship), person.getFirstName(),
                person.getLastName(), PhrasesAndConstants.IS, person.calculateAge(), PhrasesAndConstants.YEARS_OLD);
    }

    private String buildSpeechText(List<Person> personList) {
        String speechText = PhrasesAndConstants.MULTIPLE_MATCHES;
        for (Person p : personList) {
            if (p.getRelationToUser() == null) {
                continue;
            }
            speechText += buildSpeechText(p);
        }
        return speechText;
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        LOG.info(input);
        ResponseBuilder responseBuilder = input.getResponseBuilder();
        data.setUserId(input.getRequestEnvelope().getSession().getUser().getUserId());

        final Map<String, Slot> inputMap = getInputMap(input);
        LOG.info(inputMap.toString());
        final List<Person> personList;
        String speechText; // holds the output text
        PhrasesAndConstants.RELATIONS relation;

        String firstMember = inputMap.get("Member").getValue();
        if (firstMember.isEmpty()) {
            speechText = PhrasesAndConstants.NO_ALEXA_MATCH;
            responseBuilder.withSpeech(speechText).withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                    .withShouldEndSession(false);

            return responseBuilder.build();
        }
        LOG.info("Is MemeberS present? " + inputMap.containsKey("MemberS"));
        try {
            if (inputMap.get("MemberS").getValue() != null && !inputMap.get("MemberS").getValue().isEmpty()) {
                String secondMember;
                if (PhrasesAndConstants.RELATIONS.getRelationByString(inputMap.get("MemberS").getValue()) == null) {
                    secondMember = inputMap.get("MemberS").getResolutions().getResolutionsPerAuthority().get(0)
                            .getValues().get(0).getValue().getName();
                } else {
                    secondMember = inputMap.get("MemberS").getValue();
                }
                LOG.info("second member: " + PhrasesAndConstants.RELATIONS.getRelationByString(firstMember));
                relation = PhrasesAndConstants.RELATIONS.getFamilyRelation(
                        PhrasesAndConstants.RELATIONS.getRelationByString(secondMember),
                        PhrasesAndConstants.RELATIONS.getRelationByString(firstMember))[0];
                if (relation == null) {
                    speechText = PhrasesAndConstants.NO_DB_MATCH;
                    responseBuilder.withSpeech(speechText).withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                            .withShouldEndSession(false);

                    return responseBuilder.build();
                }
            } else {
                relation = PhrasesAndConstants.RELATIONS.getRelationByString(firstMember);
            }
        } catch (Exception e) {
            LOG.error(e);
            relation = PhrasesAndConstants.RELATIONS.getRelationByString(firstMember);
        }

        if (relation == null) {
            speechText = PhrasesAndConstants.NO_ALEXA_MATCH;

            responseBuilder.withSpeech(speechText).withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                    .withShouldEndSession(false);

            return responseBuilder.build();
        }

        LOG.info("Member=" + relation.getRelation());
        personList = data.getPersonsByRelationship(relation.getRelation());
        LOG.info(personList);
        if (personList.size() == 0) {
            speechText = PhrasesAndConstants.NO_DB_MATCH;
        } else if (personList.size() == 1) {
            speechText = buildSpeechText(personList.get(0));
        } else {
            speechText = buildSpeechText(personList);
        }

        responseBuilder.withSpeech(speechText).withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                .withShouldEndSession(false);

        return responseBuilder.build();
    }
}
