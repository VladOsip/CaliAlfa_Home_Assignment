package converter.handlers;

import converter.MarketHandler;
import converter.models.*;
import java.util.*;

public abstract class HandicapBaseHandler extends MarketHandler {

    public static final int HANDICAP_TEAM_A_TYPE_ID = 1714;
    public static final int HANDICAP_TEAM_B_TYPE_ID = 1715;

    @Override
    public Map<String, String> extractSpecifiers(InputMarket market) {
        Map<String, String> specifiers = new HashMap<>();
        
        // Extract handicap value from first selection
        for (InputSelection selection : market.getSelections()) {
            String hcpValue = extractNumericValue(selection.getName());
            if (!hcpValue.isEmpty()) {
                specifiers.put("hcp", hcpValue);
                break;
            }
        }
        
        return specifiers;
    }
    
    @Override
    public int getSelectionTypeId(String selectionName) {
        String name = selectionName.toLowerCase().trim();
        
        if (name.contains("team a")) {
            return HANDICAP_TEAM_A_TYPE_ID;
        } else if (name.contains("team b")) {
            return HANDICAP_TEAM_B_TYPE_ID;
        } else {
            throw new IllegalArgumentException(
                "Unable to determine handicap team for selection: " + selectionName
            );
        }
    }
}