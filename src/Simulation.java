import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Simulation {
    private ArrayList<Agent> aliveAgents;
    private ArrayList<Food> foods;
    private ArrayList<Pheromone> pheromones;
    private int currentTime;
    private Random random = new Random();
    private double simulation_height;
    private double simulation_width;
    private Config config;
    
    public Simulation(Config config, double height, double width) {
        this.aliveAgents = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.pheromones = new ArrayList<>();
        this.currentTime = 0;
        this.simulation_height = height;
        this.simulation_width = width;
        this.config = config;
    }

    public void updateSimulation() {
        List<Agent> agentsToAdd = new ArrayList<>();
        List<Agent> agentsToRemove = new ArrayList<>();
        List<Pheromone> pheromonesToAdd = new ArrayList<>();
        List<Pheromone> pheromonesToRemove = new ArrayList<>();

        for (Agent agent : aliveAgents) {
            agent.updateReproductiveStatus();
        }

        for (Agent agent : aliveAgents) {
            Agent potential_child = agent.updateDirectionAndEat(aliveAgents, foods, pheromones, currentTime);
            if (!(potential_child == null)) {
                agentsToAdd.add(potential_child);
            }

            if (agent.canMove()) {
                agent.moveTowardDirection(agent.getDirection(), agent.getSpeed());
            }   

            boolean isDead = agent.getOlder();
            if (isDead) {
                agentsToRemove.add(agent);
            }

            // Agent potential_child = agent.reproduce(config.getEnergyLevelToReproduce(), config.getReproductionCost(), config.getMutationProbability());
            // if (!(potential_child == null)) {
            //     agentsToAdd.add(potential_child);
            // }
            if (agent.isEating()) {
                if (agent.spreadPheromone(currentTime, pheromones)) {
                    Pheromone new_pheromone = new Pheromone(agent.getX(), agent.getY(), config.getPheromoneLifeSpan(), config.getPheromoneRadius(), this.simulation_height, this.simulation_width);
                    pheromonesToAdd.add(new_pheromone);
                    // agent.updateLastPheromone(currentTime);
                    agent.modifyEnergyLevel(-config.getPheromoneEnergyCost());
                }
            }
        }

        for (Pheromone pheromone : pheromones) {
            boolean is_done = pheromone.update();
            if (is_done) {
                pheromonesToRemove.add(pheromone);
            }
        }

        // Remove dead agents after the iteration
        for (Agent deadAgent : agentsToRemove) {
            removeAgent(deadAgent);
        }

        for (Agent newAgent : agentsToAdd) {
            addAgentAgent(newAgent);
        }
        
        for (Pheromone new_pheromone : pheromonesToAdd) {
            addPheromonePheromone(new_pheromone);
        }

        for (Pheromone donePheromone : pheromonesToRemove) {
            removePheromone(donePheromone);
        }
        
        for (Food food : foods) {
            if (food.isEmpty() && ((currentTime - food.getWhenEmpty()) > config.getTimeBeforeFoodRespawn())) {
                food.flourishes(food.getMaxSupply());
                food.setEmptiness(false);
            }
        }

        incrementTime();
    }

    public void addAgent(double x, double y, double energy, double energy_to_reproduce, int[] allele1, int[] allele2, String class_type, double moving_speed, double food_detection_range, double agent_detection_range) {
        this.aliveAgents.add(new Agent(x, y, energy, energy_to_reproduce, allele1, allele2, class_type, moving_speed, food_detection_range, agent_detection_range, config.getMutationProbability(), simulation_height, simulation_width));
    }

    public void addAgentAgent(Agent new_agent) {
        this.aliveAgents.add(new_agent);
    }

    public void removeAgent(Agent agent) {
        this.aliveAgents.remove(agent);
    }

    public void addFood(double x, double y, double nutritive_value, double regeneration_pace, double max_supply) {
        this.foods.add(new Food(x, y, nutritive_value, regeneration_pace, max_supply, simulation_height, simulation_width));
    }

    public void addPheromonePheromone(Pheromone new_pheromone) {
        this.pheromones.add(new_pheromone);
    }

    public void removePheromone(Pheromone pheromone) {
        this.pheromones.remove(pheromone);
    }

    public void addRandomAgent() {
        int allele_length = config.getAlleleLength();
        int[] allele1 = new int[allele_length];
        int[] allele2 = new int[allele_length];
        for (int i = 0; i < allele_length; i++) {
            allele1[i] = random.nextInt(2);
            allele2[i] = random.nextInt(2);
        }
        addAgent(Math.random()*simulation_width, Math.random()*simulation_height, config.getAgentBaseEnergyLevel(), config.getEnergyLevelToReproduce(), allele1, allele2, "Agent1", config.getMovingSpeed(), config.getFoodDetectionRange(), config.getAgentDetectionRange());
    }
    
    public void addAltruisticAgent() {
        int allele_length = config.getAlleleLength();
        int[] allele1 = new int[allele_length];
        int[] allele2 = new int[allele_length];
        for (int i = 0; i < allele_length; i++) {
            allele1[i] = 1;
            allele2[i] = 1;
        }
        addAgent(Math.random()*simulation_width, Math.random()*simulation_height, config.getAgentBaseEnergyLevel(), config.getEnergyLevelToReproduce(), allele1, allele2, "Agent1", config.getMovingSpeed(), config.getFoodDetectionRange(), config.getAgentDetectionRange());
    }
    
    public void addEgoisticAgent() {
        int allele_length = config.getAlleleLength();
        int[] allele1 = new int[allele_length];
        int[] allele2 = new int[allele_length];
        for (int i = 0; i < allele_length; i++) {
            allele1[i] = 0;
            allele2[i] = 0;
        }
        addAgent(Math.random()*simulation_width, Math.random()*simulation_height, config.getAgentBaseEnergyLevel(), config.getEnergyLevelToReproduce(), allele1, allele2, "Agent1", config.getMovingSpeed(), config.getFoodDetectionRange(), config.getAgentDetectionRange());
    }

    public void addRandomFood() {
        addFood(Math.random()*simulation_width, Math.random()*simulation_height, config.getFoodNutritiveValue(), 1, config.getFoodMaxSupply());
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

    public double getNumberAgents() {
        double number_of_agents = aliveAgents.size();
        return number_of_agents;
    }

    public double getAverageSpreadProba() {
        List<Agent> agents = getAliveAgents();
        double number_of_agents = getNumberAgents();
        
        double sum_spread_probas = 0;
        for (Agent agent : agents) {
            sum_spread_probas += agent.getSpreadProba();
        }
        double average_spread_proba = sum_spread_probas / number_of_agents;
        return average_spread_proba;
    }

    public double getPartOfAltruists() {
        List<Agent> agents = getAliveAgents();
        double number_of_agents = getNumberAgents();
        
        double part_of_altruists = 0;
        for (Agent agent : agents) {
            if (agent.isAProducer()) {
                part_of_altruists += 1;
            }
        }
        double final_part = part_of_altruists / number_of_agents;
        return final_part;
    }

    public void incrementTime() {
        this.currentTime++;
    }
}
