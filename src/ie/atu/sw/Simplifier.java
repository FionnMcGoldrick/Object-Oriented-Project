package ie.atu.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


public class Simplifier {

    // Create a new instance of the CosineCalculator class
    CosineCalculator cosineCalculator = new CosineCalculator();

    /*
        * This method simplifies the content of a file by replacing each word with the most similar word in the embeddings.
        * @param embeddings A map containing word embeddings
        * @param userFile The file to be simplified
        * @param outputFile The file to write the simplified content to
        * @return void
     */
    public void simplify(Map<String, double[]> embeddings, String userFile, String outputFile) {

        // Try to read the userFile and write the simplified content to the outputFile
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder modifiedLine = new StringBuilder();

                // Split the line into tokens
                String[] tokens = line.split("(?<=\\b)|(?=\\b)|(?=\\p{Punct})");

                /*
                    * For each token in the line, check if it is a word.
                    * If it is a word, find the most similar word in the embeddings and append it to the modified line.
                    * If it is not a word, append it to the modified line.
                 */
                for (String token : tokens) {
                    if (token.matches("\\w+")) {
                        if (embeddings.containsKey(token)) {
                            double[] wordVector = embeddings.get(token);
                            String mostSimilarWord = findMostSimilarWord(token, wordVector, embeddings);
                            modifiedLine.append(mostSimilarWord);
                        } else {
                            modifiedLine.append(token); // Append the token as it is
                        }
                    }else {
                        modifiedLine.append(token); // Append the token as it is
                    }
                }
                // Write the modified line to the output file
                writer.write(modifiedLine.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    /*
        * This method finds the most similar word to a target word in the embeddings.
        * @param targetWord The target word
        * @param targetVector The vector representation of the target word
        * @param embeddings A map containing word embeddings
        * @return String / The most similar word to the target word
     */
    private String findMostSimilarWord(String targetWord, double[] targetVector, Map<String, double[]> embeddings) {
        String mostSimilarWord = null;
        double highestSimilarity = Double.NEGATIVE_INFINITY; // Initialize the highest similarity to negative infinity

        /*
            * For each word in the embeddings, calculate the cosine similarity between the target word and the word.
            * If the similarity is higher than the highest similarity found so far, update the highest similarity and the most similar word.
         */
        for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
            String word = entry.getKey();
            double[] vector = entry.getValue();

            // Skip the target word
            if (word.equals(targetWord)) {
                continue;
            }

            /*
                * Calculate the cosine similarity between the target word and the word.
                * If the similarity is higher than the highest similarity found so far, update the highest similarity and the most similar word.
             */
            double similarity = cosineCalculator.calculate(targetVector, vector);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                mostSimilarWord = word;
            }
        }

        return mostSimilarWord;
    }
}
