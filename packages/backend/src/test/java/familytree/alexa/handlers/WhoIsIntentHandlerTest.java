package familytree.alexa.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.familytree.alexa.handlers.WhoIsIntentHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;
import com.familytree.alexa.PhrasesAndConstants;

public class WhoIsIntentHandlerTest {
	private WhoIsIntentHandler handler;
	IDataSource dataSource = new DummyDataSource();

	@Before
	public void setup() {
		handler = new WhoIsIntentHandler(dataSource);
	}

	@Test
	public void testCanHandle() {
		final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
		when(inputMock.matches(any())).thenReturn(true);
		assertTrue(handler.canHandle(inputMock));
	}

	@Test
	public void testHandleNoMember() {
		// handler = new WhoIsIntentHandler(dataSource);
		final Response response = TestUtil.standardTestForHandle(handler, "", "", "");
		// System.out.println(response.getCard());
		assertTrue(response.getCard().toString().contains(PhrasesAndConstants.CARD_TITLE));
	}

	@Test
	public void testHandleRelationResolve() {
		System.out.println("testHandleRelationResolve():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Bruder", "Vater", "").getCard().toString()
				.contains("Dagobert"));
	}

	@Test
	public void testHandleName() {
		System.out.println("testHandleName():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Mutter", "", "").getCard().toString()
				.contains("Der Name, nach dem du suchst ist RosiM. RosiM ist deine Mutter."));
	}

	@Test
	public void testHandleMultipleName() {
		System.out.println("testHandleMultipleName():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Bruder", "", "").getCard().toString().contains(
				"Folgende Personen habe ich gefunden: Der Name, nach dem du suchst ist Hans0. Hans0 ist dein Bruder.Der Name, nach dem du suchst ist Hans2. Hans2 ist dein Bruder.Der Name, nach dem du suchst ist Hans4. Hans4 ist dein Bruder.Der Name, nach dem du suchst ist Hans6. Hans6 ist dein Bruder.Der Name, nach dem du suchst ist Hans8. Hans8 ist dein Bruder."));
	}

}
