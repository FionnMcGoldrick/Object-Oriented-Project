package ie.atu.sw;

import java.util.Scanner;

public class Console {

    //Constructor
    public Console() {
        startConsole();
    }

    //Method to start the console
    private void startConsole(){
        menu();
    }

    //Method to display the menu and take user choice
    private void menu(){

        Scanner keyb = new Scanner(System.in);
        String choice;

        String embeddingsFile = "./resources/embeddings.txt"; //default embeddings file
        String googleWordsFile = "./resources/google-10000.txt"; //default google words file
        String outputFile = "./out.txt"; //default output file

        //Directory for Resources
        final String DIRECTORY = "./resources/";


        while(true) {

            //You should put the following code into a menu or Menu class
            System.out.println(ConsoleColour.WHITE);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*             Virtual Threaded Text Simplifier             *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            System.out.println("(1) Specify Embeddings File");
            System.out.println("(2) Specify Google 1000 File");
            System.out.println("(3) Specify an Output File (default: ./out.txt)");
            System.out.println("(4) Execute, Analyse and Report");
            System.out.println("(5) Optional Extras...");
            System.out.println("(?) Quit");

            //Output a menu of options and solicit text from the user
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Select Option [1-4]>");
            System.out.println();

            //take user choice
            choice = keyb.nextLine();

            //switch statement to handle user choice
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
                    System.out.println("You selected option 3\nPlease enter title of output file:");
                    outputFile = keyb.nextLine();
                    break;
                case "4":
                    System.out.println("You selected option 4\nBeginning processing...");
                    beginProcessing(embeddingsFile, googleWordsFile, outputFile);
                    break;
                case "5":
                    System.out.println("You selected option 5");
                    break;
                case "?":
                    System.out.println("You selected option ?");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        }

    }


    //Method to begin processing
    private void beginProcessing(String embeddingsFile, String googleWordsFile, String outputFile){
        //Create new instance of WordEmbeddingsProccessor
        WordEmbeddingsProccessor wep = new WordEmbeddingsProccessor();

        //Begin storing embeddings in files
        wep.storeFile(embeddingsFile);

    }



}
