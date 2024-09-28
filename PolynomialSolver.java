import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PolynomialSolver {

    // Function to decode a number from a given base
    public static BigInteger decodeBase(String value, int base) {
        BigInteger result = new BigInteger(value, base);
        System.out.println("Decoded value from base " + base + ": " + result);  // Debugging
        return result;
    }

    // Function to perform Lagrange Interpolation and find constant term c
    public static BigInteger lagrangeInterpolation(List<BigInteger[]> points, int k) {
        BigInteger c = BigInteger.ZERO;
        for (int i = 0; i < k; i++) {
            BigInteger term = points.get(i)[1]; // y_i (as BigInteger)
            System.out.println("Starting term for i=" + i + " with y_i=" + term);  // Debugging
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger numerator = points.get(j)[0].negate(); // -x_j
                    BigInteger denominator = points.get(i)[0].subtract(points.get(j)[0]); // x_i - x_j
                    
                    // Debugging output to track numerator/denominator
                    System.out.println("  x_j = " + points.get(j)[0] + ", x_i = " + points.get(i)[0] + 
                        ", numerator = " + numerator + ", denominator = " + denominator);

                    // Ensure denominator is not zero (though it shouldn't be in correct input)
                    if (denominator.equals(BigInteger.ZERO)) {
                        throw new ArithmeticException("Division by zero encountered in Lagrange interpolation.");
                    }

                    // Perform term multiplication and division
                    term = term.multiply(numerator).divide(denominator); // term *= (-x_j / (x_i - x_j))
                    
                    // Debugging output for updated term
                    System.out.println("  Updated term for i=" + i + " is " + term);
                }
            }
            c = c.add(term); // Sum the terms
            
            // Debugging output for constant after each iteration
            System.out.println("Updated constant c after i=" + i + ": " + c);
        }
        return c;
    }

    public static void main(String[] args) {
        try {
            // Parse the JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("input1.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject keys = (JSONObject) jsonObject.get("keys");

            // Get n and k values
            long n = (Long) keys.get("n");
            long k = (Long) keys.get("k");

            // List to store the points
            List<BigInteger[]> points = new ArrayList<>();

            // Loop through the points in the JSON file
            for (int i = 1; i <= n; i++) {
                JSONObject point = (JSONObject) jsonObject.get(String.valueOf(i));
                if (point != null) {
                    BigInteger x = BigInteger.valueOf(i); // x is the key (1, 2, 3, ... n)
                    int base = Integer.parseInt((String) point.get("base"));
                    String value = (String) point.get("value");

                    // Decode the y value using the specified base
                    BigInteger y = decodeBase(value, base);

                    // Add the point (x, y) to the list
                    points.add(new BigInteger[]{x, y});
                }
            }

            // Use Lagrange interpolation to calculate the constant term c
            BigInteger c = lagrangeInterpolation(points, (int) k);

            // Print the result
            System.out.println("The constant term (secret c) is: " + c);

        } catch (ParseException | java.io.IOException e) {
            e.printStackTrace();
        } catch (ArithmeticException e) {
            System.out.println("Error in calculation: " + e.getMessage());
        }
    }
}
