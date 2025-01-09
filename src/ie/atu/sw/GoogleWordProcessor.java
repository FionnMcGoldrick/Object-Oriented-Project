package ie.atu.sw;

import java.io.BufferedReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class GoogleWordProcessor extends AbstractProcessor{

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
        Map<String, double[]> googleWordEmbeddings = new ConcurrentHashMap<>();

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
                // Submitting the line to the executor
                executor.submit(() -> processLines(finalLine, googleWordEmbeddings));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown(); // Shutting down the executor
            try {
                // Waiting for the executor to terminate
                if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                    System.err.println("Warning: Not all tasks completed.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Interrupting the current thread
            }
        }

        return googleWordEmbeddings;
    }

    /*
     * This method is used to process lines from the file
     * Method must be implemented due to extending AbstractProcessor
     * @param line The line to be processed
     * @param googleWordEmbeddings A map containing word embeddings
     * @return void
     * @See Interface Process
     */
    public void processLines(String line, Map<String, double[]> googleWordEmbeddings) {
        try {
            // Splitting the line by whitespace
            String[] parts = line.trim().split("\\s+");

            // The first part is the word
            String word = parts[0];

            // Creating an array to store the embeddings
            double[] embeddings = new double[parts.length - 1];

            /*
                * For each part of the line, parse the double and add it to the embeddings array
                * Skip the first part as it is the word
             */
            for (int i = 1; i < parts.length; i++) {
                // Remove any trailing commas and whitespace
                String sanitizedValue = parts[i].replaceAll(",$", "").trim();
                try {
                    embeddings[i - 1] = Double.parseDouble(sanitizedValue); // Parse the double
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number [" + parts[i] + "] in line: " + line);
                    return;
                }
            }
            // Add the word and its embeddings to the map
            googleWordEmbeddings.put(word, embeddings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        * This method is used to read the words from the google words file
        * @param filepath The path to the file
        * @return A list of String (words)
     */
    public List <String> googleWords(String filepath){
        try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
            List<String> googleWords = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null){
                googleWords.add(line);
            }
            return googleWords;
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }


}
