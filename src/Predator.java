import java.util.ArrayList;
import java.util.HashMap;

public class Predator extends Creature{
    private double prey_detection_range;
    private double prey_eating_range;
    private double energy_level_to_look_for_prey;
    private double predator_detection_range;
    private double reproduction_cost;
    private boolean can_reproduce;
    private double energy_level_to_reproduce;
    private double agent_nutritive_value;
    private double kill_count;
    private double goes_back_to_hunting = 500;

    public Predator(double x, double y, double energyLevel, double predator_energy_level_to_reproduce, double predator_reproduction_cost, String classType, double movingSpeed, double predator_detection_range, double prey_detection_range, double prey_eating_range, double energy_level_to_look_for_prey, double agent_nutritive_value, double height, double width) {
        super(x, y, energyLevel, classType, movingSpeed, height, width);
        this.prey_detection_range = prey_detection_range;
        this.predator_detection_range = predator_detection_range;
        this.prey_eating_range = prey_eating_range;
        this.energy_level_to_look_for_prey = energy_level_to_look_for_prey;
        this.agent_nutritive_value = agent_nutritive_value;
        this.reproduction_cost = predator_reproduction_cost;
        this.energy_level_to_reproduce = predator_energy_level_to_reproduce;
        this.direction = this.getRandomDirection();
        this.direction[0] = this.direction[0] * this.speed;
        this.direction[1] = this.direction[1] * this.speed;
        this.kill_count = 0;
        this.can_reproduce = false;
    }

    public Predator updateDirectionAndMove(ArrayList<Agent> agents, ArrayList<Predator> predators) {  
        Predator potential_child = null;
        if (can_reproduce){
            Predator nearest_predator = findNearestPredator(predators);
            if (!(nearest_predator == null) && (this.distanceToElement(nearest_predator) < 5)) {
                potential_child = reproduce();
                this.modifyEnergyLevel(-reproduction_cost);
                nearest_predator.modifyEnergyLevel(-reproduction_cost);
                this.updateReproductiveStatus();
                nearest_predator.updateReproductiveStatus();
                return potential_child;
            } else if (!(nearest_predator == null)) { // agent not close enough to reproduce
                double[] direction_toward_predator = this.getDirectionNormedToward(this.direction, nearest_predator);
                changeDirection(direction_toward_predator);
                this.moveTowardDirection(this.direction, this.speed);
                return potential_child;
            } else {
                this.updateRandomDirection();       
                this.moveTowardDirection(this.direction, this.speed);
                return potential_child;
            }
        }

        Agent nearest_agent = findNearestAgent(agents);
            if (nearest_agent != null && needToHunt()) {    
                double[] direction_toward_agent = this.getDirectionNormedToward(this.direction, nearest_agent);
                changeDirection(direction_toward_agent);
            } else {
                this.updateRandomDirection();       
            }
        this.moveTowardDirection(this.direction, this.speed);
        return potential_child;
    }

    public Agent checkForPreyToEatAndEat(ArrayList<Agent> agents) {
        Agent dead_prey = null;
        Agent nearest_agent = findNearestAgent(agents);
        int nb_agents_nearby = getNumberOfAgentsNearby(agents);
        if (nearest_agent != null && needToHunt()) {
            double distance = this.distanceToElement(nearest_agent);
            if (distance < this.prey_eating_range) {
                dead_prey = nearest_agent;
                this.modifyEnergyLevel(nearest_agent.getEnergyLevel()/4);
                // this.modifyEnergyLevel(agent_nutritive_value);
                kill_count++;
            }    
        }
        return dead_prey;
    }

    public HashMap<Agent, Double> getNearbyAgents(ArrayList<Agent> agents, double threshold) {
        HashMap<Agent, Double> agentHashMap = new HashMap<>();
        for (int i = 0; i < agents.size(); i++) {
            double distance = this.distanceToElement(agents.get(i));
            if (distance < threshold) {
                agentHashMap.put(agents.get(i), distance);
            } 
        }
        return agentHashMap;
    }    
    public HashMap<Predator, Double> getNearbyPredators(ArrayList<Predator> predators, double threshold) {
        HashMap<Predator, Double> predatorHashMap = new HashMap<>();
        for (Predator predator : predators) {
            if (!this.equals(predator)){
                double distance = this.distanceToElement(predator);
                if (distance < threshold) {
                    predatorHashMap.put(predator, distance);
                }
            } 
        }
        return predatorHashMap;
    }

    public int getNumberOfAgentsNearby(ArrayList<Agent> agents) {
        HashMap<Agent, Double> agentHashMap = getNearbyAgents(agents, 50);
        return agentHashMap.size();
    }
    
    public Agent findNearestAgent(ArrayList<Agent> agents) {
        HashMap<Agent, Double> agentHashMap = this.getNearbyAgents(agents, this.prey_detection_range);
        double min_distance = Double.POSITIVE_INFINITY;
        Agent nearest_agent = null;
        for (Agent agent : agentHashMap.keySet()) {
            if (agentHashMap.get(agent) < min_distance) {
                nearest_agent = agent;
                min_distance = agentHashMap.get(agent);
            }
        }
        return nearest_agent;
    }    

    public Predator findNearestPredator(ArrayList<Predator> predators) {
        HashMap<Predator, Double> predatorHashMap = this.getNearbyPredators(predators, this.predator_detection_range);
        double min_distance = Double.POSITIVE_INFINITY;
        Predator nearest_predator = null;
        for (Predator predator : predatorHashMap.keySet()) {
            if (predatorHashMap.get(predator) < min_distance) {
                nearest_predator = predator;
                min_distance = predatorHashMap.get(predator);
            }
        }
        return nearest_predator;
    }

    public double getPreyDetectionRange() {
        return prey_detection_range;
    }

    public double getPreyEatingRange() {
        return prey_eating_range;
    }

    public double getKillCount() {
        return kill_count;
    }

    public boolean getOlder() {
        // double energy_to_lose = Math.max((int)((this.age + 10000)/ 10000), 0.5) / 20;
        double energy_to_lose = 0.2;
        this.modifyEnergyLevel(-energy_to_lose);
        if (this.energy_level <= 0) {
            return true;
        } 
        return false;
    }

    public boolean needToHunt() {
        if (this.energy_level < energy_level_to_look_for_prey) {
            return true;
        } 
        return false;
    }

    public void updateReproductiveStatus() {
        if (!can_reproduce || this.energy_level < goes_back_to_hunting){
            if (this.energy_level > energy_level_to_reproduce) {
                this.can_reproduce = true;
            } else {
                this.can_reproduce = false;
            }
        }
    }

    public Predator reproduce() {
        Predator child = new Predator(this.getX(), this.getY(), this.base_energy_level, energy_level_to_reproduce, reproduction_cost, this.class_type, this.speed, predator_detection_range, prey_detection_range, prey_eating_range, energy_level_to_look_for_prey, agent_nutritive_value, this.simulation_height, this.simulation_width);
        return child;
    }
}
