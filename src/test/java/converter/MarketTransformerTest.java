package converter;

import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MarketTransformerTest {
    private MarketTransformer transformer;
    
    @BeforeEach
    void setUp() {
        transformer = new MarketTransformer();
    }
    
    @Test
    void testTransformKnownMarketTypes() {
        List<InputMarket> inputMarkets = Arrays.asList(
            createInputMarket("1x2", "123", "Team A", 1.5, "draw", 3.0, "Team B", 2.5),
            createInputMarket("Total", "123", "over 2.5", 1.8, "under 2.5", 2.0),
            createInputMarket("Both teams to score", "123", "Yes", 1.7, "No", 2.1)
        );
        
        List<OutputMarket> outputMarkets = transformer.transform(inputMarkets);
        
        assertEquals(3, outputMarkets.size());
        assertEquals("1", outputMarkets.get(0).getMarketTypeId());
        assertEquals("18", outputMarkets.get(1).getMarketTypeId());
        assertEquals("50", outputMarkets.get(2).getMarketTypeId());
    }
    
    @Test
    void testTransformEmptyList() {
        List<OutputMarket> result = transformer.transform(new ArrayList<>());
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testTransformUnknownMarketType() {
        List<InputMarket> inputMarkets = Arrays.asList(
            new InputMarket("Unknown Market", "123", Arrays.asList(
                new InputSelection("Option 1", 2.0)
            ))
        );
        
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> transformer.transform(inputMarkets));
        assertTrue(exception.getMessage().contains("Unknown market type"));
    }
    
    @Test
    void testTransformCaseInsensitive() {
        List<InputMarket> inputMarkets = Arrays.asList(
            new InputMarket("1X2", "123", Arrays.asList(
                new InputSelection("Team A", 1.5),
                new InputSelection("draw", 3.0),
                new InputSelection("Team B", 2.5)
            ))
        );
        
        List<OutputMarket> result = transformer.transform(inputMarkets);
        assertEquals("1", result.get(0).getMarketTypeId());
    }
    
    private InputMarket createInputMarket(String name, String eventId, String sel1Name, double sel1Odds, String sel2Name, double sel2Odds) {
        return new InputMarket(name, eventId, Arrays.asList(
            new InputSelection(sel1Name, sel1Odds),
            new InputSelection(sel2Name, sel2Odds)
        ));
    }
    
    private InputMarket createInputMarket(String name, String eventId, String sel1Name, double sel1Odds, String sel2Name, double sel2Odds, String sel3Name, double sel3Odds) {
        return new InputMarket(name, eventId, Arrays.asList(
            new InputSelection(sel1Name, sel1Odds),
            new InputSelection(sel2Name, sel2Odds),
            new InputSelection(sel3Name, sel3Odds)
        ));
    }
}