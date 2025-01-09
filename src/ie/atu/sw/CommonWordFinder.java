package ie.atu.sw;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class CommonWordFinder {

    /*
     * This method is used to find common words between the Google word list and the embeddings map
     * @param googleWordList The list of words from the Google word file
     * @param embeddingsMap A map containing word embeddings
     * @return A list of common words
     */
    public List<String> findCommonWords(List<String> googleWordList, Map<String, double[]> embeddingsMap) {
        List<String> commonWords = new ArrayList<>();

        // Check if the embeddings map contains the words from the Google word list
        for (String word : googleWordList) {
            if (embeddingsMap.containsKey(word)) {
                commonWords.add(word);
            }
        }

        return commonWords;
    }

    /*
     * This method is used to write common words with embeddings to a file
     * @param commonWords A list of common words
     * @param embeddings A map containing word embeddings
     * @param googleWordFile The file to write the common words to
     * @return void
     */
    public void writeCommonWordsWithEmbeddings(List<String> commonWords, Map<String, double[]> embeddings, String googleWordFile) {
        try {

            // Load the existing content of the file
            Map<String, String> existingContent = loadExistingFileContent(googleWordFile);

            // Check if all embeddings are already present
            boolean allEmbeddingsExist = true;
            for (String word : commonWords) {
                if (!existingContent.containsKey(word)) {
                    allEmbeddingsExist = false;
                    break;
                }
            }
            if (allEmbeddingsExist) {
                System.out.println("All embeddings are already present. No changes made to: " + googleWordFile);
                return;
            }

            /*
                * Write the common words with embeddings to the file
                * If the word is not already present in the file, add it with its embeddings
             */
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(googleWordFile))) {
                // Write the existing content of the file
                for (Map.Entry<String, String> entry : existingContent.entrySet()) {
                    writer.write(entry.getKey() + entry.getValue()); // Write the word and its embeddings
                    writer.newLine();
                }

                /*
                    * For each common word, check if it is already present in the file.
                    * If it is not present, add it with its embeddings.
                 */
                for (String word : commonWords) {
                    if (!existingContent.containsKey(word)) {
                        double[] vector = embeddings.get(word);
                        if (vector != null) {
                            writer.write(word + ", ");
                            for (int i = 0; i < vector.length; i++) {
                                writer.write(String.valueOf(vector[i]));
                                if (i < vector.length - 1) {
                                    writer.write(", ");
                                }
                            }
                            writer.newLine();
                        }
                    }
                }
                System.out.println("Updated " + googleWordFile + " with missing embeddings.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * This method is used to load the existing content of a file
     * @param filePath The path to the file
     * @return A map containing the existing content of the file
     */
    private Map<String, String> loadExistingFileContent(String filePath) throws IOException {
        Map<String, String> content = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length > 1) {
                    content.put(parts[0].trim(), "," + parts[1]);
                }
            }
        }
        return content;
    }


}
