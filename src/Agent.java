import java.util.ArrayList;
import java.util.HashMap;

public class Agent extends Creature{
    private Genotype genotype;
    private double food_detection_range;
    private double agent_detection_range;
    private double energy_level_to_reproduce;
    private boolean is_eating = false;
    private boolean can_move = true;
    private boolean is_a_pheromone_producer;
    private int age;
    private double reproduction_cost;
    private boolean can_reproduce;
    private double mutation_probability;
    
    public Agent(double x, double y, double energy_level, double energy_to_reproduce, double reproduction_cost, Genotype genotype, String class_type, double speed, double food_detection_range, double agent_detection_range, double mutation_probability, double height, double width) {
        super(x, y, energy_level, class_type, speed, height, width);
        this.direction = this.getRandomDirection();
        this.direction[0] = this.direction[0] * this.speed;
        this.direction[1] = this.direction[1] * this.speed;
        this.genotype = genotype;
        this.food_detection_range = food_detection_range;
        this.agent_detection_range = agent_detection_range;
        this.energy_level_to_reproduce = energy_to_reproduce;
        this.reproduction_cost = reproduction_cost;
        this.age = 0;
        this.is_a_pheromone_producer = genotype.isAProducer();
        this.can_reproduce = false;
        this.mutation_probability = mutation_probability;
    }
    
    public Agent updateDirectionAndEat(ArrayList<Agent> agents, ArrayList<Food> food_sources, ArrayList<Pheromone> pheromones, double simulation_time) {
        this.is_eating = false;
        this.can_move = true;
        Agent potential_child = null;

        Food nearest_food = findNearestFoodSource(food_sources);
        if (!(nearest_food == null)) {
            // There is food nearby
            if (this.distanceToElement(nearest_food) < 5) {
                // The agent is on the food
                this.is_eating = true;
                this.eatFood(nearest_food, simulation_time, agents);
                this.can_move = false;
                return potential_child;
            } else {
                double[] direction_toward_food = this.getDirectionNormedToward(this.direction, nearest_food);
                changeDirection(direction_toward_food);
                return potential_child;
            }
        }
        
        if (canReproduce()){
            Agent nearest_agent = findNearestAgent(agents);
            if (!(nearest_agent == null) && (this.distanceToElement(nearest_agent) < 5)) {
                potential_child = reproduce(nearest_agent);
                this.modifyEnergyLevel(-reproduction_cost);
                nearest_agent.modifyEnergyLevel(-reproduction_cost);
                this.hasReproduced();
                nearest_agent.hasReproduced();
                return potential_child;
            } else if (!(nearest_agent == null)) { // agent not close enough to reproduce
                double[] direction_toward_agent = this.getDirectionNormedToward(this.direction, nearest_agent);
                changeDirection(direction_toward_agent);
                return potential_child;
            }
        }

        Pheromone nearest_pheromone = findNearestPheromone(pheromones);
        if (!(nearest_pheromone == null) && is_a_pheromone_producer) {
            // There is a pheromone nearby and agent can sense them
            double[] direction_toward_pheromone = this.getDirectionNormedToward(this.direction, nearest_pheromone);
            direction_toward_pheromone[0] *= 1.5;
            direction_toward_pheromone[1] *= 1.5;
            changeDirection(direction_toward_pheromone);
            return potential_child;             
        }

        this.updateRandomDirection();
        return potential_child;
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
        Agent nearest_agent = null;
        for (Agent agent : agentHashMap.keySet()) {
            if (agentHashMap.get(agent) < min_distance && agent.canReproduce()) {
                nearest_agent = agent;
                min_distance = agentHashMap.get(agent);
            }
        }
        return nearest_agent;
    }

    public double getNumberOfNearbyEatingAgents(ArrayList<Agent> agents) {
        double number_of_agents_nearby = 0;
        for (int i = 0; i < agents.size(); i++) {
            if (!this.equals(agents.get(i))) {
                double distance = this.distanceToElement(agents.get(i));
                if (distance < 20 && agents.get(i).isEating()) {
                    number_of_agents_nearby += 1;
                }
            }
        }
        return number_of_agents_nearby;
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

    public void eatFood(Food food, double current_time, ArrayList<Agent> agents) {
        double eating_agent_nearby = getNumberOfNearbyEatingAgents(agents);
        double nb_subbly_eaten;
        if (eating_agent_nearby < 2) {
            nb_subbly_eaten = 0;
        } else {
            nb_subbly_eaten = 1;
        }
        double nutritive_value = food.isEaten(nb_subbly_eaten, current_time);
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

    public boolean canReproduce() {
        return this.can_reproduce;
    }

    public boolean isEating() {
        return this.is_eating;
    }

    public boolean canMove() {
        return this.can_move;
    }

    public void updateReproductiveStatus() {
        if (this.energy_level > energy_level_to_reproduce) {
            this.can_reproduce = true;
        } else {
            this.can_reproduce = false;
        }
    }

    public boolean getOlder() {
        this.age++;
        // double energy_to_lose = Math.max((int)((this.age + 10000)/ 10000), 0.5) / 20;
        double energy_to_lose = 0.05;
        this.modifyEnergyLevel(-energy_to_lose);
        if (this.energy_level <= 0) {
            return true;
        } 
        return false;
    }

    public void hasReproduced() {
        this.can_reproduce = false;
    }

    public Agent reproduce(Agent other_parent) {
        Genotype child_genotype = genotype.getChildCoupleChildrenGenotype(this.getGenotype(), other_parent.getGenotype(), mutation_probability);
        Agent child = new Agent(this.x, this.y, this.base_energy_level, this.energy_level_to_reproduce, this.reproduction_cost, child_genotype, this.class_type, this.speed, this.food_detection_range, this.agent_detection_range, this.mutation_probability, this.simulation_height, this.simulation_width);
        return child;
    }

    public boolean spreadPheromone(double current_time, ArrayList<Pheromone> pheromones) {
        Pheromone is_there_pheromone_nearby = findNearestPheromone(pheromones);
        if (is_there_pheromone_nearby == null) {
            return is_a_pheromone_producer;
        } 
        return false;
    }

    public double getSpreadPheromoneProba() {
        return genotype.getSpreadPheromoneProba();
    }

    public boolean isAProducer() {
        return is_a_pheromone_producer;
    }
}