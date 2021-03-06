package de.hska.iwi.graphtraversal.input;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.IOException;

public class JsonReader {

    private final ObjectMapper mapper;

    /**
     * Initializes the reader.
     */
    public JsonReader() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Reads a given JSON document from the resources folder.
     *
     * @param filename The file name of the JSON file in the resources folder
     * @return A parsed object that contains all the information from the JSON document or null if an error occurred
     * @throws InvalidGraphConfigurationException if an error occurs during parsing of the JSON document
     */
    public GraphConfiguration readConfig(String filename) throws InvalidGraphConfigurationException {
        
        ClassLoader classLoader = getClass().getClassLoader();
        GraphConfiguration config = null;
        
        try(InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            config = mapper.readValue(inputStream, GraphConfiguration.class);
            
        } catch (IOException e) {
            throw new InvalidGraphConfigurationException(e.getMessage());
        }
        
        return config;
    }
}
