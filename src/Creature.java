public class Creature extends Element{
    protected double energy_level;
    protected double base_energy_level;
    protected String class_type;
    protected double speed;
    protected double[] direction;

    public Creature(double x, double y, double energy_level, String class_type, double speed, double height, double width){
        super(x,y,height,width);
        this.energy_level = energy_level;
        this.base_energy_level = energy_level;
        this.class_type = class_type;
        this.speed = speed;
        this.direction = new double[]{0.0, 0.0};
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void moveTowardDirection(double[] direction, double speed) {
        direction = this.normDirection(direction);
        double dx = (direction[0] * speed);
        double dy = (direction[1] * speed);
        this.move(dx, dy);
        double distance_to_vertical_sides = Math.min(this.x % this.simulation_width, (this.simulation_width-this.x) % this.simulation_width);
        double distance_to_horizontal_sides = Math.min(this.y % this.simulation_height, (this.simulation_height-this.y) % this.simulation_height);
        if ((distance_to_horizontal_sides < 10))  {
            this.direction[1] = -this.direction[1] * 2;
            // this.direction[0] = -this.direction[0] * 2;
        }
        if ((distance_to_vertical_sides < 10))  {
            // this.direction[1] = -this.direction[1] * 2;
            this.direction[0] = -this.direction[0] * 2;
        } 
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
