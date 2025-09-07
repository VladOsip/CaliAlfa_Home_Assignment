package converter.handlers;

import converter.handlers.markets.SecondHalfHandicapHandler;
import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class SecondHalfHandicapHandlerTest {
    private SecondHalfHandicapHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new SecondHalfHandicapHandler();
    }
    
    @Test
    void testGetMarketTypeId() {
        assertEquals("88", handler.getMarketTypeId());
    }
    
    @Test
    void testExtractSpecifiers() {
        InputMarket market = createSampleSecondHalfHandicapMarket();
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals(1, specifiers.size());
        assertEquals("1", specifiers.get("hcp"));
    }
    
    @Test
    void testGetSelectionTypeId() {
        assertEquals(1714, handler.getSelectionTypeId("Team A +1"));
        assertEquals(1715, handler.getSelectionTypeId("Team B -1"));
        assertEquals(1714, handler.getSelectionTypeId("something +2.5"));
        assertEquals(1715, handler.getSelectionTypeId("something -1.5"));
        assertEquals(1714, handler.getSelectionTypeId("team a"));
        assertEquals(1715, handler.getSelectionTypeId("team b"));
    }
    
    @Test
    void testGetSelectionTypeIdCaseInsensitive() {
        assertEquals(1714, handler.getSelectionTypeId("TEAM A +1"));
        assertEquals(1715, handler.getSelectionTypeId("TEAM B -1"));
    }
    
    @Test
    void testHandleSecondHalfHandicapMarket() {
        InputMarket inputMarket = createSampleSecondHalfHandicapMarket();
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("123456_88_1", outputMarket.getMarketUid());
        assertEquals("88", outputMarket.getMarketTypeId());
        assertEquals("1", outputMarket.getSpecifiers().get("hcp"));
        assertEquals(2, outputMarket.getSelections().size());
        
        List<OutputSelection> selections = outputMarket.getSelections();
        assertEquals(1714, selections.get(0).getSelectionTypeId()); // Team A
        assertEquals(1715, selections.get(1).getSelectionTypeId()); // Team B
        assertEquals("123456_88_1_1714", selections.get(0).getSelectionUid());
        assertEquals("123456_88_1_1715", selections.get(1).getSelectionUid());
    }
    
    @Test
    void testExtractSpecifiersWithDifferentHandicapValues() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Liverpool +2.5", 1.6),
            new InputSelection("Manchester City -2.5", 2.3)
        );
        InputMarket market = new InputMarket("2nd half - handicap", "789", selections);
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals("2.5", specifiers.get("hcp"));
    }
    
    @Test
    void testHandleWithLargeHandicapValues() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Team A +3.5", 1.4),
            new InputSelection("Team B -3.5", 2.8)
        );
        InputMarket inputMarket = new InputMarket("2nd half - handicap", "888", selections);
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("888_88_3.5", outputMarket.getMarketUid());
        assertEquals("3.5", outputMarket.getSpecifiers().get("hcp"));
    }
    
    @Test
    void testHandleWithNegativeHandicapForTeamA() {
        // Edge case: Team A could have negative handicap too
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Team A -0.5", 2.2),
            new InputSelection("Team B +0.5", 1.65)
        );
        InputMarket inputMarket = new InputMarket("2nd half - handicap", "999", selections);
        
        OutputMarket outputMarket = handler.handle(inputMarket);
        
        assertEquals("999_88_0.5", outputMarket.getMarketUid());
        assertEquals("0.5", outputMarket.getSpecifiers().get("hcp"));
        // Team A should still get 1714 even with negative handicap
        assertEquals(1714, outputMarket.getSelections().get(0).getSelectionTypeId());
        assertEquals(1715, outputMarket.getSelections().get(1).getSelectionTypeId());
    }
    
    @Test
    void testExtractSpecifiersWithIntegerHandicap() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Team A +2", 1.8),
            new InputSelection("Team B -2", 2.0)
        );
        InputMarket market = new InputMarket("2nd half - handicap", "456", selections);
        
        Map<String, String> specifiers = handler.extractSpecifiers(market);
        
        assertEquals("2", specifiers.get("hcp"));
    }
    
    private InputMarket createSampleSecondHalfHandicapMarket() {
        List<InputSelection> selections = Arrays.asList(
            new InputSelection("Team A +1", 2.05),
            new InputSelection("Team B -1", 1.75)
        );
        return new InputMarket("2nd half - handicap", "123456", selections);
    }
}