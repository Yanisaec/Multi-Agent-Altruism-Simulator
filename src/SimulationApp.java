import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;

public class SimulationApp extends Application {
    private Simulation simulation;  // The simulation instance

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulation Visualization");

        // Initialize the simulation
        simulation = new Simulation();
        initializeSimulation();

        // Set up the canvas
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600); // Set canvas size
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Use an AnimationTimer to update the canvas dynamically
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                // Clear the canvas
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Draw the simulation elements
                drawElements(gc);
                
                // Update the simulation state (e.g., move agents)
                updateSimulation();
            }
        }.start();
    }

    private void initializeSimulation() {
        // Add some initial agents and food to the simulation
        int[] genome = {1, 2, 3};
        simulation.addAgent(100, 100, 100, genome, "Agent1", 1.5, 50);
        simulation.addAgent(200, 150, 100, genome, "Agent2", 1.5, 50);
        simulation.addFood(150, 150, 50, 2, 100);
        simulation.addFood(300, 300, 60, 3, 120);
    }

    private void drawElements(GraphicsContext gc) {
        // Draw Agents (as circles)
        List<Agent> agents = simulation.getAliveAgents();
        gc.setFill(Color.BLUE);
        for (Agent agent : agents) {
            gc.fillOval(agent.getX(), agent.getY(), 10, 10); // x, y, width, height
        }

        // Draw Food (as squares)
        List<Food> foods = simulation.getFoods();
        gc.setFill(Color.GREEN);
        for (Food food : foods) {
            gc.fillRect(food.getX(), food.getY(), 5, 5); // x, y, width, height
        }
    }

    private void updateSimulation() {
        // Example: Move agents toward a random direction
        List<Agent> agents = simulation.getAliveAgents();
        for (Agent agent : agents) {
            // Move agents randomly (or toward food in the future)
            agent.moveTowardDirection(new double[]{Math.random(), Math.random()}, agent.getMovingSpeed());
        }

        // Update simulation time and other logic as needed
        simulation.incrementTime();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
