public class Food extends Element{
    private int nutritive_value;
    private int regeneration_pace;
    private int max_supply;
    private int current_supply;

    public Food(int x, int y, int nutritive_value, int regeneration_pace, int max_supply) {
        super(x, y);
        this.nutritive_value = nutritive_value;
        this.regeneration_pace = regeneration_pace;
        this.max_supply = max_supply;
        this.current_supply = max_supply;
    }

    public void isEaten(int nbSupplyEaten) {
        this.current_supply -= nbSupplyEaten;
        this.current_supply = Math.max(this.current_supply, 0);
    }

    public void flourishes(int nbSupplyCreated) {
        this.current_supply += nbSupplyCreated;
        this.current_supply = Math.min(this.current_supply, this.max_supply);
    }

    public int getNutritiveValue() {
        return this.nutritive_value;
    }
    
    public int getRegenerationPace() {
        return this.regeneration_pace;
    }
    
    public int getSupply() {
        return this.max_supply;
    }    

    public int getCurrentSupply() {
        return this.current_supply;
    }
}
