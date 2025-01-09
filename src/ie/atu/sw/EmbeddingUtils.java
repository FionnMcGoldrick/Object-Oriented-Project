package ie.atu.sw;

import java.util.Map;

public class EmbeddingUtils {

    /*
    Similarity threshold for finding the most similar word
    Ensures words aren't replaced with words with similarity scores below the threshold
     */
    private static final double SIMILARITY_THRESHOLD = 0.7;


    private static final CosineCalculator cosineCalculator = new CosineCalculator();

    public static String findMostSimilarWord(String targetWord, double[] targetVector, Map<String, double[]> embeddings) {
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

        // Return the most similar word only if it meets the threshold
        if (highestSimilarity >= SIMILARITY_THRESHOLD) {
            return mostSimilarWord;
        } else {
            return targetWord; // Fallback to the original word
        }
    }


    //method that clears the terminal
   /*public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }*/

    //sleep method
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.err.println("Error sleeping: " + e.getMessage());
        }
    }
}
