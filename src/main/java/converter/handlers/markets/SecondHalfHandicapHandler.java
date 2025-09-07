package converter.handlers.markets;

import converter.handlers.HandicapBaseHandler;

public class SecondHalfHandicapHandler extends HandicapBaseHandler {

    private static final String SECOND_HALF_HANDICAP = "88";
    
    @Override
    public String getMarketTypeId() {
        return SECOND_HALF_HANDICAP;
    }
}