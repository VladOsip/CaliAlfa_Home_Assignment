package converter.handlers.markets;

import converter.handlers.TotalBaseHandler;

public class FirstHalfTotalHandler extends TotalBaseHandler {
    
    private static final String FIRST_HALF_TOTAL = "68";

    @Override
    public String getMarketTypeId() {
        return FIRST_HALF_TOTAL;
    }
}
