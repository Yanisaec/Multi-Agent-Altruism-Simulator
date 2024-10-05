import java.util.ArrayList;
import java.util.HashMap;

public class Agent extends Creature{
    private Genotype genotype;
    private int food_detection_range;
    private int agent_detection_range;
    
    public Agent(int x, int y, int energy_level, int[] allele1, int[] allele2, String class_type, double moving_speed, int food_detection_range, int agent_detection_range) {
        super(x, y, energy_level, class_type, moving_speed);
        this.genotype = new Genotype(allele1, allele2);
        this.food_detection_range = food_detection_range;
    }

    public HashMap<Agent, Double> getNearbyAgents(ArrayList<Agent> agents) {
        HashMap<Agent, Double> agentHashMap = new HashMap<>();
        for (int i = 0; i < agents.size(); i++) {
            if (!this.equals(agents.get(i))) {
                double distance = this.distanceToElement(agents.get(i));
                if (distance < this.agent_detection_range) {
                    agentHashMap.put(agents.get(i), distance);
                }
            }
        }
        return agentHashMap;
    }
    
    public HashMap<Food, Double> getNearbyFoods(ArrayList<Food> food_sources) {
        HashMap<Food, Double> foodHashMap = new HashMap<>();
        for (int i = 0; i < food_sources.size(); i++) {
            double distance = this.distanceToElement(food_sources.get(i));
            if (distance < this.food_detection_range) {
                foodHashMap.put(food_sources.get(i), distance);
            }
        }
        return foodHashMap;
    }

    public HashMap<Pheromone, Double> getNearbyPheromones(ArrayList<Pheromone> pheromones) {
        HashMap<Pheromone, Double> pheromoneHashMap = new HashMap<>();
        for (int i = 0; i < pheromones.size(); i++) {
            Pheromone pheromone = pheromones.get(i);
            double distance = this.distanceToElement(pheromone);
            if (distance < pheromone.getRadiusEffect()) {
                pheromoneHashMap.put(pheromone, distance);
            }
        }
        return pheromoneHashMap;
    }
    
    public Agent findNearestAgent(ArrayList<Agent> agents) {
        HashMap<Agent, Double> agentHashMap = this.getNearbyAgents(agents);
        double min_distance = Double.POSITIVE_INFINITY;
        Agent best_agent = null;
        for (Agent agent : agentHashMap.keySet()) {
            if (agentHashMap.get(agent) < min_distance) {
                best_agent = agent;
            }
        }
        return best_agent;
    }
        
    public Food findNearestFoodSource(ArrayList<Food> food_sources) {
        HashMap<Food, Double> foodHashMap = this.getNearbyFoods(food_sources);
        double min_distance = Double.POSITIVE_INFINITY;
        Food best_food_source = null;
        for (Food food : foodHashMap.keySet()) {
            if (foodHashMap.get(food) < min_distance) {
                best_food_source = food;
            }
        }
        return best_food_source;
    }
        
    public Pheromone findNearestPheromone(ArrayList<Pheromone> pheromones) {
        HashMap<Pheromone, Double> pheromoneHashMap = this.getNearbyPheromones(pheromones);
        double min_distance = Double.POSITIVE_INFINITY;
        Pheromone best_pheronome = null;
        for (Pheromone pheromone : pheromoneHashMap.keySet()) {
            if (pheromoneHashMap.get(pheromone) < min_distance) {
                best_pheronome = pheromone;
            }
        }
        return best_pheronome;
    }

    public void moveTowardFood(Food food_source) {
        double[] direction = this.directionNormed(food_source);
        this.moveTowardDirection(direction, this.moving_speed);
    }

    public Genotype getGenotype() {
        return this.genotype;
    }

    public int getFoodDetectionRange() {
        return this.food_detection_range;
    }
}