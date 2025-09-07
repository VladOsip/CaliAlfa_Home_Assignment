package converter.handlers;

import converter.handlers.markets.BothTeamsScoreHandler;
import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BothTeamsScoreHandlerTest {
    private BothTeamsScoreHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new BothTeamsScoreHandler();
    }
    
    @Test
    void testGetMarketTypeId() {
        assertEquals("50", handler.getMarketTypeId());
    }
    
    @Test
    void testExtractSpecifiers() {
        InputMarket market = createSampleMarket();
        assertTrue(handler.extractSpecifiers(market).isEmpty());
    }
    
    @Test
    void testGetSelectionTypeId() {
        assertEquals(10, handler.getSelectionTypeId("Yes"));
        assertEquals(11, handler.getSelectionTypeId("No"));
        assertEquals(10, handler.getSelectionTypeId("yes")); // case insensitive
        assertEquals(11, handler.getSelectionTypeId("NO"));
    }
    
    @Test
    void testGetSelectionTypeIdInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> handler.getSelectionTypeId("Maybe"));
        assertTrue(exception.getMessage().contains("Unknown both teams to score selection"));
    }
    
    @Test
    void testHandleBothTeamsScoreMarket() {
        InputMarket inputMarket = createSampleMarket();
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("123456_50", outputMarket.getMarketUid());
        assertEquals("50", outputMarket.getMarketTypeId());
        assertTrue(outputMarket.getSpecifiers().isEmpty());
        assertEquals(2, outputMarket.getSelections().size());
        
        List<OutputSelection> selections = outputMarket.getSelections();
        assertEquals(10, selections.get(0).getSelectionTypeId()); // Yes
        assertEquals(11, selections.get(1).getSelectionTypeId()); // No
        assertEquals("123456_50_10", selections.get(0).getSelectionUid());
        assertEquals("123456_50_11", selections.get(1).getSelectionUid());
    }
    
    private InputMarket createSampleMarket() {
        return new InputMarket("Both teams to score", "123456", Arrays.asList(
            new InputSelection("Yes", 1.7),
            new InputSelection("No", 2.1)
        ));
    }
}