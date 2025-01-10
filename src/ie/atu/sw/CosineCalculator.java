package ie.atu.sw;

public class CosineCalculator {

    /*
     * This method is used to calculate the cosine similarity between two vectors
     * @param vector1 The first vector
     * @param vector2 The second vector
     * @return The cosine similarity between the two vectors
     */
    public double calculate(double[] vector1, double[] vector2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        /*
         * Calculate the dot product and the norms of the two vectors
         * The cosine similarity is calculated as the dot product of the two vectors divided by the product of their norms
         * @param vector1 The first vector
         * @param vector2 The second vector
         * @return The cosine similarity between the two vectors
         */
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
