package ie.atu.sw;

import java.util.Scanner;
import java.util.Map;
import java.util.List;

public class Console {

    EmbeddingUtils embeddingUtils = new EmbeddingUtils();
    UserFileProcessor userProcessor = new UserFileProcessor();
    WordSearch wordSearch = new WordSearch();

    // final strings for the directory and file types
    final String DIRECTORY = "./resources/";
    final String TXT = ".txt";

    // Default file paths
    String embeddingsFile = DIRECTORY + "embeddings" + TXT;
    String googleWordsFile = DIRECTORY + "google-1000" + TXT;
    String outputFile = DIRECTORY + "output" + TXT;
    String userFile = DIRECTORY + "userFile" + TXT;


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



        while(true) {

            System.out.println(ConsoleColour.WHITE);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*             Virtual Threaded Text Simplifier             *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            embeddingUtils.sleep(250);
            System.out.println("(1) Specify Embeddings File (default: ./resources/embeddings.txt)");
            embeddingUtils.sleep(250);
            System.out.println("(2) Specify Google 1000 File (default: ./resources/google-1000.txt)");
            embeddingUtils.sleep(250);
            System.out.println("(3) Specify the file you want simplified (default: ./resources/google-1000.txt)");
            embeddingUtils.sleep(250);
            System.out.println("(4) Specify an Output File (default: ./output.txt)");
            embeddingUtils.sleep(250);
            System.out.println("(5) Execute, Analyse and Report");
            embeddingUtils.sleep(250);
            System.out.println("(6) Create and write a file to be simplified");
            embeddingUtils.sleep(250);
            System.out.println("(?) Quit");

            embeddingUtils.sleep(250);
            System.out.print(ConsoleColour.YELLOW_BOLD);
            System.out.print("\nSelect Option [1-6]\n\nENTER: ");

            choice = keyb.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("\nYou selected option 1\nPlease enter title of embeddings file\nENTER: ");
                    embeddingsFile = DIRECTORY + keyb.nextLine() + TXT;
                    break;
                case "2":
                    System.out.print("\nYou selected option 2\nPlease enter title of Google 1000 file\nENTER: ");
                    googleWordsFile = DIRECTORY + keyb.nextLine() + TXT;
                    break;
                case "3":
                    System.out.print("\nYou selected option 3\nPlease enter title of file you want simplified:\nENTER: ");
                    userFile = DIRECTORY + keyb.nextLine() + TXT;
                    break;
                case "4":
                    System.out.print("\nYou selected option 4\nPlease enter title of output file\nENTER: ");
                    outputFile = DIRECTORY + keyb.nextLine() + TXT;
                    break;
                case "5":
                    System.out.print("\n\nBeginning processing... \n\n");
                    embeddingUtils.sleep(250);
                    beginProcessing(embeddingsFile, googleWordsFile, userFile, outputFile);
                    break;
                case "6":
                    System.out.println("You selected option 6");
                    userProcessor.writeUserFile(DIRECTORY, TXT);
                    break;
                    case "7":
                    System.out.println("You selected option 7");
                    handleSearch();
                    break;
                case "?":
                    System.out.println("System exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            embeddingUtils.sleep(1000);
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
            WordEmbeddingsProcessor wep = new WordEmbeddingsProcessor();
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
            * @param Map<String, double[]> embeddings, String userFile, String outputFile
            * @see Simplifier
             */
            simplifier.simplify(embeddings, userFile, outputFile);

        }

            /*
            * Method to handle the search functionality
            * @return void
            * @param void
            * @see WordSearch
             */
            private void handleSearch(){
                Scanner keyb = new Scanner(System.in);
                String searchWord;
                System.out.print("Enter the word you want to search for: ");
                searchWord = keyb.nextLine();
                wordSearch.search(searchWord, embeddingsFile);
        }


    }

