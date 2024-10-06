import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class SimulationApp extends Application {
    private Simulation simulation;
    private boolean isPaused = false;
    private Image agentSprite;
    private Image foodSprite;
    private double simulation_height = 600;
    private double simulation_width = 1000;
    private int iterations = 0;
    private double iterations_per_second = -1;
    private long start_time = System.nanoTime();
    private Config config;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.setOnCloseRequest(event -> {
        Platform.exit();  // Ensure that JavaFX exits fully when the window is closed
        System.exit(0);   // Optionally ensure full JVM termination
        });
        primaryStage.setTitle("Simulation Visualization");

        config = loadConfig();

        // Initialize the simulation
        simulation = new Simulation();
        initializeSimulation();
        agentSprite = new Image("file:C:/Users/boite/Desktop/altruism_simulation/Multi_Agent_Altruism_Simulation/src/agent.png");
        foodSprite = new Image("file:C:/Users/boite/Desktop/altruism_simulation/Multi_Agent_Altruism_Simulation/src/food.png");

        // Set up the canvas
        Group root = new Group();
        Canvas canvas = new Canvas(simulation_width, simulation_height); // Set canvas size
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add a key event listener to toggle pause and advance one frame when paused
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isPaused = !isPaused;  // Toggle pause mode
            } else if (event.getCode() == KeyCode.RIGHT && isPaused) {
                // Move one frame forward if paused and the right arrow key is pressed
                drawingStep(gc, canvas);
            }
        });

        // Use an AnimationTimer for continuous updates
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (!isPaused) {
                    iterations++;
                    drawingStep(gc, canvas);

                    // Calculate the elapsed time in seconds
                    long currentTime = System.nanoTime();
                    double elapsedTime = (currentTime - start_time) / 1_000_000_000.0; // Convert to seconds
                    
                    if (elapsedTime >= 1) {
                        start_time = currentTime;
                        iterations_per_second = iterations / elapsedTime; // Reset iterations counter
                        iterations = 0;
                    }
                }
            }
        }.start();
    }

    private void drawingStep(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawElements(gc);
        simulation.updateSimulation(config, simulation_height, simulation_width);
    }

    private void initializeSimulation() {
        // int[] allele1 = {1,0,0,1};
        // int[] allele2 = {1,1,1,0};
        // simulation.addAgent(200, 200, 500, allele1, allele2, "Agent1", 0.2, 150, 100);
        // simulation.addFood(200, 175, 50, 2, 100);
        // simulation.addFood(200, 245, 50, 2, 100);

        for (int i = 0; i < config.getNumberOfAltruisticAgents(); i++) {
            simulation.addAltruisticAgent(config, simulation_height, simulation_width);
        }       
        for (int i = 0; i < config.getNumberOfEgoisticAgents(); i++) {
            simulation.addEgoisticAgent(config, simulation_height, simulation_width);
        }        
        for (int i = 0; i < config.getNumberOfFoodSpots(); i++) {
            simulation.addRandomFood(config, simulation_height, simulation_width);
        }
    }

    private void drawElements(GraphicsContext gc) {
        // Draw iterations per second
        gc.setFill(Color.BLACK); // Set fill color for text
        String iterations_per_second_string = String.format("%.0f", iterations_per_second);
        gc.fillText("Iterations/s: " + iterations_per_second_string, simulation_width - 150, 20);
        
        gc.setStroke(Color.BLACK); // Set stroke for the energy text
        gc.setLineWidth(1);
        
        List<Food> foods = simulation.getFoods();
        gc.setFill(Color.GREEN);
        for (Food food : foods) {
            double foodX = food.getX();
            double foodY = food.getY();
            // gc.drawImage(foodSprite, food.getX(), food.getY(), 10, 10); // Draw food sprite at (x, y) with a size of 10x10
            gc.fillRect(foodX, foodY, 5, 5);
            gc.strokeText(String.format("%.0f", food.getCurrentSupply()), foodX - 5, foodY + 15); // Display energy above the agent
        }

        List<Pheromone> pheromones = simulation.getPheromones();
        gc.setFill(Color.GREEN);
        for (Pheromone pheromone : pheromones) {
            double pheromoneX = pheromone.getX();
            double pheromoneY = pheromone.getY();
            double size = pheromone.getRadiusEffect()*2;
            gc.setStroke(Color.PINK); 
            gc.strokeOval(pheromoneX - (size / 2), pheromoneY - (size / 2), size, size);
        }
        
        List<Agent> agents = simulation.getAliveAgents();
        double number_of_agents = agents.size();
        String number_agents_string = String.format("%.0f", number_of_agents);
        gc.setFill(Color.BLACK); // Set fill color for text
        gc.fillText("Number of agents: " + number_agents_string, 10, 20);
        
        double sum_spread_probas = 0;
        for (Agent agent : agents) {
            sum_spread_probas += agent.getSpreadProba();
        }
        double average_spread_proba = sum_spread_probas / number_of_agents;
        String average_spread_proba_string = String.format("%.2f", average_spread_proba);
        gc.fillText("Average spread probability: " + average_spread_proba_string, 10, 40);
        
        double agentSize = 10;
        for (Agent agent : agents) {
            if (agent.getSpreadProba() > 0.5) {
                gc.setFill(Color.BLUE);
            } else {
                gc.setFill(Color.RED);
            }
            double agentX = agent.getX();
            double agentY = agent.getY();
            double energy = agent.getEnergyLevel();  // Assuming `getEnergy()` returns the agent's energy level
            double food_detection_range = agent.getFoodDetectionRange()*2;
            // double agent_detection_range = agent.getAgentDetectionRange()*2;
            
            // Draw the agent as a circle
            // gc.drawImage(agentSprite, agentX, agentY, 20, 20); // Draw agent sprite at (x, y) with a size of 20x20   
            gc.fillOval(agentX - (agentSize / 2), agentY - (agentSize / 2), agentSize, agentSize);
            
            // gc.setStroke(Color.RED); 
            // gc.strokeOval(agentX - (agent_detection_range / 2), agentY - (agent_detection_range / 2), agent_detection_range, agent_detection_range);
            gc.setStroke(Color.BLACK); 
            gc.strokeOval(agentX - (food_detection_range / 2), agentY - (food_detection_range / 2), food_detection_range, food_detection_range);

            // Draw the agent's energy level as text above the agent
            gc.setLineWidth(1);
            gc.strokeText(String.format("%.0f", energy), agentX, agentY - 5); // Display energy above the agent
        } 
    }

    private Config loadConfig() {
    Gson gson = new Gson();
    Config config = null;

    try (FileReader reader = new FileReader("config.json")) {
        config = gson.fromJson(reader, Config.class);
    } catch (IOException e) {
        e.printStackTrace();
    }

    return config;
}

    public static void main(String[] args) {
        launch(args);
    }
}