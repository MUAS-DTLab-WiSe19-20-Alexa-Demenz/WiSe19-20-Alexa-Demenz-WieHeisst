package familytree.alexa.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.familytree.alexa.handlers.HobbyIntentHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;
import com.familytree.alexa.PhrasesAndConstants;

public class HobbyIntentHandlerTest {
	private HobbyIntentHandler handler;
	IDataSource dataSource = new DummyDataSource();

	@Before
	public void setup() {
		handler = new HobbyIntentHandler(dataSource);
	}

	@Test
	public void testCanHandle() {
		final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
		when(inputMock.matches(any())).thenReturn(true);
		assertTrue(handler.canHandle(inputMock));
	}

	@Test
	public void testHandleNoMember() {
		final Response response = TestUtil.standardTestForHandle(handler,"", "", "");
		assertTrue(response.getCard().toString().contains(PhrasesAndConstants.CARD_TITLE));
	}

	@Test
	public void testHandleNoHobby() {
		System.out.println("testHandleHobby():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Mutter","", "").getCard().toString()
				.contains("Mutter RosiM hat keine Hobbies."));
	}

	@Test
	public void testHandleHobby() {
		System.out.println("testHandleHobby():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Cousin","", "").getCard().toString()
				.contains("Cousin Johann betreibt das Hobby schwimmen."));
	}
	
	@Test
	public void testHandleRelationResolve() {
		System.out.println("testHandleRelationResolve():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Bruder", "Vater", "").getCard().toString().contains(
				"Dagobert"));
	}

}
