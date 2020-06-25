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
import com.familytree.alexa.handlers.PhoneNumberIntentHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;
import com.familytree.alexa.PhrasesAndConstants;

public class PhoneNumberIntentHandlerTest {
    private PhoneNumberIntentHandler handler;
    IDataSource dataSource = new DummyDataSource();

    @Before
    public void setup() {
        handler = new PhoneNumberIntentHandler(dataSource);
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
    public void testHandleNoNumber() {
        System.out.println("testHandleNoNumber():");
        assertTrue(TestUtil.standardTestForHandle(handler, "Mutter","", "").getCard().toString()
                .contains("RosiM hat keine Telefonnummer."));
    }

    @Test
    public void testHandleNumber() {
        System.out.println("testHandleNumber():");
        System.out.println(TestUtil.standardTestForHandle(handler, "Cousin","", "").getCard().toString());
        assertTrue(TestUtil.standardTestForHandle(handler, "Cousin","", "").getCard().toString()
                .contains("dein Johann hat die Telefonnummer 93920199."));
    }
    
    @Test
	public void testHandleRelationResolve() {
		System.out.println("testHandleRelationResolve():");
		assertTrue(TestUtil.standardTestForHandle(handler, "Bruder", "Vater", "").getCard().toString().contains(
				"Dagobert"));
	}

}
