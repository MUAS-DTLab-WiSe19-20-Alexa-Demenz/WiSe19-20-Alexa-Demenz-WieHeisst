/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.familytree.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import com.familytree.alexa.PhrasesAndConstants;
import com.familytree.model.IDataSource;
import com.familytree.model.Person;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HobbyIntentHandler implements RequestHandler {

	final IDataSource data;
	private static final Logger LOG = Logger.getLogger(HobbyIntentHandler.class);

	public HobbyIntentHandler(IDataSource dataSource) {
		data = dataSource;
	}

	@Override
	public boolean canHandle(HandlerInput input) {

		return input.matches(intentName("HobbyIntent"));
	}

	private Map<String, Slot> getInputMap(HandlerInput input) {
		Map<String, Slot> inputMap = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots();
		return inputMap;
	}

	private String buildSpeechText(Person person) {
		String speechText;
		speechText = String.format("%s %s",
				PhrasesAndConstants.RELATIONS.valueOf(person.getRelationToUser()).getRelation(), person.getFirstName());
		String[] hobbies = person.getHobbies();
		if (hobbies == null || hobbies.length == 0) {
			speechText += String.format(" %s.", PhrasesAndConstants.NO_HOBBIES_FOUND);
		} else if (hobbies.length == 1) {
			speechText += String.format(" %s %s.", PhrasesAndConstants.HOBBY_OUTPUT, hobbies[0]);
		} else {
			speechText += PhrasesAndConstants.HOBBY_OUTPUT_MULTIPLE;
			for (String hobby : hobbies) {
				speechText += " " + hobby + ",";
			}
		}
		return speechText;
	}

	private String buildSpeechText(List<Person> personList) {
		String speechText = PhrasesAndConstants.MULTIPLE_MATCHES;
		for (Person p : personList) {
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
