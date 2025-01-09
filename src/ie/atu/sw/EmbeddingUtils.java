package ie.atu.sw;

import java.util.Map;

public class EmbeddingUtils {

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

        return mostSimilarWord;
    }
}
