package ie.atu.sw;

import java.util.Map;

public abstract class AbstractProcessor implements Process {

    public void processLines(String line, Map<String, double[]> embeddings) {
    }

    public Map <String, double[]> storeFile(String filePath) {
        return null;
    }

}

