package converter.handlers.markets;

import java.util.*;
import converter.MarketHandler;
import converter.models.InputMarket;

public class OneXTwoHandler extends MarketHandler {
    
    // Constants for selection type IDs
    private static final int TEAM_A_SELECTION_TYPE = 1;
    private static final int DRAW_SELECTION_TYPE = 2;
    private static final int TEAM_B_SELECTION_TYPE = 3;
    
    @Override
    public String getMarketTypeId() {
        return "1";
    }
    
    @Override
    public Map<String, String> extractSpecifiers(InputMarket market) {
        return new HashMap<>(); // No specifiers for 1x2 markets
    }
    
    @Override
    public int getSelectionTypeId(String selectionName) {
        String name = selectionName.toLowerCase().trim();
        
        if (name.equals("draw")) {
            return DRAW_SELECTION_TYPE;
        } else if (name.contains("team a")) {
            return TEAM_A_SELECTION_TYPE;
        } else if (name.contains("team b")) {
            return TEAM_B_SELECTION_TYPE;
        } else {
            // For cases where selections don't have explicit team names,
            // we need additional context. This could be enhanced by looking
            // at selection position or using event metadata.
            throw new IllegalArgumentException(
                "Unable to determine selection type for 1x2 market. " +
                "Selection name: '" + selectionName + "'. " +
                "Expected 'draw', 'team a', or 'team b'."
            );
        }
    }
}