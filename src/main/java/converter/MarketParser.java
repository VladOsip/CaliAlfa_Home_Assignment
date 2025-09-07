package converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import converter.models.InputMarket;

/* converts raw JSON strings into Java Objects. offer in-place validation*/ 
public class MarketParser {
    private ObjectMapper objectMapper;
    
    public MarketParser() {
        this.objectMapper = new ObjectMapper();
    }
    
    public List<InputMarket> parse(String jsonContent) throws Exception {
        // Validate input
        validateJsonInput(jsonContent);
        
        try {
            List<InputMarket> markets = objectMapper.readValue(
                jsonContent, 
                //java type erasure countermeasure
                new TypeReference<List<InputMarket>>() {}
            );
            
            // Validate parsed data
            validateMarketData(markets);
            
            return markets;
            
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                "Invalid JSON format: " + e.getMessage(), e
            );
        }
    }
    
    private void validateJsonInput(String jsonContent) {
        if (jsonContent == null) {
            throw new IllegalArgumentException("JSON content cannot be null");
        }
        
        if (jsonContent.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON content cannot be empty");
        }
        
        // Basic JSON structure check
        String trimmed = jsonContent.trim();
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            throw new IllegalArgumentException(
                "Expected JSON array of markets, got: " + 
                trimmed.substring(0, Math.min(50, trimmed.length())) + "..."
            );
        }
    }
    
    private void validateMarketData(List<InputMarket> markets) {
        if (markets == null) {
            throw new IllegalArgumentException("Parsed market list cannot be null");
        }
        
        if (markets.isEmpty()) {
            throw new IllegalArgumentException("No markets found in input data");
        }
        
        for (int i = 0; i < markets.size(); i++) {
            InputMarket market = markets.get(i);
            validateSingleMarket(market, i);
        }
    }
    
    private void validateSingleMarket(InputMarket market, int index) {
        if (market == null) {
            throw new IllegalArgumentException("Market at index " + index + " is null");
        }
        
        if (market.getName() == null || market.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Market at index " + index + " has null or empty name"
            );
        }
        
        if (market.getEventId() == null || market.getEventId().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Market at index " + index + " (" + market.getName() + 
                ") has null or empty event_id"
            );
        }
        
        if (market.getSelections() == null || market.getSelections().isEmpty()) {
            throw new IllegalArgumentException(
                "Market at index " + index + " (" + market.getName() + 
                ") has no selections"
            );
        }
        
        // Validate each selection
        for (int j = 0; j < market.getSelections().size(); j++) {
            var selection = market.getSelections().get(j);
            if (selection == null) {
                throw new IllegalArgumentException(
                    "Selection " + j + " in market '" + market.getName() + "' is null"
                );
            }
            
            if (selection.getName() == null || selection.getName().trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Selection " + j + " in market '" + market.getName() + 
                    "' has null or empty name"
                );
            }
            
            if (selection.getOdds() <= 0) {
                throw new IllegalArgumentException(
                    "Selection '" + selection.getName() + "' in market '" + 
                    market.getName() + "' has invalid odds: " + selection.getOdds()
                );
            }
        }
    }
}