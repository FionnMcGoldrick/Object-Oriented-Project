package ie.atu.sw;

import java.util.HashMap;

public abstract class AbstractProcessor implements Process{

    //method for processing lines
    public void processLine(String filePath, String line){
    }

    //abstract method for storing embeddings in files
    public abstract HashMap<String, double[]> store();


    }

