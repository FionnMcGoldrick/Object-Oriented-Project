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

    public void writeCommonWordsWithEmbeddings(List<String> commonWords, Map<String, double[]> embeddings, String googleWordFile) {
        try {

            Map<String, String> existingContent = loadExistingFileContent(googleWordFile);

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

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(googleWordFile))) {
                for (Map.Entry<String, String> entry : existingContent.entrySet()) {
                    writer.write(entry.getKey() + entry.getValue());
                    writer.newLine();
                }

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
