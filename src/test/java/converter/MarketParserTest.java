package converter;

import converter.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class MarketParserTest {
    private MarketParser parser;
    
    @BeforeEach
    void setUp() {
        parser = new MarketParser();
    }
    
    @Test
    void testParseValidJson() throws Exception {
        String json = "[{\"name\":\"1x2\",\"event_id\":\"123456\",\"selections\":[{\"name\":\"Team A\",\"odds\":1.65},{\"name\":\"draw\",\"odds\":3.2}]}]";
        
        List<InputMarket> markets = parser.parse(json);
        
        assertEquals(1, markets.size());
        InputMarket market = markets.get(0);
        assertEquals("1x2", market.getName());
        assertEquals("123456", market.getEventId());
        assertEquals(2, market.getSelections().size());
        assertEquals("Team A", market.getSelections().get(0).getName());
        assertEquals(1.65, market.getSelections().get(0).getOdds(), 0.001);
    }
    
    // Test validation errors that MarketParser specifically addresses
    @Test
    void testParseNullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> parser.parse(null));
        assertTrue(exception.getMessage().contains("cannot be null"));
    }
    
    @Test
    void testParseEmptyInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> parser.parse(""));
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }
    
    @Test
    void testParseInvalidJsonFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> parser.parse("{\"not\":\"array\"}"));
        assertTrue(exception.getMessage().contains("Expected JSON array"));
    }
    
    @Test
    void testParseInvalidJsonSyntax() {
        assertThrows(Exception.class, () -> parser.parse("invalid json"));
    }
    
    @Test
    void testParseEmptyArray() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> parser.parse("[]"));
        assertTrue(exception.getMessage().contains("No markets found"));
    }
    
    @Test
    void testParseMarketWithNullName() {
        String json = "[{\"name\":null,\"event_id\":\"123\",\"selections\":[{\"name\":\"test\",\"odds\":1.5}]}]";
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> parser.parse(json));
        assertTrue(exception.getMessage().contains("null or empty name"));
    }
    
    @Test
    void testParseMarketWithInvalidOdds() {
        String json = "[{\"name\":\"test\",\"event_id\":\"123\",\"selections\":[{\"name\":\"test\",\"odds\":0}]}]";
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> parser.parse(json));
        assertTrue(exception.getMessage().contains("invalid odds"));
    }
    
    @Test
    void testParseMarketWithNoSelections() {
        String json = "[{\"name\":\"test\",\"event_id\":\"123\",\"selections\":[]}]";
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> parser.parse(json));
        assertTrue(exception.getMessage().contains("no selections"));
    }
}