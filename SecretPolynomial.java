import java.io.FileReader;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SecretPolynomial {

    // Function to decode a number from a given base
    public static long decodeBase(String value, int base) {
        return Long.parseLong(value, base);
    }

    // Function to perform Lagrange Interpolation and find constant term c
    public static double lagrangeInterpolation(List<int[]> points, int k) {
        double c = 0.0;
        for (int i = 0; i < k; i++) {
            double term = points.get(i)[1]; // y_i
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0.0 - points.get(j)[0]) / (double)(points.get(i)[0] - points.get(j)[0]);
                }
            }
            c += term;
        }
        return c;
    }

    public static void main(String[] args) {
        try {
            // Parse JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("input.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject keys = (JSONObject) jsonObject.get("keys");

            // Get n and k values
            long n = (Long) keys.get("n");
            long k = (Long) keys.get("k");

            // List to store the points
            List<int[]> points = new ArrayList<>();

            // Loop through the points in the JSON file
            for (int i = 1; i <= n; i++) {
                JSONObject point = (JSONObject) jsonObject.get(String.valueOf(i));
                if (point != null) {
                    int x = i; // x is the key (1, 2, 3, ... n)
                    int base = Integer.parseInt((String) point.get("base"));
                    String value = (String) point.get("value");

                    // Decode the y value using the specified base
                    long y = decodeBase(value, base);

                    // Add the point (x, y) to the list
                    points.add(new int[]{x, (int) y});
                }
            }

            // Use Lagrange interpolation to calculate the constant term c
            double c = lagrangeInterpolation(points, (int) k);

            // Print the result
            System.out.println("The constant term c is: " + c);

        } catch (ParseException | java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
