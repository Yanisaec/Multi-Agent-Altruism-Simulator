import java.util.List;
import java.util.ArrayList;

public class Simulation {
    private ArrayList<Agent> aliveAgents;
    private ArrayList<Food> foods;
    private ArrayList<Pheromone> pheromones;
    private int currentTime;
    
    public Simulation() {
        this.aliveAgents = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.pheromones = new ArrayList<>();
        this.currentTime = 0;
    }

    public void addAgent(double x, double y, double energy, int[] allele1, int[] allele2, String class_type, double moving_speed, double food_detection_range, double agent_detection_range) {
        this.aliveAgents.add(new Agent(x, y, energy, allele1, allele2, class_type, moving_speed, food_detection_range, agent_detection_range));
    }

    public void removeAgent(Agent agent) {
        this.aliveAgents.remove(agent);
    }

    public void addFood(double x, double y, double nutritive_value, double regeneration_pace, double max_supply) {
        this.foods.add(new Food(x, y, nutritive_value, regeneration_pace, max_supply));
    }

    public ArrayList<Agent> getAliveAgents() {
        return this.aliveAgents;
    }

    public ArrayList<Food> getFoods() {
        return this.foods;
    }

    public ArrayList<Pheromone> getPheromones() {
        return this.pheromones;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public void incrementTime() {
        this.currentTime++;
    }
}
