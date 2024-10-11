import java.util.ArrayList;
import java.util.HashMap;

public class Predator extends Creature{
    private double prey_detection_range;
    private double prey_eating_range;
    private double kill_count;

    public Predator(double x, double y, double energyLevel, String classType, double movingSpeed, double prey_detection_range, double prey_eating_range, double height, double width) {
        super(x, y, energyLevel, classType, movingSpeed, height, width);
        this.prey_detection_range = prey_detection_range;
        this.prey_eating_range = prey_eating_range;
        this.direction = this.getRandomDirection();
        this.direction[0] = this.direction[0] * this.speed;
        this.direction[1] = this.direction[1] * this.speed;
        this.kill_count = 0;
    }

    public void updateDirectionAndMove(ArrayList<Agent> agents) {     
        Agent nearest_agent = findNearestAgent(agents);
        if (nearest_agent != null) {    
            double[] direction_toward_agent = this.getDirectionNormedToward(this.direction, nearest_agent);
            changeDirection(direction_toward_agent);
        } else {
            this.updateRandomDirection();       
        }
        this.moveTowardDirection(this.direction, this.speed);
    }

    public Agent checkForPreyToEatAndEat(ArrayList<Agent> agents) {
        Agent dead_prey = null;
        Agent nearest_agent = findNearestAgent(agents);
        if (nearest_agent != null) {
            double distance = this.distanceToElement(nearest_agent);
            if (distance < this.prey_eating_range) {
                dead_prey = nearest_agent;
                this.modifyEnergyLevel(dead_prey.getEnergyLevel());
                kill_count++;
            }    
        }
        return dead_prey;
    }

    public HashMap<Agent, Double> getNearbyAgents(ArrayList<Agent> agents) {
        HashMap<Agent, Double> agentHashMap = new HashMap<>();
        for (int i = 0; i < agents.size(); i++) {
            double distance = this.distanceToElement(agents.get(i));
            if (distance < this.prey_detection_range) {
                agentHashMap.put(agents.get(i), distance);
            } 
        }
        return agentHashMap;
    }
    
    public Agent findNearestAgent(ArrayList<Agent> agents) {
        HashMap<Agent, Double> agentHashMap = this.getNearbyAgents(agents);
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

    public double getPreyDetectionRange() {
        return prey_detection_range;
    }

    public double getPreyEatingRange() {
        return prey_eating_range;
    }

    public double getKillCount() {
        return kill_count;
    }
}
