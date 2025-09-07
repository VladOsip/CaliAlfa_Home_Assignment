package converter.handlers;

import converter.handlers.markets.FirstHalfHandicapHandler;
import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class FirstHalfHandicapHandlerTest {
    private FirstHalfHandicapHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new FirstHalfHandicapHandler();
    }
    
    @Test
    void testGetMarketTypeId() {
        assertEquals("66", handler.getMarketTypeId());
    }
    
    @Test
    void testExtractSpecifiers() {
        InputMarket market = createSampleFirstHalfHandicapMarket();
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals(1, specifiers.size());
        assertEquals("0.5", specifiers.get("hcp"));
    }
    
    @Test
    void testGetSelectionTypeId() {
        assertEquals(1714, handler.getSelectionTypeId("Team A +0.5"));
        assertEquals(1715, handler.getSelectionTypeId("Team B -0.5"));
        assertEquals(1714, handler.getSelectionTypeId("something +1.0"));
        assertEquals(1715, handler.getSelectionTypeId("something -0.25"));
        assertEquals(1714, handler.getSelectionTypeId("team a"));
        assertEquals(1715, handler.getSelectionTypeId("team b"));
    }
    
    @Test
    void testGetSelectionTypeIdCaseInsensitive() {
        assertEquals(1714, handler.getSelectionTypeId("TEAM A +0.5"));
        assertEquals(1715, handler.getSelectionTypeId("TEAM B -0.5"));
    }
    
    @Test
    void testHandleFirstHalfHandicapMarket() {
        InputMarket inputMarket = createSampleFirstHalfHandicapMarket();
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("123456_66_0.5", outputMarket.getMarketUid());
        assertEquals("66", outputMarket.getMarketTypeId());
        assertEquals("0.5", outputMarket.getSpecifiers().get("hcp"));
        assertEquals(2, outputMarket.getSelections().size());
        
        List<OutputSelection> selections = outputMarket.getSelections();
        assertEquals(1714, selections.get(0).getSelectionTypeId()); // Team A
        assertEquals(1715, selections.get(1).getSelectionTypeId()); // Team B
        assertEquals("123456_66_0.5_1714", selections.get(0).getSelectionUid());
        assertEquals("123456_66_0.5_1715", selections.get(1).getSelectionUid());
    }
    
    @Test
    void testExtractSpecifiersWithDifferentHandicapValues() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Barcelona +1.0", 1.8),
            new InputSelection("Real Madrid -1.0", 2.0)
        );
        InputMarket market = new InputMarket("1st half - handicap", "789", selections);
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals("1.0", specifiers.get("hcp"));
    }
    
    @Test
    void testHandleWithZeroHandicap() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Team A +0", 1.95),
            new InputSelection("Team B -0", 1.85)
        );
        InputMarket inputMarket = new InputMarket("1st half - handicap", "555", selections);
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("555_66_0", outputMarket.getMarketUid());
        assertEquals("0", outputMarket.getSpecifiers().get("hcp"));
    }
    
    @Test
    void testHandleWithDecimalHandicapValues() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Team A +0.25", 2.1),
            new InputSelection("Team B -0.25", 1.75)
        );
        InputMarket inputMarket = new InputMarket("1st half - handicap", "777", selections);
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("777_66_0.25", outputMarket.getMarketUid());
        assertEquals("0.25", outputMarket.getSpecifiers().get("hcp"));
    }
    
    private InputMarket createSampleFirstHalfHandicapMarket() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Team A +0.5", 1.9),
            new InputSelection("Team B -0.5", 1.9)
        );
        return new InputMarket("1st half - handicap", "123456", selections);
    }
}