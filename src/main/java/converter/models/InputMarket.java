package converter.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InputMarket {
    private String name;
    
    @JsonProperty("event_id")
    private String eventId;
    
    private List<InputSelection> selections;
    
    public InputMarket() {}
    
    public InputMarket(String name, String eventId, List<InputSelection> selections) {
        this.name = name;
        this.eventId = eventId;
        this.selections = selections;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    public List<InputSelection> getSelections() {
        return selections;
    }
    
    public void setSelections(List<InputSelection> selections) {
        this.selections = selections;
    }
}