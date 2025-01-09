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

    public Map<String, double[]> storeFile(String filePath) {

        Map<String, double[]> googleWordEmbeddings = new ConcurrentHashMap<>();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String finalLine = line;
                executor.submit(() -> processLines(finalLine, googleWordEmbeddings));
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

        return googleWordEmbeddings;
    }


    public void processLines(String line, Map<String, double[]> googleWordEmbeddings) {
        try {
            String[] parts = line.trim().split("\\s+");

            if (parts.length < 2) {
                System.err.println("Skipping malformed line: " + line);
                return;
            }

            String word = parts[0];
            double[] embeddings = new double[parts.length - 1];

            for (int i = 1; i < parts.length; i++) {
                String sanitizedValue = parts[i].replaceAll(",$", "").trim();
                try {
                    embeddings[i - 1] = Double.parseDouble(sanitizedValue);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number [" + parts[i] + "] in line: " + line);
                    return;
                }
            }

            googleWordEmbeddings.put(word, embeddings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
