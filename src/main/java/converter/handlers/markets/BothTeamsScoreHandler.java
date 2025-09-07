package converter.handlers.markets;

public class BothTeamsScoreHandler extends SimpleMarketHandler {

    public static final String MARKET_TYPE_ID = "50";
    public static final int SELECTION_TYPE_ID_YES = 10;
    public static final int SELECTION_TYPE_ID_NO = 11;

    @Override
    public String getMarketTypeId() {
        return MARKET_TYPE_ID;
    }
    
    @Override
    public int getSelectionTypeId(String selectionName) {
        String name = selectionName.toLowerCase().trim();
        
        if (name.equals("yes")) {
            return SELECTION_TYPE_ID_YES;
        } else if (name.equals("no")) {
            return SELECTION_TYPE_ID_NO;
        } else {
            throw new IllegalArgumentException("Unknown both teams to score selection: " + selectionName);
        }
    }
}