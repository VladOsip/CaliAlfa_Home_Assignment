package converter.models;

public class InputSelection {
    private String name;
    private double odds;
    
    public InputSelection() {}
    
    public InputSelection(String name, double odds) {
        this.name = name;
        this.odds = odds;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getOdds() {
        return odds;
    }
    
    public void setOdds(double odds) {
        this.odds = odds;
    }
}