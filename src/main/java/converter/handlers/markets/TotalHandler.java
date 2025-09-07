package converter.handlers.markets;

import converter.handlers.TotalBaseHandler;

public class TotalHandler extends TotalBaseHandler {
    
    private static final String TOTAL = "18";
    @Override
    public String getMarketTypeId() {
        return TOTAL;
    }
}
