public class Config {
    private int number_of_altruistic_agents;
    private int number_of_egoistic_agents;
    private int number_of_random_agents;
    private int number_of_food_spots;
    private double moving_speed;
    private double agent_base_energy_level;
    private double food_detection_range;
    private double agent_detection_range;
    private double food_nutritive_value;
    private double food_max_supply;
    private double time_before_food_respawn;
    private double energy_level_to_reproduce;
    private double reproduction_cost;
    private double pheromone_radius;
    private double pheromone_lifespan;
    private double pheromone_energy_cost;
    private double mutation_probability;
    private int allele_length;
    private double prey_detection_range;
    private double prey_eating_range;
    private double predator_speed;

    public int getNumberOfAltruisticAgents() { return number_of_altruistic_agents; }
    public int getNumberOfEgoisticAgents() { return number_of_egoistic_agents; }
    public int getNumberOfRandomAgents() { return number_of_random_agents; }
    public int getNumberOfFoodSpots() { return number_of_food_spots; }
    public double getAgentBaseEnergyLevel() { return agent_base_energy_level; }
    public double getMovingSpeed() { return moving_speed; }
    public double getFoodDetectionRange() { return food_detection_range; }
    public double getAgentDetectionRange() { return agent_detection_range; }
    public double getFoodNutritiveValue() { return food_nutritive_value; }
    public double getFoodMaxSupply() { return food_max_supply; }
    public double getTimeBeforeFoodRespawn() { return time_before_food_respawn; }
    public double getEnergyLevelToReproduce() { return energy_level_to_reproduce; }
    public double getReproductionCost() { return reproduction_cost; }
    public double getPheromoneRadius() { return pheromone_radius; }
    public double getPheromoneLifeSpan() { return pheromone_lifespan; }
    public double getPheromoneEnergyCost() { return pheromone_energy_cost; }
    public double getMutationProbability() { return mutation_probability; }
    public int getAlleleLength() { return allele_length; }
    public double getPredatorSpeed() { return predator_speed; }
    public double getPredatorPreyDetectionRange() { return prey_detection_range; }
    public double getPredatorPreyEatingRange() { return prey_eating_range; }
}