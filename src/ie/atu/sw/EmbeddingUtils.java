package ie.atu.sw;

import java.util.Map;

public class EmbeddingUtils {

    /*
    Similarity threshold for finding the most similar word
    Ensures words aren't replaced with words with similarity scores below the threshold
     */
    private static final double SIMILARITY_THRESHOLD = 0.7;


    private static final CosineCalculator cosineCalculator = new CosineCalculator();

    /*
    * This method finds the most similar word to a target word in the embeddings.
    * @param targetWord The target word
    * @param targetVector The vector representation of the target word
    * @param embeddings A map containing word embeddings
    * @return String / The most similar word to the target word
     */
    public static String findMostSimilarWord(String targetWord, double[] targetVector, Map<String, double[]> embeddings) {
        String mostSimilarWord = null;
        double highestSimilarity = Double.NEGATIVE_INFINITY;

        // For each word in the embeddings, calculate the cosine similarity between the target word and the word
        for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
            String word = entry.getKey();
            double[] vector = entry.getValue();

            if (word.equals(targetWord)) {
                continue;
            }

            // Calculate the cosine similarity between the target word and the word
            double similarity = cosineCalculator.calculate(targetVector, vector);

            // Update the highest similarity and the most similar word if the similarity is higher than the highest similarity found so far
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                mostSimilarWord = word;
            }
        }

        // Return the most similar word only if it meets the threshold
        if (highestSimilarity >= SIMILARITY_THRESHOLD) {
            return mostSimilarWord;
        } else {
            return targetWord; // Fallback to the original word
        }
    }

    //sleep method
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.err.println("Error sleeping: " + e.getMessage());
        }
    }
}
