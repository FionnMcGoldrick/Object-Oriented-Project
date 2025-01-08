package ie.atu.sw;

import java.util.HashMap;

public abstract class AbstractProcessor implements Process {

    //method for processing lines
    public void processLines(String line) {
    }

    //abstract method for storing embeddings in files
    public HashMap<String, double[]> storeFile(String filePath) {
        return null;
    }

}

