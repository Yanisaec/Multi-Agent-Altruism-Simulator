public class Creature extends Element {
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
        this.x = Math.max(this.x, 0);
        this.y = Math.max(this.y, 0);
        this.x = Math.min(this.x, simulation_width);
        this.y = Math.min(this.y, simulation_height);
    }

    public void moveTowardDirection(double[] direction, double speed) {
        direction = this.normDirection(direction);
        double dx = (direction[0] * speed);
        double dy = (direction[1] * speed);
        this.move(dx, dy);
        double distance_to_vertical_sides = Math.min(this.x % this.simulation_width, (this.simulation_width-this.x) % this.simulation_width);
        double distance_to_horizontal_sides = Math.min(this.y % this.simulation_height, (this.simulation_height-this.y) % this.simulation_height);
        if ((distance_to_horizontal_sides <= 0))  {
            this.direction[1] = -this.direction[1];
            // this.direction[0] = -this.direction[0] * 2;
        }
        if ((distance_to_vertical_sides <= 0))  {
            // this.direction[1] = -this.direction[1] * 2;
            this.direction[0] = -this.direction[0];
        } 
    }

    public void changeDirection(double[] new_direction) {
        double direction_norm = this.getDirectionNorm(this.direction);
        new_direction[0] = new_direction[0]*direction_norm;
        new_direction[1] = new_direction[1]*direction_norm;
        this.direction = new_direction;
    }

    public void updateRandomDirection() {
        double[] random_direction = this.getRandomDirection();
        double cosine_similarity = this.cosineSimilarity(this.direction, random_direction) * 0.5;
        this.direction[0] += random_direction[0]*cosine_similarity;
        this.direction[1] += random_direction[1]*cosine_similarity;
        this.direction = this.normDirection(this.direction);
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
