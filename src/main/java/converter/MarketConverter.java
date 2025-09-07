package converter;

import java.util.List;

import converter.models.InputMarket;
import converter.models.OutputMarket;

public class MarketConverter {
    private MarketParser parser;
    private MarketTransformer transformer;
    private FileHandler fileHandler;
    
    public MarketConverter() {
        this.parser = new MarketParser();
        this.transformer = new MarketTransformer();
        this.fileHandler = new FileHandler();
    }
    
    public void convertFile(String inputPath, String outputPath) {
        try {
            // Read input file
            String jsonContent = fileHandler.readFile(inputPath);
            
            // Parse input markets
            List<InputMarket> inputMarkets = parser.parse(jsonContent);
            
            // Transform to output format
            List<OutputMarket> outputMarkets = transformer.transform(inputMarkets);
            
            // Write output file
            String outputJson = fileHandler.toJson(outputMarkets);
            fileHandler.writeFile(outputPath, outputJson);
            
            System.out.println("Conversion completed successfully!");
            System.out.println("Output file created: " + outputPath);
            
        } catch (Exception e) {
            System.err.println("Error during conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}