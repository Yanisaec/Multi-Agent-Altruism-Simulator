import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import java.util.ArrayList;

import com.google.gson.Gson;

public class MultipleSimulation extends Application {
    private ArrayList<Simulation> simulations;
    private Config config;
    private double height;
    private double width;
    private double average_part_of_altruists_across_sims;
    private XYChart.Series<Number, Number> spreadProbabilitySeries;  // For average spread probability chart data
    private XYChart.Series<Number, Number> simulationsRunningSeries;  // For simulations running chart data
    private XYChart.Series<Number, Number> numberAgentsSeries; 
    private LineChart<Number, Number> spreadProbabilityChart;
    private LineChart<Number, Number> simulationsRunningChart;
    private LineChart<Number, Number> numberAgentsChart;

    @Override
    public void start(Stage stage) {
        config = loadConfig();
        height = 2000;
        width = 2000;
        average_part_of_altruists_across_sims = 0;
        
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
        yAxis1.setLabel("Part of Altruists");

        spreadProbabilityChart = new LineChart<>(xAxis1, yAxis1);
        spreadProbabilityChart.setTitle("Part of Altruists Across Simulations");
        spreadProbabilityChart.setVerticalGridLinesVisible(false);

        spreadProbabilitySeries = new XYChart.Series<>();
        spreadProbabilitySeries.setName("Part of Altruists");
        spreadProbabilityChart.getData().add(spreadProbabilitySeries);

        // Create chart for Number of Simulations Running
        NumberAxis xAxis2 = new NumberAxis();
        NumberAxis yAxis2 = new NumberAxis(0, 100, 1);
        yAxis2.setAutoRanging(false);
        xAxis2.setLabel("Iteration");
        yAxis2.setLabel("Number of Simulations Running");

        simulationsRunningChart = new LineChart<>(xAxis2, yAxis2);
        simulationsRunningChart.setTitle("Number of Simulations Still Running");
        simulationsRunningChart.setVerticalGridLinesVisible(false);

        simulationsRunningSeries = new XYChart.Series<>();
        simulationsRunningSeries.setName("Simulations Running");
        simulationsRunningChart.getData().add(simulationsRunningSeries);      

        // Create char for average number of agents across simulations
        NumberAxis xAxis3 = new NumberAxis();
        NumberAxis yAxis3 = new NumberAxis();  // Auto-ranging
        xAxis3.setLabel("Iteration");
        yAxis3.setLabel("Average Number of Agents Across Simulations");

        numberAgentsChart = new LineChart<>(xAxis3, yAxis3);
        numberAgentsChart.setTitle("Average Number of Agents Across Simulations");
        numberAgentsChart.setVerticalGridLinesVisible(false);

        numberAgentsSeries = new XYChart.Series<>();
        numberAgentsSeries.setName("Number of Agents");
        numberAgentsChart.getData().add(numberAgentsSeries);

        // Create the GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);  // Horizontal gap between columns
        gridPane.setVgap(10);  // Vertical gap between rows

        // Add charts to the grid: 2x2 layout
        gridPane.add(spreadProbabilityChart, 0, 0);  // Column 0, Row 0
        gridPane.add(simulationsRunningChart, 1, 0);  // Column 1, Row 0
        gridPane.add(numberAgentsChart, 0, 1);  // Column 0, Row 1

        // Make the charts resize with the window
        GridPane.setVgrow(spreadProbabilityChart, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setVgrow(simulationsRunningChart, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setVgrow(numberAgentsChart, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setHgrow(spreadProbabilityChart, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setHgrow(simulationsRunningChart, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setHgrow(numberAgentsChart, javafx.scene.layout.Priority.ALWAYS);

        // Adjust the scene size if needed
        Scene scene = new Scene(gridPane);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

        // Run the simulation and update the charts
        new Thread(this::run).start();

        // Save charts after the simulation ends (this could be triggered elsewhere as needed)
        new Thread(() -> {
            try {
                Thread.sleep(5000);  // Wait for some time to ensure the charts are populated
                saveChartsAsPng();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void run() {
        for (int i = 0; i < 1000000; i++) {
            average_part_of_altruists_across_sims = 0;
            double average_number_of_agents = 0;
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
                average_part_of_altruists_across_sims += simulation.getPartOfAltruists();
                // average_part_of_altruists_across_sims += simulation.getAverageSpreadProba();
                average_number_of_agents += simulation.getNumberAgents();
            }


            if (number_of_simulations > 0) {
                average_part_of_altruists_across_sims /= number_of_simulations;
                average_number_of_agents /= number_of_simulations;
            }

            // Update the charts with the new values
            final int iteration = i;
            final double currentAveragePartAltruists = average_part_of_altruists_across_sims;
            final double currentSimulationsRunning = number_of_simulations;
            final double currentAverageNumberAgents = average_number_of_agents;
            if (i % 1000 == 0 && number_of_simulations != 0) {
                javafx.application.Platform.runLater(() -> {
                    spreadProbabilitySeries.getData().add(new XYChart.Data<>(iteration, currentAveragePartAltruists));
                    simulationsRunningSeries.getData().add(new XYChart.Data<>(iteration, currentSimulationsRunning));
                    numberAgentsSeries.getData().add(new XYChart.Data<>(iteration, currentAverageNumberAgents));
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
        for (int i= 0; i < config.getNumberOfRandomAgents(); i ++) {
            simulation.addRandomAgent();
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

    public void saveChartAsPng(LineChart<Number, Number> chart, String path) {
    // Ensure this runs on the JavaFX application thread
    Platform.runLater(() -> {
        WritableImage image = chart.snapshot(null, null);
        File file = new File(path);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("Chart saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
}
    // Save all charts at once
    private void saveChartsAsPng() {
        saveChartAsPng(spreadProbabilityChart, "spread_probability_chart.png");
        saveChartAsPng(simulationsRunningChart, "simulations_running_chart.png");
        saveChartAsPng(numberAgentsChart, "number_agents_chart.png");
    }
    

    public static void main(String[] args) {
        launch(args);  // Launches the JavaFX application
    }
}
