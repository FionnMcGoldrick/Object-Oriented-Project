package ie.atu.sw;

import java.util.List;
import java.util.Map;


public class Simplifier {

    /*
        * Method to simplify embeddings
        * @param embeddings
        * @param googleWordsEmbeddings
        * @param lines
        * @return void
     */
    public void simplify(Map<String, double[]> embeddings, Map<String, double[]> googleWordsEmbeddings, List<String> userWords, String outputFile) {

        //look through list of userWords and check if they are in the embeddings
        for (String word : userWords) {
            if (embeddings.containsKey(word)) {
                //print out the word and its embeddings
                System.out.println(word + " found in embeddings");
            }else{
                System.out.println(word + " not found in embeddings");
            }
        }


    }



}
