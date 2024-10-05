public class Config {
    private int number_of_agents;
    private int number_of_food_spots;
    private double moving_speed;
    private double agent_base_energy_level;
    private double food_detection_range;
    private double agent_detection_range;
    private double food_nutritive_value;
    private double food_max_supply;
    private double time_before_food_respawn;


    public int getNumberOfAgents() { return number_of_agents; }
    public int getNumberOfFoodSpots() { return number_of_food_spots; }
    public double getAgentBaseEnergyLevel() { return agent_base_energy_level; }
    public double getMovingSpeed() { return moving_speed; }
    public double getFoodDetectionRange() { return food_detection_range; }
    public double getAgentDetectionRange() { return agent_detection_range; }
    public double getFoodNutritiveValue() { return food_nutritive_value; }
    public double getFoodMaxSupply() { return food_max_supply; }
    public double getTimeBeforeFoodRespawn() { return time_before_food_respawn; }
}