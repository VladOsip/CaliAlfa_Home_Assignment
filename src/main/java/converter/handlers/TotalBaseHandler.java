package converter.handlers;

import converter.MarketHandler;
import converter.models.*;
import java.util.*;

public abstract class TotalBaseHandler extends MarketHandler {

    // The selection type ID for "over" markets.
    public static final int OVER_SELECTION_TYPE_ID = 12;

    // The selection type ID for "under" markets.
    public static final int UNDER_SELECTION_TYPE_ID = 13;
    
    @Override
    public Map<String, String> extractSpecifiers(InputMarket market) {
        Map<String, String> specifiers = new HashMap<>();
        
        // Extract total value from first selection
        for (InputSelection selection : market.getSelections()) {
            String totalValue = extractNumericValue(selection.getName());
            if (!totalValue.isEmpty()) {
                specifiers.put("total", totalValue);
                break;
            }
        }
        
        return specifiers;
    }
    
    @Override
    public int getSelectionTypeId(String selectionName) {
        String name = selectionName.toLowerCase().trim();
        
        if (name.startsWith("over")) {
            return OVER_SELECTION_TYPE_ID;
        } else if (name.startsWith("under")) {
            return UNDER_SELECTION_TYPE_ID;
        } else {
            throw new IllegalArgumentException("Unknown total selection: " + selectionName);
        }
    }
}