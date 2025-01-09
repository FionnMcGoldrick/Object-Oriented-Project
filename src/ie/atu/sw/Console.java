package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class Console {

    /*
    * Constructor for the Console class
    * Runs the startConsole method as soon as a Console object is created
    * @return void
    * @param void
     */
    public Console() {
        startConsole();
    }

    private void startConsole(){
        menu();
    }

    /*
    * Method to display the menu options and to handle user input
    * @return void
    * @param void
     */
    private void menu(){

        Scanner keyb = new Scanner(System.in);
        String choice;

        // Default file paths
        String embeddingsFile = "./resources/embeddings.txt";
        String googleWordsFile = "./resources/google-1000.txt";
        String outputFile = "./resources/output.txt";
        String userFile = "./resources/userFile.txt";

        while(true) {

            System.out.println(ConsoleColour.WHITE);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*             Virtual Threaded Text Simplifier             *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            System.out.println("(1) Specify Embeddings File (default: ./resources/embeddings.txt)");
            System.out.println("(2) Specify Google 1000 File (default: ./resources/google-1000.txt)");
            System.out.println("(3) Specify the file you want simplified (default: ./resources/google-1000.txt)");
            System.out.println("(4) Specify an Output File (default: ./out.txt)");
            System.out.println("(5) Execute, Analyse and Report");
            System.out.println("(6) Optional Extras...");
            System.out.println("(?) Quit");

            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Select Option [1-5]>");
            System.out.println();

            choice = keyb.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("You selected option 1\nPlease enter title of embeddings file:");
                    embeddingsFile = keyb.nextLine();
                    break;
                case "2":
                    System.out.println("You selected option 2\nPlease enter title of Google 1000 file:");
                    googleWordsFile = keyb.nextLine();
                    break;
                case "3":
                    System.out.println("You selected option 3\nPlease enter title of file you want simplified:");
                    userFile = keyb.nextLine();
                    break;
                case "4":
                    System.out.println("You selected option 4\nPlease enter title of output file:");
                    outputFile = keyb.nextLine();
                    break;
                case "5":
                    System.out.println("You selected option 5\nBeginning processing...");
                    beginProcessing(embeddingsFile, googleWordsFile, userFile, outputFile);
                    break;
                case "6":
                    System.out.println("You selected option 6");
                    break;
                case "?":
                    System.out.println("System exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        }

    }

   /*
    * Method to begin the processing of the files.
    * Calls the necessary classes to process the files
    * @return void
    * @param String embeddingsFile, String googleWordsFile, String userFile, String outputFile
    */
    private void beginProcessing(String embeddingsFile, String googleWordsFile, String userFile, String outputFile){

            // Create instances of the classes
            WordEmbeddingsProccessor wep = new WordEmbeddingsProccessor();
            GoogleWordProcessor gwp = new GoogleWordProcessor();
            CommonWordFinder finder = new CommonWordFinder();
            Simplifier simplifier = new Simplifier();

            /*
            * Load the embeddings file and store the embeddings in a map
            * @return Map<String, double[]>
            * @param String embeddingsFile
            * @see WordEmbeddingsProccessor
             */
            Map<String, double[]> embeddings = wep.storeFile(embeddingsFile);
            System.out.println("Embeddings: " + embeddings.size() + " words loaded.");

            /*
            * Load the google words file and store the words in a list
            * @return List<String>
            * @param String googleWordsFile
            * @see GoogleWordProcessor
             */
            List<String> googleWords = gwp.googleWords(googleWordsFile);
            System.out.println("Google Words: " + googleWords.size() + " words loaded.");

            /*
            * Find the common words between the google words and the embeddings
            * Write common words that have been found to a file
            * @return List<String>
            * @param List<String> googleWordList, Map<String, double[]> embeddingsMap
            * @see CommonWordFinder
             */
            List<String> commonWords = finder.findCommonWords(googleWords, embeddings);
            finder.writeCommonWordsWithEmbeddings(commonWords, embeddings, googleWordsFile);

            /*
            * Read the user file and store the words in a list
            * @return List<String>
            * @param String userFile
            * @see GoogleWordProcessor
             */
            Map<String, double[]> googleWordEmbeddings = gwp.storeFile(googleWordsFile);
            System.out.println("Google Word Embeddings: " + googleWordEmbeddings.size() + " words loaded.");

            /*
            * Simplify the user file using the embeddings
            * @return void
            * @param Map<String, double[]> embeddings, List<String> userWords, String userFile, String outputFile
            * @see Simplifier
             */
            List<String> userWords = readUserFile(userFile);
            simplifier.simplify(embeddings, userFile, outputFile);

        }

        /*
        * Method to read the user file and store the words in a list
        * @return List<String>
        * @param String userFile
         */
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

    }

