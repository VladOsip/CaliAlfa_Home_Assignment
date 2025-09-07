package converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;

public class MarketConverterTest {
    private MarketConverter converter;
    private final String testInputFile = "test_input.json";
    private final String testOutputFile = "test_output.json";
    
    @BeforeEach
    void setUp() {
        converter = new MarketConverter();
    }
    
    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(Paths.get(testInputFile));
            Files.deleteIfExists(Paths.get(testOutputFile));
        } catch (IOException e) {
            // Ignore cleanup errors
        }
    }
    
    @Test
    void testConvertValidFile() throws IOException {
        String inputJson = "[{\"name\":\"1x2\",\"event_id\":\"123456\",\"selections\":[{\"name\":\"Team A\",\"odds\":1.65},{\"name\":\"draw\",\"odds\":3.2},{\"name\":\"Team B\",\"odds\":2.6}]}]";
        Files.write(Paths.get(testInputFile), inputJson.getBytes());
        
        assertDoesNotThrow(() -> converter.convertFile(testInputFile, testOutputFile));
        
        assertTrue(Files.exists(Paths.get(testOutputFile)));
        String outputContent = Files.readString(Paths.get(testOutputFile));
        assertTrue(outputContent.contains("market_uid"));
        assertTrue(outputContent.contains("123456_1"));
    }
    
    @Test
    void testConvertFileNotFound() {
        assertDoesNotThrow(() -> converter.convertFile("nonexistent.json", testOutputFile));
    }
    
    @Test
    void testConvertInvalidJson() throws IOException {
        Files.write(Paths.get(testInputFile), "invalid json".getBytes());
        assertDoesNotThrow(() -> converter.convertFile(testInputFile, testOutputFile));
    }
    
    @Test
    void testConvertMultipleMarketTypes() throws IOException {
        String inputJson = "[" +
            "{\"name\":\"Total\",\"event_id\":\"789\",\"selections\":[{\"name\":\"over 2.5\",\"odds\":1.85},{\"name\":\"under 2.5\",\"odds\":1.95}]}," +
            "{\"name\":\"Handicap\",\"event_id\":\"789\",\"selections\":[{\"name\":\"Team A +1.5\",\"odds\":1.8},{\"name\":\"Team B -1.5\",\"odds\":2.0}]}" +
            "]";
        Files.write(Paths.get(testInputFile), inputJson.getBytes());
        
        assertDoesNotThrow(() -> converter.convertFile(testInputFile, testOutputFile));
        
        String outputContent = Files.readString(Paths.get(testOutputFile));
        assertTrue(outputContent.contains("789_18_2.5")); // Total market
        assertTrue(outputContent.contains("789_16_1.5")); // Handicap market
    }
}