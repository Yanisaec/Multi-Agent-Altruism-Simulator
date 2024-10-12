import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Simulation {
    private ArrayList<Agent> aliveAgents;
    private ArrayList<Predator> predators;
    private ArrayList<Food> foods;
    private ArrayList<Pheromone> pheromones;
    private ArrayList<Repelant> repelants;
    private int currentTime;
    private Random random = new Random();
    private double simulation_height;
    private double simulation_width;
    private Config config;
    
    public Simulation(Config config, double height, double width) {
        this.aliveAgents = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.pheromones = new ArrayList<>();
        this.predators = new ArrayList<>();
        this.repelants = new ArrayList<>();
        this.currentTime = 0;
        this.simulation_height = height;
        this.simulation_width = width;
        this.config = config;
    }

    public void updateSimulation() {
        List<Agent> agentsToAdd = new ArrayList<>();
        List<Agent> agentsToRemove = new ArrayList<>();
        List<Predator> predators_to_add = new ArrayList<>();
        List<Predator> predatorsToRemove = new ArrayList<>();
        List<Pheromone> pheromonesToAdd = new ArrayList<>();
        List<Pheromone> pheromonesToRemove = new ArrayList<>();       
        List<Repelant> repelantsToAdd = new ArrayList<>();
        List<Repelant> repelantsToRemove = new ArrayList<>();

        for (Predator predator : predators) {
            boolean is_dead = predator.getOlder();
            if (is_dead) {
                predatorsToRemove.add(predator);
            }
        }

        for (Predator predator : predatorsToRemove) {
            predators.remove(predator);
        }

        for (Predator predator : predators) {
            predator.updateReproductiveStatus();
        }

        for (Predator predator : predators) {
            Predator potential_child = predator.updateDirectionAndMove(aliveAgents, predators);
            if (potential_child != null) {
                predators_to_add.add(potential_child);
            }
            Agent potential_prey = predator.checkForPreyToEatAndEat(aliveAgents);
            if (potential_prey != null) {
                this.aliveAgents.remove(potential_prey);
            }
        }

        for (Predator new_predator : predators_to_add) {
            predators.add(new_predator);
        }

        for (Agent agent : aliveAgents) {
            agent.updateReproductiveStatus();
        }

        for (Agent agent : aliveAgents) {
            Agent potential_child = agent.updateDirectionAndEat(aliveAgents, foods, pheromones, predators, currentTime);
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

            if (agent.isEating()) {
                if (agent.spreadPheromone(currentTime, pheromones)) {
                    Pheromone new_pheromone = new Pheromone(agent.getX(), agent.getY(), config.getPheromoneLifeSpan(), config.getPheromoneRadius(), this.simulation_height, this.simulation_width);
                    pheromonesToAdd.add(new_pheromone);
                    agent.modifyEnergyLevel(-config.getPheromoneEnergyCost());
                }
            }

            if (agent.isInDanger()) {
                Repelant new_repelant = new Repelant(agent.getX(), agent.getY(), config.getRepelantLifespan(), config.getRepelantRadius(),simulation_height, simulation_width);
                repelantsToAdd.add(new_repelant);
                agent.modifyEnergyLevel(-config.getRepelantEnergyCost());
            }
        }

        for (Pheromone pheromone : pheromones) {
            boolean is_done = pheromone.update();
            if (is_done) {
                pheromonesToRemove.add(pheromone);
            }
        }

        for (Repelant repelant : repelants) {
            boolean is_done = repelant.update();
            if (is_done) {
                repelantsToRemove.add(repelant);
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

        for (Repelant new_repelant : repelantsToAdd) {
            addRepelantRepelant(new_repelant);
        }

        for (Pheromone donePheromone : pheromonesToRemove) {
            removePheromone(donePheromone);
        }

        for (Repelant doneRepelant : repelantsToRemove) {
            removeRepelant(doneRepelant);
        }
        
        for (Food food : foods) {
            if (food.isEmpty() && ((currentTime - food.getWhenEmpty()) > config.getTimeBeforeFoodRespawn())) {
                food.flourishes(food.getMaxSupply());
                food.setEmptiness(false);
            }
        }

        incrementTime();
    }

    public void addPredator(double x, double y, double energyLevel, double predator_energy_level_to_reproduce, double predator_reproduction_cost, String classType, double movingSpeed, double predator_detection_range, double prey_detection_range, double prey_eating_range, double energy_level_to_look_for_prey, double agent_nutritive_value, double height, double width) {
        this.predators.add(new Predator(x, y, energyLevel, predator_energy_level_to_reproduce, predator_reproduction_cost, classType, movingSpeed, predator_detection_range, prey_detection_range, prey_eating_range, energy_level_to_look_for_prey, agent_nutritive_value, height, width));
    }

    public void addPredatorPredator(Predator predator) {
        this.predators.add(predator);
    }

    public void addAgent(double x, double y, double energy, double energy_to_reproduce, double reproduction_cost, Genotype genotype, String class_type, double moving_speed, double food_detection_range, double agent_detection_range, double predator_detection_range) {
        this.aliveAgents.add(new Agent(x, y, energy, energy_to_reproduce, reproduction_cost, genotype, class_type, moving_speed, food_detection_range, agent_detection_range, predator_detection_range, config.getMutationProbability(), simulation_height, simulation_width));
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

    public void addRepelantRepelant(Repelant repelant) {
        this.repelants.add(repelant);
    }

    public void removeRepelant(Repelant repelant) {
        this.repelants.remove(repelant);
    }
    
    public void addRandomPredator() {
        addPredator(Math.random()*simulation_width, Math.random()*simulation_height, config.getPredatorBaseEnergyLevel(), config.getPredatorEnergyLevelToReproduce(), config.getPredatorReproductionCost(), "Predator", config.getPredatorSpeed(), config.getOtherPredatorDetectionRange(), config.getPredatorPreyDetectionRange(), config.getPredatorPreyEatingRange(), config.getPredatorEnergyLevelToLookForPrey(), config.getAgentNutritiveValue(), simulation_height, simulation_width);
    }
    
    public void addRandomAgent() {
        int allele_length = config.getAlleleLength();
        Gene phero_gene = getRandomGene(allele_length);
        Gene repelant_gene = getRandomGene(allele_length);
        Genotype genotype = new Genotype(phero_gene, repelant_gene);
        addAgent(Math.random()*simulation_width, Math.random()*simulation_height, config.getAgentBaseEnergyLevel(), config.getAgentEnergyLevelToReproduce(), config.getReproductionCost(), genotype, "Agent1", config.getMovingSpeed(), config.getFoodDetectionRange(), config.getAgentDetectionRange(), config.getAgentPredatorDetectionRange());
    }

    public void addAltruisticAgent() {
        int allele_length = config.getAlleleLength();
        Gene phero_gene = getConstantGene(allele_length, 1);
        Gene repelant_gene = getConstantGene(allele_length, 1);
        Genotype genotype = new Genotype(phero_gene, repelant_gene);
        addAgent(Math.random()*simulation_width, Math.random()*simulation_height, config.getAgentBaseEnergyLevel(), config.getAgentEnergyLevelToReproduce(), config.getReproductionCost(), genotype, "Agent1", config.getMovingSpeed(), config.getFoodDetectionRange(), config.getAgentDetectionRange(), config.getAgentPredatorDetectionRange());
    }
    
    public void addEgoisticAgent() {
        int allele_length = config.getAlleleLength();
        Gene phero_gene = getConstantGene(allele_length, 0);
        Gene repelant_gene = getConstantGene(allele_length, 0);
        Genotype genotype = new Genotype(phero_gene, repelant_gene);
        addAgent(Math.random()*simulation_width, Math.random()*simulation_height, config.getAgentBaseEnergyLevel(), config.getAgentEnergyLevelToReproduce(), config.getReproductionCost(), genotype, "Agent1", config.getMovingSpeed(), config.getFoodDetectionRange(), config.getAgentDetectionRange(), config.getAgentPredatorDetectionRange());
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

    public ArrayList<Predator> getPredators() {
        return this.predators;
    }

    public ArrayList<Repelant> getRepelants() {
        return this.repelants;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public double getNumberAgents() {
        double number_of_agents = aliveAgents.size();
        return number_of_agents;
    }    
    
    public double getNumberPredators() {
        double number_of_predators = predators.size();
        return number_of_predators;
    }

    public double getAverageSpreadProba() {
        List<Agent> agents = getAliveAgents();
        double number_of_agents = getNumberAgents();
        
        double sum_spread_probas = 0;
        for (Agent agent : agents) {
            sum_spread_probas += agent.getSpreadPheromoneProba();
        }
        double average_spread_proba = sum_spread_probas / number_of_agents;
        return average_spread_proba;
    }

    public double getPartOfAltruists() {
        List<Agent> agents = getAliveAgents();
        double number_of_agents = getNumberAgents(); 
        double part_of_altruists = 0;
        for (Agent agent : agents) {
            if (agent.isAPheromoneProducer()) {
                part_of_altruists += 1;
            }
        }
        double final_part = part_of_altruists / number_of_agents;
        return final_part;
    }

    public double getPartOfRepelers() {
        List<Agent> agents = getAliveAgents();
        double number_of_agents = getNumberAgents();
        double part_of_repelers = 0;
        for (Agent agent : agents) {
            if (agent.isARepelantProducer()) {
                part_of_repelers += 1;
            }
        }
        double final_part = part_of_repelers / number_of_agents;
        return final_part;
    }

    public Gene getRandomGene(int allele_length) {
        int[] allele1 = new int[allele_length];
        int[] allele2 = new int[allele_length];
        for (int i = 0; i < allele_length; i++) {
            allele1[i] = random.nextInt(2);
            allele2[i] = random.nextInt(2);
        }
        Allele all1 = new Allele(allele1);
        Allele all2 = new Allele(allele2);
        Gene random_gene = new Gene(all1, all2);
        return random_gene;
    }

    public Gene getConstantGene(int allele_length, int value) {
        int[] allele1 = new int[allele_length];
        int[] allele2 = new int[allele_length];
        for (int i = 0; i < allele_length; i++) {
            allele1[i] = value;
            allele2[i] = value;
        }
        Allele all1 = new Allele(allele1);
        Allele all2 = new Allele(allele2);
        Gene zero_gene = new Gene(all1, all2);
        return zero_gene;
    }


    public void incrementTime() {
        this.currentTime++;
    }
}
