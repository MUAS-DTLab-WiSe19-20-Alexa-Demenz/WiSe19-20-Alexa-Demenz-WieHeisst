package familytree.alexa.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.familytree.alexa.handlers.BirthdayIntentHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;
import com.familytree.alexa.PhrasesAndConstants;

public class BirthdayIntentHandlerTest {
    private BirthdayIntentHandler handler;
    IDataSource dataSource = new DummyDataSource();

    @Before
    public void setup() {
        handler = new BirthdayIntentHandler(dataSource);
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
        System.out.println(response.getCard());
        assertTrue(response.getCard().toString().contains(PhrasesAndConstants.CARD_TITLE));
    }

    @Test
    public void testBirthdayIntentRelation1() {
        assertTrue(TestUtil.standardTestForHandle(handler, "Mutter","", "").getCard().toString()
                .contains("deine Mutter RosiM Musterfrau hat am 1969-02-02 Geburtstag"));
    }

    @Test
    public void testBirthdayIntentRelation2() {
        assertTrue(TestUtil.standardTestForHandle(handler, "Cousine","", "").getCard().toString()
                .contains("deine Cousine Melinda Ackermann hat am 1988-04-20 Geburtstag"));
    }

    @Test
    public void testBirthdayIntentRelation3() {
        System.out.println(TestUtil.standardTestForHandle(handler, "Cousin","", ""));
        assertTrue(TestUtil.standardTestForHandle(handler, "Cousin","", "").getCard().toString()
                .contains("dein Cousin Johann August hat am 1979-12-05 Geburtstag"));
    }
    
    @Test
	public void testHandleRelationResolve() {
		System.out.println("testHandleRelationResolve():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Bruder", "Vater", "").getCard().toString().contains(
				"Dagobert"));
	}
}
