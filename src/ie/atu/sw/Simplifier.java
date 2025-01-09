package ie.atu.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Simplifier {

    CosineCalculator cosineCalculator = new CosineCalculator();

    /*
     * Simplify method to replace a word with its most similar word
     * and append the result to the output file.
     */
    public void simplify(Map<String, double[]> embeddings, Map<String, double[]> googleWordsEmbeddings, List<String> userWords, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) { // Open file in append mode
            for (String word : userWords) {
                if (embeddings.containsKey(word)) {
                    System.out.println("'" + word + "' found in embeddings");

                    double[] wordVector = embeddings.get(word);

                    // Find the most similar word
                    String mostSimilarWord = findMostSimilarWord(word, wordVector, embeddings);

                    // Replace or log the replacement
                    System.out.println("Replaced '" + word + "' with most similar word: '" + mostSimilarWord + "'");

                    // Append the replaced word to the file
                    writer.write(mostSimilarWord);
                } else {
                    System.out.println("'" + word + "' not found in embeddings");

                    // Append the original word to the file if not found
                    writer.write(word);
                }

                writer.write(" "); // Ensure proper spacing between words
            }

            writer.newLine(); // Add a newline after processing the line
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /*
     * Find the most similar word based on cosine similarity
     * @param targetWord
     * @param targetVector
     * @param embeddings
     * @return String
     */
    private String findMostSimilarWord(String targetWord, double[] targetVector, Map<String, double[]> embeddings) {
        String mostSimilarWord = null;
        double highestSimilarity = Double.NEGATIVE_INFINITY;

        for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
            String word = entry.getKey();
            double[] vector = entry.getValue();

            // Skip the target word itself
            if (word.equals(targetWord)) {
                continue;
            }

            // Calculate cosine similarity
            double similarity = cosineCalculator.calculate(targetVector, vector);

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                mostSimilarWord = word;
            }
        }

        return mostSimilarWord;
    }
}
