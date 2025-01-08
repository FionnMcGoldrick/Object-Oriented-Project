package ie.atu.sw;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordEmbeddingsProccessor extends AbstractProcessor {

    //method for storing embeddings in files
    public HashMap<String, double[]> storeFile(String filePath) {

        //Create a new HashMap to store the embeddings
        HashMap<String, double[]> embeddings = new HashMap<String, double[]>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            //Loop through the file and store the embeddings
            while (line != null) {
                processLines(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return embeddings;

    }

    //method for processing lines
    public void processLines(String line){
    }


}
