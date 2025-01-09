package ie.atu.sw;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class WordEmbeddingsProccessor extends AbstractProcessor {

    /*
     * This method is used to store the content of a file
     * Method must be implemented due to extending AbstractProcessor
     * @param filePath The path to the file
     * @return A map containing word embeddings
     * @See Interface Process
     */
    public Map<String, double[]> storeFile(String filePath) {

        /*
            * Creating a ConcurrentHashMap to store the word embeddings
            * ConcurrentHashmap used to allow for concurrent access to the map from virtual threads
         */
        Map<String, double[]> embeddings = new ConcurrentHashMap<>();

        // Creating a VirtualThreadPerTaskExecutor to process the file
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        /*
            * Try with resources to read the file
            * Using a BufferedReader to read the file line by line
            * Submitting each line to the executor to be processed
         */
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String finalLine = line;

                /*
                    * Submitting the line to the executor
                    * Using a lambda expression to process the line
                    * This allows for the processing of the line to be done in a separate thread
                 */
                executor.submit(() -> processLines(finalLine, embeddings));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Shutting down the executor
            executor.shutdown();
            try {
                // Waiting for the executor to terminate
                if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                    System.err.println("Warning: Not all tasks completed.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return embeddings;
    }

    /*
        * This method is used to process lines from the file
        * @param line The line to be processed
        * @param embeddings A map containing word embeddings
        * @return void
        * @See Interface Process
     */
    public void processLines(String line, Map<String, double[]> embeddings) {
        try {

            // Cleaning the line by removing extra commas and spaces
            String cleanedLine = line.trim().replaceAll(",+", ",").replaceAll(",$", "").replaceAll(",\\s+", " ");

            // Splitting the cleaned line by spaces
            String[] section = cleanedLine.split("\\s+");

            // Getting the word from the first section
            String word = section[0];

            // Parsing the doubles from the section
            double[] vector = parseDoubles(section, 1);

            // Adding the word and its embeddings to the map
            embeddings.put(word, vector);

        } catch (Exception e) {
            System.err.println("Error processing line: " + line);
            e.printStackTrace();
        }
    }

    /*
        * This helper method is used to parse doubles from a string array
        * @param parts The string array to parse
        * @param startIndex The index to start parsing from
        * @return An array of doubles
     */
    private double[] parseDoubles(String[] parts, int startIndex) {

        // Creating a list to store the parsed doubles
        List<Double> vectorList = new ArrayList<>();

        /*
            * For each part of the line, parse the double and add it to the vectorList
            * Skip the first part as it is the word
            * If the value is not a valid double, print an error message
         */
        for (int i = startIndex; i < parts.length; i++) {
            String value = parts[i].trim();
            try {
                vectorList.add(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: [" + value + "]");
            }
        }
        // Convert the list to an array and return it
        return vectorList.stream().mapToDouble(Double::doubleValue).toArray();
    }

}
