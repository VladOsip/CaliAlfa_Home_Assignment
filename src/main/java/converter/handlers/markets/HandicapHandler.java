package converter.handlers.markets;

import converter.handlers.HandicapBaseHandler;

public class HandicapHandler extends HandicapBaseHandler {

    private static final String HANDICAP = "16";
    
    @Override
    public String getMarketTypeId() {
        return HANDICAP;
    }
}
