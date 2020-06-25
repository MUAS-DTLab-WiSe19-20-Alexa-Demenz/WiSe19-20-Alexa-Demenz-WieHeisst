package familytree.alexa.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import com.familytree.alexa.PhrasesAndConstants;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TestUtil {

        public static HandlerInput mockHandlerInput(String relationship, String memberSecond, String name,
                        Map<String, Object> sessionAttributes, Map<String, Object> persistentAttributes,
                        Map<String, Object> requestAttributes) {
                final AttributesManager attributesManagerMock = Mockito.mock(AttributesManager.class);
                when(attributesManagerMock.getSessionAttributes()).thenReturn(sessionAttributes);
                when(attributesManagerMock.getPersistentAttributes()).thenReturn(persistentAttributes);
                when(attributesManagerMock.getRequestAttributes()).thenReturn(requestAttributes);

                final User user = User.builder().withUserId("123456").build();

                final Session session = Session.builder().withUser(user).build();

                // Mock Slots
                final RequestEnvelope requestEnvelopeMock = RequestEnvelope.builder().withSession(session)
                                .withRequest(IntentRequest.builder().withIntent(Intent.builder()
                                                .putSlotsItem(PhrasesAndConstants.MEMBER_SLOT,
                                                                Slot.builder().withName(PhrasesAndConstants.MEMBER_SLOT)
                                                                                .withValue(relationship).build())
                                                .putSlotsItem(PhrasesAndConstants.MEMBER_SECOND_SLOT,
                                                        Slot.builder().withName(PhrasesAndConstants.MEMBER_SECOND_SLOT)
                                                                        .withValue(memberSecond).build())
                                                .putSlotsItem(PhrasesAndConstants.NAME_SLOT,
                                                                Slot.builder().withName(PhrasesAndConstants.NAME_SLOT)
                                                                                .withValue(name).build())
                                                .build()).build())
                                .build();

                // Mock Handler input attributes
                final HandlerInput input = Mockito.mock(HandlerInput.class);
                when(input.getAttributesManager()).thenReturn(attributesManagerMock);
                when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
                when(input.getRequestEnvelope()).thenReturn(requestEnvelopeMock);

                return input;
        }

        public static Response standardTestForHandle(RequestHandler handler, String member, String memberSecond, String name) {
                final HandlerInput inputMock = TestUtil.mockHandlerInput(member, memberSecond, name, null, null, null);

                final Optional<Response> res = handler.handle(inputMock);

                assertTrue(res.isPresent());
                final Response response = res.get();

                assertNotEquals("Test", response.getReprompt());
                assertNotNull(response.getOutputSpeech());
                return response;
        }

        public static Response sessionEndedTestForHandle(RequestHandler handler) {
                final HandlerInput inputMock = TestUtil.mockHandlerInput(null, null, null, null, null, null);
                final Optional<Response> res = handler.handle(inputMock);

                assertTrue(res.isPresent());
                final Response response = res.get();
                assertTrue(response.getShouldEndSession());
                return response;
        }

}
