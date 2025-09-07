package converter.handlers.markets;

import converter.MarketHandler;
import converter.models.*;
import java.util.*;

public abstract class SimpleMarketHandler extends MarketHandler {
    
    @Override
    public Map<String, String> extractSpecifiers(InputMarket market) {
        return new HashMap<>(); // No specifiers for simple markets
    }
}