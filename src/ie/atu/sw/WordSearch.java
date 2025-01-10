package ie.atu.sw;

import java.util.Map;

public class WordSearch {

    EmbeddingUtils embeddingUtils = new EmbeddingUtils();

    /*
     * Method to search for a word in the embeddings file
     * @param word The word to search for
     * @param filePath The path to the embeddings file
     * @return void
     */
    public void search(String word, String filePath) {

        //method to get the embeddings
        Map<String, double[]> embeddings = embeddingUtils.getEmbeddings(filePath);


        if (embeddings.containsKey(word)) {
            System.out.println("The word " + word + " is present in the embeddings file");

            // Get the word vector for the given word
            double[] wordVector = embeddings.get(word);
            String[] topFiveWords = embeddingUtils.findTopFiveSimilarWords(word, wordVector, embeddings);

            //method to print top 5 similar words
            printTopFiveWords(topFiveWords);

        } else {
            System.out.println("The word " + word + " is not present in the embeddings file");
        }

    }

    /*
     * Method to print the top 5 similar words
     * @param topFiveWords An array containing the top 5 similar words
     * @return void
     */
    public void printTopFiveWords(String[] topFiveWords) {
        System.out.println("The top 5 similar words are:");
        //change console color to grey
        System.out.println(ConsoleColour.CYAN);
        for (String word : topFiveWords) {
            System.out.println("> " + word);
            embeddingUtils.sleep(250);
        }
        //reset console color
        System.out.println(ConsoleColour.YELLOW);
    }

}
