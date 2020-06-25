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

public class PhoneNumberIntentHandler implements RequestHandler {

	final IDataSource data;
	private static final Logger LOG = Logger.getLogger(WhoIsIntentHandler.class);

	public PhoneNumberIntentHandler(IDataSource dataSource) {
		data = dataSource;
	}

	@Override
	public boolean canHandle(HandlerInput input) {

		return input.matches(intentName("PhoneNumberIntent"));
	}

	private Map<String, Slot> getInputMap(HandlerInput input) {
		Map<String, Slot> inputMap = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots();
		return inputMap;
	}

	private String buildSpeechText(Person person) {
		String speechText;
		if (person.getPhoneNumber() == null) {
			speechText = String.format("%s %s %s %s.", person.getFirstName(), "hat", "keine", "Telefonnummer");
		} else {
			try {
				speechText = String.format("%s %s %s %s.",
						PhrasesAndConstants.RELATIONS.valueOf(person.getRelationToUser()).getPossesivpronomen(),
						person.getFirstName(), PhrasesAndConstants.PHONENUMBER, person.getPhoneNumber());
			} catch (Exception e) {
				speechText = PhrasesAndConstants.NO_DB_MATCH;
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

	public Optional<Response> handle(HandlerInput input) {
		ResponseBuilder responseBuilder = input.getResponseBuilder();

		data.setUserId(input.getRequestEnvelope().getSession().getUser().getUserId());

		final Map<String, Slot> inputMap = getInputMap(input);
		final List<Person> personList;
		String speechText; // holds the output text
		PhrasesAndConstants.RELATIONS relation;

		if (inputMap.get("Name") != null && !inputMap.get("Name").getValue().isEmpty()) {
			String name = inputMap.get("Name").getValue();
			List<Person> p = data.getPersonsByName(name);
			speechText = "Folgende Telefonnummern habe ich gefunden: ";
			for (Person per : p) {
				String phone = per.getPhoneNumber();
				if (phone == null) {
					phone = "unbekannt";
				}
				speechText += per.getFirstName() + " " + per.getLastName() + " hat die Nummer: " + per.getPhoneNumber();
			}
			if (p.isEmpty()) {
				speechText = PhrasesAndConstants.NO_DB_MATCH;
			}
			responseBuilder.withSpeech(speechText).withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
					.withShouldEndSession(false);

			return responseBuilder.build();
		}

		String firstMember = inputMap.get("Member").getValue();
		if (firstMember.isEmpty()) {
			speechText = PhrasesAndConstants.NO_ALEXA_MATCH;
			responseBuilder.withSpeech(speechText).withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
					.withShouldEndSession(false);

			return responseBuilder.build();
		}

		if (inputMap.get("MemberS") != null && !inputMap.get("MemberS").getValue().isEmpty()) {
			String secondMember = inputMap.get("MemberS").getValue();
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
