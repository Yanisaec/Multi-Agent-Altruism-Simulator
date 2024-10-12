public class Config {
    private int number_of_altruistic_agents;
    private int number_of_egoistic_agents;
    private int number_of_predators;
    private int number_of_random_agents;
    private int number_of_food_spots;
    private double moving_speed;
    private double agent_base_energy_level;
    private double food_detection_range;
    private double agent_detection_range;
    private double predator_detection_range;
    private double predator_base_energy_level;
    private double energy_level_to_look_for_prey;
    private double agent_nutritive_value;
    private double food_nutritive_value;
    private double food_max_supply;
    private double time_before_food_respawn;
    private double agent_energy_level_to_reproduce;
    private double predator_energy_level_to_reproduce;
    private double reproduction_cost;
    private double pheromone_radius;
    private double pheromone_lifespan;
    private double pheromone_energy_cost;
    private double mutation_probability;
    private int allele_length;
    private double prey_detection_range;
    private double prey_eating_range;
    private double predator_speed;
    private double other_predator_detection_range;
    private double predator_reproduction_cost;
    private double repelant_radius;
    private double repelant_lifespan;
    private double repelant_energy_cost;

    public int getNumberOfAltruisticAgents() { return number_of_altruistic_agents; }
    public int getNumberOfEgoisticAgents() { return number_of_egoistic_agents; }
    public int getNumberOfPredators() { return number_of_predators; }
    public int getNumberOfRandomAgents() { return number_of_random_agents; }
    public int getNumberOfFoodSpots() { return number_of_food_spots; }
    public double getAgentBaseEnergyLevel() { return agent_base_energy_level; }
    public double getMovingSpeed() { return moving_speed; }
    public double getFoodDetectionRange() { return food_detection_range; }
    public double getAgentDetectionRange() { return agent_detection_range; }
    public double getAgentPredatorDetectionRange() { return predator_detection_range; }
    public double getFoodNutritiveValue() { return food_nutritive_value; }
    public double getFoodMaxSupply() { return food_max_supply; }
    public double getTimeBeforeFoodRespawn() { return time_before_food_respawn; }
    public double getAgentEnergyLevelToReproduce() { return agent_energy_level_to_reproduce; }
    public double getPredatorEnergyLevelToReproduce() { return predator_energy_level_to_reproduce; }
    public double getReproductionCost() { return reproduction_cost; }
    public double getPheromoneRadius() { return pheromone_radius; }
    public double getPheromoneLifeSpan() { return pheromone_lifespan; }
    public double getPheromoneEnergyCost() { return pheromone_energy_cost; }
    public double getMutationProbability() { return mutation_probability; }
    public int getAlleleLength() { return allele_length; }
    public double getPredatorSpeed() { return predator_speed; }
    public double getPredatorPreyDetectionRange() { return prey_detection_range; }
    public double getPredatorPreyEatingRange() { return prey_eating_range; }
    public double getPredatorBaseEnergyLevel() { return predator_base_energy_level; }
    public double getPredatorEnergyLevelToLookForPrey() { return energy_level_to_look_for_prey; }
    public double getOtherPredatorDetectionRange() { return other_predator_detection_range; }
    public double getPredatorReproductionCost() { return predator_reproduction_cost; }
    public double getAgentNutritiveValue() { return agent_nutritive_value; }
    public double getRepelantRadius() { return repelant_radius; }
    public double getRepelantLifespan() { return repelant_lifespan; }
    public double getRepelantEnergyCost() { return repelant_energy_cost; }
}