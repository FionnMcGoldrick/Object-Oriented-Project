package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class Console {


    public Console() {
        startConsole();
    }


    private void startConsole(){
        menu();
    }


    private void menu(){

        Scanner keyb = new Scanner(System.in);
        String choice;

        String embeddingsFile = "./resources/embeddings.txt";
        String googleWordsFile = "./resources/google-1000.txt";
        String outputFile = "./out.txt";
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
    * Method to begin processing the files
    * @param embeddingsFile
    * @param googleWordsFile
    * @param outputFile
    * @return void
    */
    private void beginProcessing(String embeddingsFile, String googleWordsFile, String userFile, String outputFile){

            WordEmbeddingsProccessor wep = new WordEmbeddingsProccessor();
            GoogleWordProcessor gwp = new GoogleWordProcessor();
            CommonWordFinder finder = new CommonWordFinder();
            Simplifier simplifier = new Simplifier();

            Map<String, double[]> embeddings = wep.storeFile(embeddingsFile);
            System.out.println("Embeddings: " + embeddings.size() + " words loaded.");

            List<String> googleWords = gwp.googleWords(googleWordsFile);
            System.out.println("Google Words: " + googleWords.size() + " words loaded.");

            List<String> commonWords = finder.findCommonWords(googleWords, embeddings);
            finder.writeCommonWordsWithEmbeddings(commonWords, embeddings, googleWordsFile);

            Map<String, double[]> googleWordEmbeddings = gwp.storeFile(googleWordsFile);
            System.out.println("Google Word Embeddings: " + googleWordEmbeddings.size() + " words loaded.");

            List<String> userWords = readUserFile(userFile);
            simplifier.simplify(embeddings, userWords, userFile, outputFile);

        }

        public List<String> readUserFile(String userFile){

            List<String> userWords = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.replace(".", "");
                    String[] words = line.split("\\s+");
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

