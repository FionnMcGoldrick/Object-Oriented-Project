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
    public String findMostSimilarWord(String targetWord, double[] targetVector, Map<String, double[]> embeddings) {
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

    //method that returns the top 5 closest words
    public String[] findTopFiveSimilarWords(String targetWord, double[] targetVector, Map<String, double[]> embeddings) {
        String[] top5SimilarWords = new String[5];
        double[] top5Similarity = new double[5];
        for (int i = 0; i < 5; i++) {
            top5Similarity[i] = Double.NEGATIVE_INFINITY;
        }

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
            for (int i = 0; i < 5; i++) {
                if (similarity > top5Similarity[i]) {
                    for (int j = 4; j > i; j--) {
                        top5Similarity[j] = top5Similarity[j - 1];
                        top5SimilarWords[j] = top5SimilarWords[j - 1];
                    }
                    top5Similarity[i] = similarity;
                    top5SimilarWords[i] = word;
                    break;
                }
            }
        }

        return top5SimilarWords;
    }

    //sleep method
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.err.println("Error sleeping: " + e.getMessage());
        }
    }

    //method that gets embeddings hashmap
    public Map<String, double[]> getEmbeddings(String filepath) {
        WordEmbeddingsProcessor wordEmbeddingsProcessor = new WordEmbeddingsProcessor();
        return wordEmbeddingsProcessor.storeFile(filepath);
    }

}
