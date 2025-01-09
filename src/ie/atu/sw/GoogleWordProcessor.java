package ie.atu.sw;

import java.io.BufferedReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GoogleWordProcessor extends AbstractProcessor{

    public Map<String, double[]> storeFile(String filePath) {

        //Create Concurrent Hashmap for storing embeddings
        Map<String, double[]> googleWordEmbeddings = new ConcurrentHashMap<>();

        //Create a new instance of the ExecutorService class
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        List<String> googleWordList = googleWords(filePath);

        System.out.println("Google Word List: " + googleWordList.size());

        //print out list of words
        for(String word : googleWordList){
            System.out.println(word);
        }


        return null;

    }

    public List <String> googleWords(String filepath){
        try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
            List<String> googleWords = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null){
                googleWords.add(line);
            }
            return googleWords;
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }


}
