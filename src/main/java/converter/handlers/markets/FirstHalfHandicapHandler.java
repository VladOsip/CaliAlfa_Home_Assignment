package converter.handlers.markets;

import converter.handlers.HandicapBaseHandler;

public class FirstHalfHandicapHandler extends HandicapBaseHandler {

    private static final String FIRST_HALF_HANDICAP = "66";
    
    @Override
    public String getMarketTypeId() {
        return FIRST_HALF_HANDICAP;
    }
}
