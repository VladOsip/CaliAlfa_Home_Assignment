package converter.handlers;

import converter.handlers.markets.FirstHalfTotalHandler;
import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class FirstHalfTotalHandlerTest {
    private FirstHalfTotalHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new FirstHalfTotalHandler();
    }
    
    @Test
    void testGetMarketTypeId() {
        assertEquals("68", handler.getMarketTypeId());
    }
    
    @Test
    void testExtractSpecifiers() {
        InputMarket market = createSampleFirstHalfTotalMarket();
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals(1, specifiers.size());
        assertEquals("1.5", specifiers.get("total"));
    }
    
    @Test
    void testGetSelectionTypeId() {
        assertEquals(12, handler.getSelectionTypeId("over 1.5"));
        assertEquals(13, handler.getSelectionTypeId("under 1.5"));
        assertEquals(12, handler.getSelectionTypeId("OVER 0.5"));
        assertEquals(13, handler.getSelectionTypeId("UNDER 2.5"));
    }
    
    @Test
    void testGetSelectionTypeIdInvalid() {
        assertThrows(IllegalArgumentException.class, 
            () -> handler.getSelectionTypeId("invalid selection"));
    }
    
    @Test
    void testHandleFirstHalfTotalMarket() {
        InputMarket inputMarket = createSampleFirstHalfTotalMarket();
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("123456_68_1.5", outputMarket.getMarketUid());
        assertEquals("68", outputMarket.getMarketTypeId());
        assertEquals("1.5", outputMarket.getSpecifiers().get("total"));
        assertEquals(2, outputMarket.getSelections().size());
        
        List<OutputSelection> selections = outputMarket.getSelections();
        assertEquals(12, selections.get(0).getSelectionTypeId()); // over
        assertEquals(13, selections.get(1).getSelectionTypeId()); // under
        assertEquals("123456_68_1.5_12", selections.get(0).getSelectionUid());
        assertEquals("123456_68_1.5_13", selections.get(1).getSelectionUid());
    }
    
    @Test
    void testExtractSpecifiersWithDifferentTotalValues() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("over 0.5", 1.3),
            new InputSelection("under 0.5", 3.5)
        );
        InputMarket market = new InputMarket("1st half - total", "789", selections);
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals("0.5", specifiers.get("total"));
    }
    
    @Test
    void testHandleWithDecimalTotalValues() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("over 2.75", 1.9),
            new InputSelection("under 2.75", 1.9)
        );
        InputMarket inputMarket = new InputMarket("1st half - total", "999", selections);
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("999_68_2.75", outputMarket.getMarketUid());
        assertEquals("2.75", outputMarket.getSpecifiers().get("total"));
    }
    
    private InputMarket createSampleFirstHalfTotalMarket() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("over 1.5", 2.1),
            new InputSelection("under 1.5", 1.7)
        );
        return new InputMarket("1st half - total", "123456", selections);
    }
}