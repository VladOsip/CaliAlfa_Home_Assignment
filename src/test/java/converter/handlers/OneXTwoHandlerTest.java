package converter.handlers;

import converter.handlers.markets.OneXTwoHandler;
import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class OneXTwoHandlerTest {
    private OneXTwoHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new OneXTwoHandler();
    }
    
    @Test
    void testGetMarketTypeId() {
        assertEquals("1", handler.getMarketTypeId());
    }
    
    @Test
    void testExtractSpecifiers() {
        InputMarket market = createSampleMarket();
        assertTrue(handler.extractSpecifiers(market).isEmpty());
    }
    
    @Test
    void testGetSelectionTypeId() {
        assertEquals(1, handler.getSelectionTypeId("Team A"));
        assertEquals(2, handler.getSelectionTypeId("draw"));
        assertEquals(3, handler.getSelectionTypeId("Team B"));
        assertEquals(1, handler.getSelectionTypeId("team a")); // case insensitive
    }
    
    @Test
    void testGetSelectionTypeIdInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> handler.getSelectionTypeId("invalid"));
        assertTrue(exception.getMessage().contains("Unable to determine selection type"));
    }
    
    @Test
    void testHandle1x2Market() {
        InputMarket inputMarket = createSampleMarket();
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("123456_1", outputMarket.getMarketUid());
        assertEquals("1", outputMarket.getMarketTypeId());
        assertTrue(outputMarket.getSpecifiers().isEmpty());
        assertEquals(3, outputMarket.getSelections().size());
        
        List<OutputSelection> selections = outputMarket.getSelections();
        assertEquals(1, selections.get(0).getSelectionTypeId()); // Team A
        assertEquals(2, selections.get(1).getSelectionTypeId()); // draw
        assertEquals(3, selections.get(2).getSelectionTypeId()); // Team B
    }
    
    private InputMarket createSampleMarket() {
        return new InputMarket("1x2", "123456", Arrays.asList(
            new InputSelection("Team A", 1.65),
            new InputSelection("draw", 3.2),
            new InputSelection("Team B", 2.6)
        ));
    }
}