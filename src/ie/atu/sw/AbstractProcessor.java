package ie.atu.sw;

import java.util.Map;

public abstract class AbstractProcessor implements Process {

    //method for processing lines
    public void processLines(String line, Map<String, double[]> embeddings) {
    }

    //abstract method for storing embeddings in files
    public Map <String, double[]> storeFile(String filePath) {
        return null;
    }

}

