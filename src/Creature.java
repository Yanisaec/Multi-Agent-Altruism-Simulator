public class Creature extends Element{
    protected int energy_level;
    protected String class_type;
    protected double moving_speed;

    public Creature(int x, int y, int energy_level, String class_type, double moving_speed){
        super(x,y);
        this.energy_level = energy_level;
        this.class_type = class_type;
        this.moving_speed = moving_speed;
    }
    
    public void modifyEnergyLevel(int energyDelta) {
        this.energy_level += energyDelta;
    }
    
    public int getEnergyLevel() {
        return this.energy_level;
    }

    public String getClassType() {
        return this.class_type;
    }

    public double getMovingSpeed() {
        return this.moving_speed;
    }
}
