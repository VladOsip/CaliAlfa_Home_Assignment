package converter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static final String INPUT_FOLDER = "inputs";
    private static final String OUTPUT_FOLDER = "outputs";
    
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
    
    public void run() {
        try {
            // Setup folders
            setupFolders();
            
            // Get input files
            File[] inputFiles = getInputFiles();
            if (inputFiles.length == 0) {
                System.out.println("No input files found in the 'inputs' folder.");
                System.out.println("Please add JSON files to the 'inputs' folder and run again.");
                return;
            }
            
            // Display menu and get user selection
            String selectedFile = displayMenuAndGetSelection(inputFiles);
            if (selectedFile == null) {
                System.out.println("Invalid selection. Exiting.");
                return;
            }
            
            // Process the selected file
            processFile(selectedFile);
            
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /* If the folders don't exist then this creates them */
    private void setupFolders() throws Exception {
        Path inputPath = Paths.get(INPUT_FOLDER);
        Path outputPath = Paths.get(OUTPUT_FOLDER);
        
        boolean inputCreated = false;
        boolean outputCreated = false;
        
        if (!Files.exists(inputPath)) {
            Files.createDirectories(inputPath);
            inputCreated = true;
        }
        
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
            outputCreated = true;
        }
        
        if (inputCreated) {
            System.out.println("'inputs' folder created. Please add JSON files to the 'inputs' folder.");
            if (outputCreated) {
                System.out.println("'outputs' folder created for generated output files.");
            }
            return;
        }
    }
    
    private File[] getInputFiles() {
        File inputDir = new File(INPUT_FOLDER);
        return inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
    }
    
    private String displayMenuAndGetSelection(File[] inputFiles) {
        System.out.println("\n=== Market Converter ===");
        System.out.println("Available input files:");
        
        for (int i = 0; i < inputFiles.length; i++) {
            System.out.println((i + 1) + ". " + inputFiles[i].getName());
        }
        
        System.out.print("\nEnter the number of the file you want to process: ");
        
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        try {
            int selection = scanner.nextInt();
            if (selection >= 1 && selection <= inputFiles.length) {
                return inputFiles[selection - 1].getName();
            }
        } catch (Exception e) {// Ignore invalid input
             System.err.println("Invalid input received.");
        }
        
        return null;
    }
    
    private void processFile(String fileName) {
        try {
            String inputPath = INPUT_FOLDER + File.separator + fileName;
            String outputPath = generateOutputPath(fileName);
            
            MarketConverter converter = new MarketConverter();
            converter.convertFile(inputPath, outputPath);
            
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String generateOutputPath(String inputFileName) {
        // Remove .json extension from input name
        String baseName = inputFileName;
        if (baseName.toLowerCase().endsWith(".json")) {
            baseName = baseName.substring(0, baseName.length() - 5);
        }
        
        // Generate timestamp in DDMMYYYY_HHMMSS format
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String timestamp = now.format(formatter);
        
        // Create output filename
        String outputFileName = String.format("Output_%s_%s.json", baseName, timestamp);
        
        return OUTPUT_FOLDER + File.separator + outputFileName;
    }
}