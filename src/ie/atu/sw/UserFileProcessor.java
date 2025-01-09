package ie.atu.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserFileProcessor {

    Scanner keyb = new Scanner(System.in);
    EmbeddingUtils embeddingUtils = new EmbeddingUtils();

    public List<String> readUserFile(String userFile){

        // Create a list to store the words
        List<String> userWords = new ArrayList<>();

        /*
         * Read the user file with BufferReader and store the words in a list
         * @return List<String>
         * @param String userFile
         */
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(".", ""); // Remove full stops
                String[] words = line.split("\\s+"); // Split the line into words
                for (String word : words) {
                    userWords.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userWords;
    }


    public void writeUserFile(String DIRECTORY, String TXT){

        System.out.println("---------------------------------");
        System.out.print("File Title: ");

        String title = DIRECTORY + keyb.nextLine() + TXT;

        embeddingUtils.sleep(250);

        File file = handleFileCreation(title);
        writeToUserFile(file);

        System.out.println("\n---------------------------------");

    }

    private File handleFileCreation(String title){

        File file = new File(title);
        try{
            if(file.createNewFile()){
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e){
            System.out.println("Error: " + e);
        }

        return file;
    }

    private void writeToUserFile(File file){

        try(FileWriter writer = new FileWriter(file)){
            System.out.println("---------------------------------");
            System.out.println("Enter text to write to file: ");
            String text = keyb.nextLine();
            writer.write(text);
            System.out.println("Text written to file.\n");
        } catch (IOException e){
            System.out.println("Error: " + e);
        }
    }
}
