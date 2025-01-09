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
import java.util.Arrays;

public class WordEmbeddingsProccessor extends AbstractProcessor {


    /*
        * Method to store embeddings in a concurrent hashmap
        * @param filePath
        * @return Map<String, double[]>
     */
    public Map<String, double[]> storeFile(String filePath) {
        Map<String, double[]> embeddings = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String finalLine = line;
                executor.submit(() -> processLines(finalLine, embeddings));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            try {
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
        * Method to process lines from a file
        * @param line
        * @param embeddings
        * @return void
     */
    public void processLines(String line, Map<String, double[]> embeddings) {
        try {
            // Skip blank lines
            if (line.trim().isEmpty()) {
                System.err.println("Skipping blank line.");
                return;
            }

            // Clean the line
            String cleanedLine = line.trim().replaceAll(",+", ",").replaceAll(",$", "").replaceAll(",\\s+", " ");

            // Split into word and vector
            String[] section = cleanedLine.split("\\s+");
            if (section.length < 2) {
                System.err.println("Skipping malformed line: " + cleanedLine);
                return;
            }

            String word = section[0];
            double[] vector = parseDoubles(section, 1);

            // Log and add to the map
            if (vector.length > 0) {
                embeddings.put(word, vector);
            } else {
                System.err.println("Skipping word due to empty vector: " + word);
            }
        } catch (Exception e) {
            System.err.println("Error processing line: " + line);
            e.printStackTrace();
        }
    }



    /*
        * Help method to parse doubles from a string array
        * @param parts
        * @param startIndex
        * @return double[]
     */
    private double[] parseDoubles(String[] parts, int startIndex) {
        List<Double> vectorList = new ArrayList<>();
        for (int i = startIndex; i < parts.length; i++) {
            String value = parts[i].trim();
            try {
                vectorList.add(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: [" + value + "]");
            }
        }
        return vectorList.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /*
        * Method to print embeddings to the console
        * @param embeddings
        * @return void
        * Used for debugging purposes
     */
    public void printEmbeddings(Map<String, double[]> embeddings) {

        embeddings.forEach((word, vector) -> {
            System.out.print(word + " -> [");
            for (int i = 0; i < vector.length; i++) {
                System.out.print(vector[i]);
                if (i < vector.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        });
    }

}
