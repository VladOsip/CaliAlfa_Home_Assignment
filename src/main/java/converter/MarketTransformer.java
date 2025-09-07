package converter;

import java.util.*;

import converter.handlers.markets.*;
import converter.models.*;

public class MarketTransformer {
    private Map<String, MarketHandler> handlers;
    
    public MarketTransformer() {
        initializeHandlers();
    }
    
    private void initializeHandlers() {
        handlers = new HashMap<>();
        handlers.put("1x2", new OneXTwoHandler());
        handlers.put("total", new TotalHandler());
        handlers.put("1st half - total", new FirstHalfTotalHandler());
        handlers.put("handicap", new HandicapHandler());
        handlers.put("1st half - handicap", new FirstHalfHandicapHandler());
        handlers.put("2nd half - handicap", new SecondHalfHandicapHandler());
        handlers.put("both teams to score", new BothTeamsScoreHandler());
    }
    
    public List<OutputMarket> transform(List<InputMarket> inputMarkets) {
        List<OutputMarket> outputMarkets = new ArrayList<>();
        
        for (InputMarket inputMarket : inputMarkets) {
            MarketHandler handler = getHandler(inputMarket.getName().toLowerCase());
            if (handler != null) {
                OutputMarket outputMarket = handler.handle(inputMarket);
                outputMarkets.add(outputMarket);
            } else {
                throw new IllegalArgumentException("Unknown market type: " + inputMarket.getName());
            }
        }
        
        return outputMarkets;
    }
    
    private MarketHandler getHandler(String marketName) {
        return handlers.get(marketName.toLowerCase());
    }
}