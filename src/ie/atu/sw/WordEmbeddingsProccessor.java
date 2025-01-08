package ie.atu.sw;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class WordEmbeddingsProccessor extends AbstractProcessor {

    //method for storing embeddings in files
    public HashMap<String, double[]> storeFile(String filePath) {

        //Create a new HashMap to store the embeddings
        HashMap<String, double[]> embeddings = new HashMap<String, double[]>();

        //Create a new VirtualThreadPerTaskExecutor
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            //Loop through the file and store the embeddings
            while ((line = br.readLine()) != null) {
                String finalLine = line; //final variable for lambda
                executor.submit(() -> {
                    processLines(finalLine);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return embeddings;

    }

    //method for processing lines
    public HashMap<String, double[]> processLines(String line, HashMap<String, double[]> embeddings) {

        String[] word;
        String[] parts = line.split("\\s+");

        //looping through the the lines and storing the embeddings
        for (int i = 1; i < parts.length; i++) {
            word = parts[i].split(",");
            double[] embedding = new double[word.length - 1];
            for (int j = 1; j < word.length; j++) {
                embedding[j - 1] = Double.parseDouble(word[j]);
            }
            embeddings.put(word[0], embedding);
        }
        return embeddings;
    }


}
