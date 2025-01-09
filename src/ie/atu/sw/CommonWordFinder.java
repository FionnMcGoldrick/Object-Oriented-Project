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

    public List<String> findCommonWords(List<String> googleWordList, Map<String, double[]> embeddingsMap) {
        List<String> commonWords = new ArrayList<>();

        for (String word : googleWordList) {
            if (embeddingsMap.containsKey(word)) {
                commonWords.add(word);
            }
        }

        return commonWords;
    }

    /*
        * Write common words with embeddings to a file.
        * @param commonWords
        * @param embeddings
        * @param googleWordFile
        * @return void
        * Running time: O(n) where n is the number of common words
     */
    public void writeCommonWordsWithEmbeddings(List<String> commonWords, Map<String, double[]> embeddings, String googleWordFile) {
        try {
            // Load existing content of the file
            Map<String, String> existingContent = loadExistingFileContent(googleWordFile);

            // Check if all embeddings are already present
            boolean allEmbeddingsExist = true;
            for (String word : commonWords) {
                if (!existingContent.containsKey(word)) {
                    allEmbeddingsExist = false;
                    break;
                }
            }

            // If all embeddings are present, do nothing
            if (allEmbeddingsExist) {
                System.out.println("All embeddings are already present. No changes made to: " + googleWordFile);
                return;
            }

            // Write updated content (append missing embeddings)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(googleWordFile))) {
                // Write existing content back to the file
                for (Map.Entry<String, String> entry : existingContent.entrySet()) {
                    writer.write(entry.getKey() + entry.getValue());
                    writer.newLine();
                }

                // Append missing embeddings
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
        * Load existing content of the file.
        * Used to check if embeddings are already present.
        * @param filePath
        * @return Map<String, String>
        * @throws IOException
        * Running time: O(n) where n is the number of lines in the file
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
