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

    public void processLines(String line, Map<String, double[]> embeddings) {
        try {


            String cleanedLine = line.trim().replaceAll(",+", ",").replaceAll(",$", "").replaceAll(",\\s+", " ");

            String[] section = cleanedLine.split("\\s+");

            String word = section[0];
            double[] vector = parseDoubles(section, 1);

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

}
