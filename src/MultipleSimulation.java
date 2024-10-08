import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class MultipleSimulation extends Application {
    private ArrayList<Simulation> simulations;
    private Config config;
    private double height;
    private double width;
    private double average_spread_proba_across_sims;
    private XYChart.Series<Number, Number> spreadProbabilitySeries;  // For average spread probability chart data
    private XYChart.Series<Number, Number> simulationsRunningSeries;  // For simulations running chart data

    @Override
    public void start(Stage stage) {
        config = loadConfig();
        height = 600;
        width = 1000;
        average_spread_proba_across_sims = 0;
        
        // Initialize simulations
        simulations = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Simulation new_sim = new Simulation(config, height, width);
            new_sim = initializeSimulation(new_sim);
            simulations.add(new_sim);
        }
        
        // Create chart for Average Spread Probability
        NumberAxis xAxis1 = new NumberAxis();
        NumberAxis yAxis1 = new NumberAxis(0, 1, 0.1);  // Fixed bounds between 0 and 1
        yAxis1.setAutoRanging(false);  // Disable auto-ranging to enforce the bounds
        xAxis1.setLabel("Iteration");
        yAxis1.setLabel("Average Spread Probability");

        LineChart<Number, Number> spreadProbabilityChart = new LineChart<>(xAxis1, yAxis1);
        spreadProbabilityChart.setTitle("Average Spread Probability Across Simulations");

        spreadProbabilitySeries = new XYChart.Series<>();
        spreadProbabilitySeries.setName("Average Spread Probability");
        spreadProbabilityChart.getData().add(spreadProbabilitySeries);

        // Create chart for Number of Simulations Running
        NumberAxis xAxis2 = new NumberAxis();
        NumberAxis yAxis2 = new NumberAxis();  // Auto-ranging
        xAxis2.setLabel("Iteration");
        yAxis2.setLabel("Number of Simulations Running");

        LineChart<Number, Number> simulationsRunningChart = new LineChart<>(xAxis2, yAxis2);
        simulationsRunningChart.setTitle("Number of Simulations Still Running");

        simulationsRunningSeries = new XYChart.Series<>();
        simulationsRunningSeries.setName("Simulations Running");
        simulationsRunningChart.getData().add(simulationsRunningSeries);

        // Add both charts to the scene
        VBox vbox = new VBox(spreadProbabilityChart, simulationsRunningChart);
        Scene scene = new Scene(vbox, 800, 800);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();

        // Run the simulation and update the charts
        new Thread(this::run).start();
    }

    private void run() {
        for (int i = 0; i < 100000; i++) {
            average_spread_proba_across_sims = 0;
            ArrayList<Simulation> simulations_to_remove = new ArrayList<>();
            
            for (Simulation simulation : simulations) {
                double number_of_agents = simulation.getNumberAgents();

                if (number_of_agents > 0) {
                    simulation.updateSimulation();
                } else {
                    simulations_to_remove.add(simulation);
                }
            }

            for (Simulation simulation_to_remove : simulations_to_remove) {
                simulations.remove(simulation_to_remove);
            }

            double number_of_simulations = simulations.size();
            for (Simulation simulation : simulations) {
                average_spread_proba_across_sims += simulation.getAverageSpreadProba();
            }

            if (number_of_simulations > 0) {
                average_spread_proba_across_sims /= number_of_simulations;
            }

            // Update the charts with the new values
            final int iteration = i;
            final double currentAverageSpreadProba = average_spread_proba_across_sims;
            final double currentSimulationsRunning = number_of_simulations;
            if (i % 100 == 0) {
                javafx.application.Platform.runLater(() -> {
                    spreadProbabilitySeries.getData().add(new XYChart.Data<>(iteration, currentAverageSpreadProba));
                    simulationsRunningSeries.getData().add(new XYChart.Data<>(iteration, currentSimulationsRunning));
                });
            }
        }
    }

    private Simulation initializeSimulation(Simulation simulation) {
        for (int i = 0; i < config.getNumberOfAltruisticAgents(); i++) {
            simulation.addAltruisticAgent();
        }       
        for (int i = 0; i < config.getNumberOfEgoisticAgents(); i++) {
            simulation.addEgoisticAgent();
        }        
        for (int i = 0; i < config.getNumberOfFoodSpots(); i++) {
            simulation.addRandomFood();
        }
        return simulation;
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
        launch(args);  // Launches the JavaFX application
    }
}
