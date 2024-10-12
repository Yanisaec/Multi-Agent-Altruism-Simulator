import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;

public class SimulationApp extends Application {
    private Simulation simulation;
    private boolean isPaused = false;
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
        simulation = new Simulation(config, simulation_height, simulation_width);
        initializeSimulation();

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
        simulation.updateSimulation();
    }

    private void initializeSimulation() {
        for (int i = 0; i < config.getNumberOfAltruisticAgents(); i++) {
            simulation.addAltruisticAgent();
        }        
        for (int i = 0; i < config.getNumberOfEgoisticAgents(); i++) {
            simulation.addEgoisticAgent();
        }        
        for (int i= 0; i < config.getNumberOfRandomAgents(); i ++) {
            simulation.addRandomAgent();
        }
        for (int i = 0; i < config.getNumberOfFoodSpots(); i++) {
            simulation.addRandomFood();
        }
        simulation.addRandomPredator();
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
        for (Pheromone pheromone : pheromones) {
            double pheromoneX = pheromone.getX();
            double pheromoneY = pheromone.getY();
            double size = pheromone.getRadiusEffect()*2;
            gc.setStroke(Color.PINK); 
            gc.strokeOval(pheromoneX - (size / 2), pheromoneY - (size / 2), size, size);
        }

        List<Repelant> repelants = simulation.getRepelants();
        gc.setFill(Color.BEIGE);
        for (Repelant repelant : repelants) {
            double repelantX = repelant.getX();
            double repelantY = repelant.getY();
            double size = repelant.getRadiusEffect()*2;
            gc.strokeOval(repelantX - (size / 2), repelantY - (size / 2), size, size);
        }
        
        List<Agent> agents = simulation.getAliveAgents();
        double number_of_agents = simulation.getNumberAgents();
        String number_agents_string = String.format("%.0f", number_of_agents);
        gc.setFill(Color.BLACK); // Set fill color for text
        gc.fillText("Number of agents: " + number_agents_string, 10, 20);
        
        double part_of_altruists = simulation.getPartOfAltruists();
        String part_of_altruists_string = String.format("%.2f", part_of_altruists);
        gc.fillText("Average spread probability: " + part_of_altruists_string, 10, 40);        
        
        double part_of_repelers = simulation.getPartOfRepelers();
        String part_of_repelers_string = String.format("%.2f", part_of_repelers);
        gc.fillText("Average spread probability: " + part_of_repelers_string, 10, 60);
        
        double agentSize = 10;
        for (Agent agent : agents) {
            if (agent.isAPheromoneProducer()) {
                gc.setFill(Color.BLUE);
            } else {
                gc.setFill(Color.RED);
            }
            double agentX = agent.getX();
            double agentY = agent.getY();
            double energy = agent.getEnergyLevel();
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

        double predator_size = 12;
        List<Predator> predators = simulation.getPredators();
        for (Predator predator : predators) {
            double predatorX = predator.getX();
            double predatorY = predator.getY();
            double prey_detection_range = predator.getPreyDetectionRange();
            double prey_eating_range = predator.getPreyEatingRange();
            double kill_count = predator.getKillCount();
            double energy_level_predator = predator.getEnergyLevel();
            gc.setFill(Color.BLACK);
            gc.fillRect(predatorX - (predator_size / 2), predatorY - (predator_size / 2), predator_size, predator_size);
            gc.strokeOval(predatorX - (prey_detection_range / 2), predatorY - (prey_detection_range / 2), prey_detection_range, prey_detection_range);
            gc.strokeOval(predatorX - (prey_eating_range / 2), predatorY - (prey_eating_range / 2), prey_eating_range, prey_eating_range);
            gc.strokeText(String.format("%.0f", energy_level_predator), predatorX - 3, predatorY - 10);
            // gc.strokeText(String.format("%.0f", kill_count), predatorX - 3, predatorY - 10);
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