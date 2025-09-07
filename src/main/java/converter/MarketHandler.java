package converter;

import converter.models.*;
import java.util.*;

// Abstract base class for market handlers
// uses template method pattern
public abstract class MarketHandler {
    
    public abstract String getMarketTypeId();//unique market type ID
    public abstract Map<String, String> extractSpecifiers(InputMarket market);
    public abstract int getSelectionTypeId(String selectionName);// map selection name to type ID
    //template of transofrmation
    public OutputMarket handle(InputMarket input) {
        // Step 1: Get market-specific information from concrete handler
        String marketTypeId = getMarketTypeId();
        Map<String, String> specifiers = extractSpecifiers(input);

        // Step 2: Build the unique market identifier
        String marketUid = buildMarketUid(input.getEventId(), marketTypeId, specifiers);

        // Step 3: Transform all selections from input format to output format
        List<OutputSelection> outputSelections = new ArrayList<>();
        for (InputSelection inputSelection : input.getSelections()) {
            // Map selection name to type ID using concrete handler's logic
            int selectionTypeId = getSelectionTypeId(inputSelection.getName().toLowerCase());
            
            // Build unique selection identifier
            String selectionUid = buildSelectionUid(marketUid, selectionTypeId);
            
            // Create output selection with transformed data
            OutputSelection outputSelection = new OutputSelection(
                selectionUid,
                selectionTypeId,
                inputSelection.getOdds()  // Odds remain unchanged
            );
            outputSelections.add(outputSelection);
        }
        // Step 4: Return the fully transformed market
        return new OutputMarket(marketUid, marketTypeId, specifiers, outputSelections);
    }
    
    // construct unique IDs following the pattern of {event_id}_{market_type_id}_{specifier_value}
    protected String buildMarketUid(String eventId, String marketTypeId, Map<String, String> specifiers) {
        StringBuilder uid = new StringBuilder();
        uid.append(eventId).append("_").append(marketTypeId);
        
        if (!specifiers.isEmpty()) {
            // Add specifiers to UID - for this implementation we'll use the first specifier value
            String specifierValue = specifiers.values().iterator().next();
            uid.append("_").append(specifierValue);
        }
        
        return uid.toString();
    }
    
    // construct unique selection IDs following the pattern of {market_uid}_{selection_type_id}
    protected String buildSelectionUid(String marketUid, int selectionTypeId) {
        return marketUid + "_" + selectionTypeId;
    }
    
    protected String extractNumericValue(String text) {
        // Extract numeric value from strings like "over 2.5", "Team A +1.5"
        String[] parts = text.split("\\s+");
        for (String part : parts) {
            // Remove + or - signs and check if it's a number
            String cleaned = part.replaceAll("[+\\-]", "");
            try {
                Double.parseDouble(cleaned);
                return cleaned;
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return "";
    }
}