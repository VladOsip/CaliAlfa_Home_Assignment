package converter.handlers;

import converter.handlers.markets.TotalHandler;
import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TotalHandlerTest {
    private TotalHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new TotalHandler();
    }
    
    @Test
    void testGetMarketTypeId() {
        assertEquals("18", handler.getMarketTypeId());
    }
    
    @Test
    void testExtractSpecifiers() {
        InputMarket market = createSampleMarket();
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals(1, specifiers.size());
        assertEquals("2.5", specifiers.get("total"));
    }
    
    @Test
    void testGetSelectionTypeId() {
        assertEquals(12, handler.getSelectionTypeId("over 2.5"));
        assertEquals(13, handler.getSelectionTypeId("under 2.5"));
        assertEquals(12, handler.getSelectionTypeId("OVER 3.5")); // case insensitive
    }
    
    @Test
    void testGetSelectionTypeIdInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> handler.getSelectionTypeId("invalid"));
        assertTrue(exception.getMessage().contains("Unknown total selection"));
    }
    
    @Test
    void testHandleTotalMarket() {
        InputMarket inputMarket = createSampleMarket();
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("123456_18_2.5", outputMarket.getMarketUid());
        assertEquals("18", outputMarket.getMarketTypeId());
        assertEquals("2.5", outputMarket.getSpecifiers().get("total"));
        assertEquals(2, outputMarket.getSelections().size());
        
        List<OutputSelection> selections = outputMarket.getSelections();
        assertEquals(12, selections.get(0).getSelectionTypeId()); // over
        assertEquals(13, selections.get(1).getSelectionTypeId()); // under
        assertEquals("123456_18_2.5_12", selections.get(0).getSelectionUid());
        assertEquals("123456_18_2.5_13", selections.get(1).getSelectionUid());
    }
    
    private InputMarket createSampleMarket() {
        return new InputMarket("Total", "123456", Arrays.asList(
            new InputSelection("over 2.5", 1.85),
            new InputSelection("under 2.5", 1.95)
        ));
    }
}