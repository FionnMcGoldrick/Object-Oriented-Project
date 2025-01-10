package ie.atu.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


public class Simplifier {

    // Create a new instance of the EmbeddingUtils class
    EmbeddingUtils embeddingUtils = new EmbeddingUtils();

    /*
     * This method simplifies the content of a file by replacing each word with the most similar word in the embeddings.
     * @param embeddings A map containing word embeddings
     * @param userFile The file to be simplified
     * @param outputFile The file to write the simplified content to
     * @return void
     */
    public void simplify(Map<String, double[]> embeddings, String userFile, String outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder modifiedLine = new StringBuilder();

                // Split the line into tokens, keeping punctuation and spaces
                String[] tokens = line.split("(?<=\\b)|(?=\\b)|(?=\\p{Punct})");

                for (String token : tokens) {
                    if (token.matches("\\w+")) { // Check if the token is a word
                        if (embeddings.containsKey(token)) {
                            double[] wordVector = embeddings.get(token);
                            String mostSimilarWord = embeddingUtils.findMostSimilarWord(token, wordVector, embeddings);
                            modifiedLine.append(mostSimilarWord);
                        } else {
                            modifiedLine.append(token); // Append the original token if no embedding is found
                        }
                    } else {
                        modifiedLine.append(token); // Append punctuation or spaces as-is
                    }
                }

                writer.write(modifiedLine.toString()); // Write the modified line
                writer.newLine(); // Add a new line for each input line
            }

        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
}
