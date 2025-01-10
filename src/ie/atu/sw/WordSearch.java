package ie.atu.sw;

import java.util.Map;

public class WordSearch {

    EmbeddingUtils embeddingUtils = new EmbeddingUtils();

    // This method is used to search for a word in the embeddings file
    // @param word The word to be searched for
    public void search(String word, String filePath) {

        //method to get the embeddings
        Map<String, double[]> embeddings = embeddingUtils.getEmbeddings(filePath);

        if (embeddings.containsKey(word)) {
            System.out.println("The word " + word + " is present in the embeddings file");

            // Get the word vector for the given word
            double[] wordVector = embeddings.get(word);
            embeddingUtils.findTopFiveSimilarWords(word, wordVector, embeddings);

        } else {
            System.out.println("The word " + word + " is not present in the embeddings file");
        }

    }

}
