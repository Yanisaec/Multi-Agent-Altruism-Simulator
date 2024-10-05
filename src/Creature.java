public class Creature extends Element{
    protected double energy_level;
    protected String class_type;
    protected double speed;
    protected double[] direction;

    public Creature(double x, double y, double energy_level, String class_type, double speed){
        super(x,y);
        this.energy_level = energy_level;
        this.class_type = class_type;
        this.speed = speed;
        this.direction = new double[]{0.0, 0.0};
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void moveTowardDirection(double[] direction, double speed) {
        double dx = (direction[0] * speed);
        double dy = (direction[1] * speed);
        this.move(dx, dy);
    }
    
    public boolean modifyEnergyLevel(double energyDelta) {
        this.energy_level += energyDelta;
        if (this.energy_level <= 0) {
            return true;
        }
        return false;
    }
    
    public double getEnergyLevel() {
        return this.energy_level;
    }

    public String getClassType() {
        return this.class_type;
    }

    public double getSpeed() {
        return this.speed;
    }

    public double[] getDirection() {
        return this.direction;
    }
}
