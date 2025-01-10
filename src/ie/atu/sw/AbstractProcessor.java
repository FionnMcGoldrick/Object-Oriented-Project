package ie.atu.sw;

import java.util.Map;

public abstract class AbstractProcessor implements Process {

    /*
     * This method is used to process lines from the file
     * @param line The line to be processed
     * @param embeddings A map containing word embeddings
     * @return void
     * @See Interface Process
     */
    public void processLines(String line, Map<String, double[]> embeddings) {
    }

    /*
     * This method is used to store the content of a file
     * @param filePath The path to the file
     * @return A map containing word embeddings
     * @See Interface Process
     */
    public Map<String, double[]> storeFile(String filePath) {
        return null;
    }

}

