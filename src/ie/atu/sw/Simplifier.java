package ie.atu.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.List;

public class Simplifier {

    CosineCalculator cosineCalculator = new CosineCalculator();

    public void simplify(Map<String, double[]> embeddings, List<String> userWords, String userFile, String outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder modifiedLine = new StringBuilder();

                String[] tokens = line.split("(?<=\\b)|(?=\\b)|(?=\\p{Punct})");

                for (String token : tokens) {
                    if (token.matches("\\w+")) {
                        if (embeddings.containsKey(token)) {
                            double[] wordVector = embeddings.get(token);
                            String mostSimilarWord = findMostSimilarWord(token, wordVector, embeddings);
                            modifiedLine.append(mostSimilarWord);
                        } else {
                            modifiedLine.append(token);
                        }
                    } else {
                        modifiedLine.append(token);
                    }
                }

                writer.write(modifiedLine.toString());
                writer.newLine();
            }

            System.out.println("Simplified content written to: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    private String findMostSimilarWord(String targetWord, double[] targetVector, Map<String, double[]> embeddings) {
        String mostSimilarWord = null;
        double highestSimilarity = Double.NEGATIVE_INFINITY;

        for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
            String word = entry.getKey();
            double[] vector = entry.getValue();

            if (word.equals(targetWord)) {
                continue;
            }

            double similarity = cosineCalculator.calculate(targetVector, vector);

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                mostSimilarWord = word;
            }
        }

        return mostSimilarWord;
    }
}
