import java.util.ArrayList;
import java.util.HashMap;

public class Agent extends Creature{
    private Genotype genotype;
    private double food_detection_range;
    private double agent_detection_range;
    private double time_between_change_of_direction_random_walk = 100;
    private double last_direction_change_time;
    
    public Agent(double x, double y, double energy_level, int[] allele1, int[] allele2, String class_type, double speed, double food_detection_range, double agent_detection_range) {
        super(x, y, energy_level, class_type, speed);
        this.genotype = new Genotype(allele1, allele2);
        this.food_detection_range = food_detection_range;
        this.agent_detection_range = agent_detection_range;
        this.last_direction_change_time = 0;
    }
    
    public void updateDirectionAndEat(ArrayList<Agent> agents, ArrayList<Food> food_sources, ArrayList<Pheromone> pheromones, double simulation_time, double height, double width) {
        Food nearest_food = findNearestFoodSource(food_sources);
        if (nearest_food == null) {
            Pheromone nearest_pheromone = findNearestPheromone(pheromones);
            if (nearest_pheromone == null) {
                // No food, no pheromone, random walk
                if ((simulation_time - this.last_direction_change_time) >= this.time_between_change_of_direction_random_walk) {
                    this.direction = this.getRandomDirection(height, width);
                    this.last_direction_change_time = simulation_time;
                }
            } else {
                // There is a pheromone nearby
                this.direction = this.getDirectionNormedToward(nearest_pheromone);
                this.last_direction_change_time = simulation_time;
            }
        } else {
            // There is food nearby
            if (this.distanceToElement(nearest_food) < 5) {
                // The agent is on the food
                this.eatFood(nearest_food, simulation_time);
                this.direction = new double[]{0, 0};
                this.last_direction_change_time = simulation_time;
            } else {
                this.direction = this.getDirectionNormedToward(nearest_food);this.direction = this.getDirectionNormedToward(nearest_food);
                this.last_direction_change_time = simulation_time;
            }
        }
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
            Food food = food_sources.get(i);
            double distance = this.distanceToElement(food);
            if ((distance < this.food_detection_range) && !food.isEmpty()) {
                foodHashMap.put(food, distance);
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
                min_distance = agentHashMap.get(agent);
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
                min_distance = foodHashMap.get(food);
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
                min_distance = pheromoneHashMap.get(pheromone);
            }
        }
        return best_pheronome;
    }

    public void eatFood(Food food, double current_time) {
        double nutritive_value = food.isEaten(1, current_time);
        this.modifyEnergyLevel(nutritive_value);
    }

    public Genotype getGenotype() {
        return this.genotype;
    }

    public double getFoodDetectionRange() {
        return this.food_detection_range;
    }
    
    public double getAgentDetectionRange() {
        return this.agent_detection_range;
    }
}