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

import java.util.ArrayList;
import java.util.List;

public class SimulationApp extends Application {
    private Simulation simulation;
    private boolean isPaused = false;
    private Image agentSprite;
    private Image foodSprite;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
        Platform.exit();  // Ensure that JavaFX exits fully when the window is closed
        System.exit(0);   // Optionally ensure full JVM termination
        });
        primaryStage.setTitle("Simulation Visualization");

        // Initialize the simulation
        simulation = new Simulation();
        initializeSimulation();

        agentSprite = new Image("file:C:\\Users\\boite\\Desktop\\altruism_simulation\\Multi_Agent_Altruism_Simulation\\ressources\\agent.png");
        foodSprite = new Image("file:C:\\Users\\boite\\Desktop\\altruism_simulation\\Multi_Agent_Altruism_Simulation\\ressources\\food.png");

        // Set up the canvas
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600); // Set canvas size
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
                    drawingStep(gc, canvas);
                }
            }
        }.start();
    }

    private void drawingStep(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawElements(gc);
        updateSimulation();
    }

    private void initializeSimulation() {
        // Add some initial agents and food to the simulation
        int[] allele1_1 = {1,0,0,1};
        int[] allele1_2 = {1,1,1,0};
        int[] allele2_1 = {1,0,0,1};
        int[] allele2_2 = {1,1,1,0};
        simulation.addAgent(100, 100, 100, allele1_1, allele1_2, "Agent1", 1.5, 50, 50);
        simulation.addAgent(200, 150, 100, allele2_1, allele2_2, "Agent2", 1.5, 50, 50);
        simulation.addFood(150, 150, 50, 2, 100);
        simulation.addFood(300, 300, 60, 3, 120);
    }

    private void drawElements(GraphicsContext gc) {
        // Draw Agents (as circles) and display their energy level
        List<Agent> agents = simulation.getAliveAgents();
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLACK); // Set stroke for the energy text
        gc.setLineWidth(1);
    
        for (Agent agent : agents) {
            double agentX = agent.getX();
            double agentY = agent.getY();
            double energy = agent.getEnergyLevel();  // Assuming `getEnergy()` returns the agent's energy level
    
            // Draw the agent as a circle
            gc.drawImage(agentSprite, agentX, agentY, 20, 20); // Draw agent sprite at (x, y) with a size of 20x20   
             
            // Draw the agent's energy level as text above the agent
            gc.strokeText(String.format("%.0f", energy), agentX, agentY - 5); // Display energy above the agent
        }
    
        // Draw Food (as squares)
        List<Food> foods = simulation.getFoods();
        gc.setFill(Color.GREEN);
        for (Food food : foods) {
            gc.drawImage(foodSprite, food.getX(), food.getY(), 10, 10); // Draw food sprite at (x, y) with a size of 10x10
        }
    }

    private void updateSimulation() {
        // Example: Move agents toward a random direction
        List<Agent> agents = simulation.getAliveAgents();
        for (Agent agent : agents) {
            // Move agents randomly (or toward food in the future)
            agent.moveTowardDirection(new double[]{Math.random()*2 - 0.9, Math.random()*2 - 0.9}, agent.getMovingSpeed());
        }

        // Update simulation time and other logic as needed
        simulation.incrementTime();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
