import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.LineChart;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ChartSaver {
    public static void saveChartAsPng(LineChart<?, ?> chart, String filePath) {
        // Snapshot the chart
        WritableImage image = chart.snapshot(null, null);

        // Save the snapshot to a file
        File file = new File(filePath);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("Chart saved as " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving chart: " + e.getMessage());
        }
    }
}
