package converter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import converter.models.OutputMarket;

public class FileHandler {
    private ObjectMapper objectMapper;
    
    public FileHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    public String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
    
    public void writeFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes());
    }
    
    public String toJson(List<OutputMarket> markets) throws IOException {
        return objectMapper.writeValueAsString(markets);
    }
}