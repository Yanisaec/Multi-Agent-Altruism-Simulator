import java.util.List;
import java.util.ArrayList;

public class Simulation {
    private ArrayList<Agent> aliveAgents;
    private ArrayList<Food> foods;
    private int currentTime;
    
    public Simulation() {
        this.aliveAgents = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.currentTime = 0;
    }

    public void addAgent(int x, int y, int energy, int[] genome, String class_type, double moving_speed, int food_detection_range) {
        this.aliveAgents.add(new Agent(x, y, energy, genome, class_type, moving_speed, food_detection_range));
    }

    public void addFood(int x, int y, int nutritive_value, int regeneration_pace, int max_supply) {
        this.foods.add(new Food(x, y, nutritive_value, regeneration_pace, max_supply));
    }

    public List<Agent> getAliveAgents() {
        return this.aliveAgents;
    }

    public List<Food> getFoods() {
        return this.foods;
    }

    public void incrementTime() {
        this.currentTime++;
    }
}
