public class Food extends Element{
    private double nutritive_value;
    private double regeneration_pace;
    private double max_supply;
    private double current_supply;
    private boolean is_empty;
    private double when_became_empty;

    public Food(double x, double y, double nutritive_value, double regeneration_pace, double max_supply, double height, double width) {
        super(x, y, height, width);
        this.nutritive_value = nutritive_value;
        this.regeneration_pace = regeneration_pace;
        this.max_supply = max_supply;
        this.current_supply = max_supply;
        this.is_empty = false;
        this.when_became_empty = 0;
    }

    public double isEaten(double nbSupplyEaten, double current_time) {
        double supplies_eaten = Math.min(this.current_supply, nbSupplyEaten);
        this.current_supply -= nbSupplyEaten;
        this.current_supply = Math.max(this.current_supply, 0);
        if (this.current_supply == 0) {
            this.is_empty = true;
            this.when_became_empty = current_time;
        }
        return supplies_eaten * this.nutritive_value;
    }

    public void flourishes(double nbSupplyCreated) {
        this.current_supply += nbSupplyCreated;
        this.current_supply = Math.min(this.current_supply, this.max_supply);
    }

    public double getNutritiveValue() {
        return this.nutritive_value;
    }
    
    public double getRegenerationPace() {
        return this.regeneration_pace;
    }
    
    public double getMaxSupply() {
        return this.max_supply;
    }    

    public double getCurrentSupply() {
        return this.current_supply;
    }

    public boolean isEmpty() {
        return this.is_empty;
    }

    public void setEmptiness(boolean empty_or_not) {
        this.is_empty = empty_or_not;
    }

    public double getWhenEmpty() {
        return this.when_became_empty;
    }
}
