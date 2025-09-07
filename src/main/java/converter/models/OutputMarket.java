package converter.models;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputMarket {
    @JsonProperty("market_uid")
    private String marketUid;
    
    @JsonProperty("market_type_id")
    private String marketTypeId;
    
    private Map<String, String> specifiers;
    
    private List<OutputSelection> selections;
    
    public OutputMarket() {}
    
    public OutputMarket(String marketUid, String marketTypeId, Map<String, String> specifiers, List<OutputSelection> selections) {
        this.marketUid = marketUid;
        this.marketTypeId = marketTypeId;
        this.specifiers = specifiers;
        this.selections = selections;
    }
    
    public String getMarketUid() {
        return marketUid;
    }
    
    public void setMarketUid(String marketUid) {
        this.marketUid = marketUid;
    }
    
    public String getMarketTypeId() {
        return marketTypeId;
    }
    
    public void setMarketTypeId(String marketTypeId) {
        this.marketTypeId = marketTypeId;
    }
    
    public Map<String, String> getSpecifiers() {
        return specifiers;
    }
    
    public void setSpecifiers(Map<String, String> specifiers) {
        this.specifiers = specifiers;
    }
    
    public List<OutputSelection> getSelections() {
        return selections;
    }
    
    public void setSelections(List<OutputSelection> selections) {
        this.selections = selections;
    }
}