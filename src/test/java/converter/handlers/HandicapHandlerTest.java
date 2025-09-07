package converter.handlers;

import converter.handlers.markets.HandicapHandler;
import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class HandicapHandlerTest {
    private HandicapHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new HandicapHandler();
    }
    
    @Test
    void testGetMarketTypeId() {
        assertEquals("16", handler.getMarketTypeId());
    }
    
    @Test
    void testExtractSpecifiers() {
        InputMarket market = createSampleMarket();
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals(1, specifiers.size());
        assertEquals("1.5", specifiers.get("hcp"));
    }
    
    @Test
    void testGetSelectionTypeId() {
        assertEquals(1714, handler.getSelectionTypeId("Team A +1.5"));
        assertEquals(1715, handler.getSelectionTypeId("Team B -1.5"));
        assertEquals(1714, handler.getSelectionTypeId("team a")); // case insensitive
    }
    
    @Test
    void testGetSelectionTypeIdInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> handler.getSelectionTypeId("invalid selection"));
        assertTrue(exception.getMessage().contains("Unable to determine handicap team"));
    }
    
    @Test
    void testHandleHandicapMarket() {
        InputMarket inputMarket = createSampleMarket();
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("123456_16_1.5", outputMarket.getMarketUid());
        assertEquals("16", outputMarket.getMarketTypeId());
        assertEquals("1.5", outputMarket.getSpecifiers().get("hcp"));
        assertEquals(2, outputMarket.getSelections().size());
        
        List<OutputSelection> selections = outputMarket.getSelections();
        assertEquals(1714, selections.get(0).getSelectionTypeId()); // Team A
        assertEquals(1715, selections.get(1).getSelectionTypeId()); // Team B
    }
    
    @Test
    void testExtractSpecifiersWithDifferentValues() {
        InputMarket market = new InputMarket("Handicap", "789", Arrays.asList(
            new InputSelection("Team A +0.5", 1.8),
            new InputSelection("Team B -0.5", 2.0)
        ));
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        assertEquals("0.5", specifiers.get("hcp"));
    }
    
    private InputMarket createSampleMarket() {
        return new InputMarket("Handicap", "123456", Arrays.asList(
            new InputSelection("Team A +1.5", 1.8),
            new InputSelection("Team B -1.5", 2.0)
        ));
    }
}