package converter.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputSelection {
    @JsonProperty("selection_uid")
    private String selectionUid;
    
    @JsonProperty("selection_type_id")
    private int selectionTypeId;
    
    @JsonProperty("decimal_odds")
    private double decimalOdds;
    
    public OutputSelection() {}
    
    public OutputSelection(String selectionUid, int selectionTypeId, double decimalOdds) {
        this.selectionUid = selectionUid;
        this.selectionTypeId = selectionTypeId;
        this.decimalOdds = decimalOdds;
    }
    
    public String getSelectionUid() {
        return selectionUid;
    }
    
    public void setSelectionUid(String selectionUid) {
        this.selectionUid = selectionUid;
    }
    
    public int getSelectionTypeId() {
        return selectionTypeId;
    }
    
    public void setSelectionTypeId(int selectionTypeId) {
        this.selectionTypeId = selectionTypeId;
    }
    
    public double getDecimalOdds() {
        return decimalOdds;
    }
    
    public void setDecimalOdds(double decimalOdds) {
        this.decimalOdds = decimalOdds;
    }
}