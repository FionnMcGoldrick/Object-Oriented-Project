package ie.atu.sw;

import java.util.List;
import java.util.Map;

public class Simplifier {

    CosineCalculator cosineCalculator = new CosineCalculator();

    /*
     * Simplify method to replace a word with its most similar word
     * @param embeddings
     * @param googleWordsEmbeddings
     * @param userWords
     * @return void
     */
    public void simplify(Map<String, double[]> embeddings, Map<String, double[]> googleWordsEmbeddings, List<String> userWords, String outputFile) {

        for (String word : userWords) {
            if (embeddings.containsKey(word)) {
                System.out.println("'" + word + "' found in embeddings");

                double[] wordVector = embeddings.get(word);

                // Find the most similar word
                String mostSimilarWord = findMostSimilarWord(word, wordVector, embeddings);

                // Replace or log the replacement
                System.out.println("Replaced '" + word + "' with most similar word: '" + mostSimilarWord + "'");
            } else {
                System.out.println("'" + word + "' not found in embeddings");
            }
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

        // Iterate over all words in the embeddings
        for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
            String word = entry.getKey();
            double[] vector = entry.getValue();

            // Skip the target word itself
            if (word.equals(targetWord)) {
                continue;
            }

            // Calculate cosine similarity
            double similarity = cosineCalculator.calculate(targetVector, vector);

            // Update the most similar word if higher similarity is found
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                mostSimilarWord = word;
            }
        }

        return mostSimilarWord;
    }
}
