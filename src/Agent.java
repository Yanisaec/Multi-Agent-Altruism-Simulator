import java.util.ArrayList;
import java.util.HashMap;

public class Agent extends Creature{
    private Genotype genotype;
    private double food_detection_range;
    private double agent_detection_range;
    private double time_between_change_of_direction_random_walk = 500;
    private double last_direction_change_time;
    private boolean is_eating = false;
    private double last_time_spread_pheromone; 
    private int age;
    
    public Agent(double x, double y, double energy_level, int[] allele1, int[] allele2, String class_type, double speed, double food_detection_range, double agent_detection_range) {
        super(x, y, energy_level, class_type, speed);
        this.genotype = new Genotype(allele1, allele2);
        this.food_detection_range = food_detection_range;
        this.agent_detection_range = agent_detection_range;
        this.last_direction_change_time = -1001;
        this.last_time_spread_pheromone = -301;
        this.age = 0;
    }
    
    public void updateDirectionAndEat(ArrayList<Agent> agents, ArrayList<Food> food_sources, ArrayList<Pheromone> pheromones, double simulation_time, double height, double width) {
        Food nearest_food = findNearestFoodSource(food_sources);
        this.is_eating = false;
        if (nearest_food == null) {
            Pheromone nearest_pheromone = findNearestPheromone(pheromones);
            if (nearest_pheromone == null) {
                // No food, no pheromone, random walk
                if ((simulation_time - this.last_direction_change_time) >= this.time_between_change_of_direction_random_walk) {
                    this.direction = this.getRandomDirectionCanva(height, width);
                    // this.direction = this.getRandomDirection();
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
                this.is_eating = true;
                this.direction = new double[]{0, 0};
                this.last_direction_change_time = simulation_time - this.time_between_change_of_direction_random_walk;
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

    public int getAge() {
        return this.age;
    }

    public boolean isEating() {
        return this.is_eating;
    }

    public boolean getOlder() {
        this.age++;
        double energy_to_lose = Math.max((int)((this.age + 10000)/ 10000), 0.5) / 10;
        this.modifyEnergyLevel(-energy_to_lose);
        if (this.energy_level <= 0) {
            return true;
        } 
        return false;
    }

    public Agent reproduce(double energy_level_required, double reproduction_cost, double mutation_probability) {
        Agent child = null;
        if (this.energy_level >= energy_level_required) {
            ArrayList<int[]> genotype = this.genotype.getChildGenotype(mutation_probability);
            child = new Agent(this.x, this.y, this.base_energy_level, genotype.get(0), genotype.get(1), this.class_type, this.speed, this.food_detection_range, this.agent_detection_range);
            this.modifyEnergyLevel(-reproduction_cost);
        }
        return child;
    }

    public boolean spreadPheromone(double current_time) {
        if ((current_time - last_time_spread_pheromone) > 250) {
            boolean spread_or_not = genotype.spreadOrNot();
            if (spread_or_not) {
                last_time_spread_pheromone = current_time;
                return true;
            }
        }
        return false;
    }

    public double getSpreadProba() {
        return genotype.getSpreadProba();
    }
}