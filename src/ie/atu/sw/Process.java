package ie.atu.sw;

import java.util.Map;

public interface Process {

    /*
     * This method is used to process lines from the file
     * Ensures all Processors have a common method to process lines from the file
     * @param line The line to be processed
     * @param embeddings A map containing word embeddings
     * @return void
     */
    void processLines(String line, Map<String, double[]> embeddings);

    /*
     * This method is used to store the content of a file
     * Ensures all Processors have a common method to store the content of a file
     * @param filePath The path to the file
     * @return A map containing word embeddings
     */
    Map<String, double[]> storeFile(String filePath);

}
