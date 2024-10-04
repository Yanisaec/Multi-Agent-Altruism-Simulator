import java.util.ArrayList;

public class Agent extends Creature{
    private int[] genome;
    private int food_detection_range;
    
    public Agent(int x, int y, int energy_level, int[] genome, String class_type, double moving_speed, int food_detection_range) {
        super(x, y, energy_level, class_type, moving_speed);
        this.genome = genome;
        this.food_detection_range = food_detection_range;
    }
    
    public int findNearestFoodSource(ArrayList<Food> food_sources) {
        double min_distance = Double.POSITIVE_INFINITY;
        int best_food_source = -1;
        for (int i = 0; i < food_sources.size(); i++) {
            double distance = this.distanceToElement(food_sources.get(i));
            if ((distance < min_distance) && (distance < this.food_detection_range)) {
                min_distance = distance;
                best_food_source = i;
            }
        }
        return best_food_source;
    }

    public void moveTowardFood(Food food_source) {
        double[] direction = this.directionNormed(food_source);
        this.moveTowardDirection(direction, this.moving_speed);
    }

    public int[] getGenome() {
        return this.genome;
    }

    public int getFoodDetectionRange() {
        return this.food_detection_range;
    }
}