package familytree.alexa.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.familytree.alexa.handlers.AgeIntentHandler;
import com.familytree.model.DummyDataSource;
import com.familytree.model.IDataSource;
import com.familytree.alexa.PhrasesAndConstants;

public class AgeIntentHandlerTest {
    private AgeIntentHandler handler;
    IDataSource dataSource = new DummyDataSource();

    @Before
    public void setup() {
        handler = new AgeIntentHandler(dataSource);
    }

    @Test
    public void testCanHandle() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }

    @Test
    public void testHandleNoMember() {
        final Response response = TestUtil.standardTestForHandle(handler, "","", "");
        System.out.println(response.getCard());
        assertTrue(response.getCard().toString().contains(PhrasesAndConstants.CARD_TITLE));
    }

    @Test
    public void testAgeIntentRelation1() {
        assertTrue(TestUtil.standardTestForHandle(handler, "Mutter","", "").getCard().toString()
                .contains("deine Mutter RosiM Musterfrau ist 50 Jahre alt "));
    }

    @Test
    public void testAgeIntentRelation2() {
        assertTrue(TestUtil.standardTestForHandle(handler, "Cousine","", "").getCard().toString()
                .contains("deine Cousine Melinda Ackermann ist 31 Jahre alt"));
    }

    @Test
    public void testAgeIntentRelation3() {
        assertTrue(TestUtil.standardTestForHandle(handler, "Cousin","", "").getCard().toString()
                .contains("dein Cousin Johann August ist 40 Jahre alt"));
    }
}