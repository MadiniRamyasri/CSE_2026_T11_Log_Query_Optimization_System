package com.klu.compiler;

import java.util.ArrayList;
import java.util.List;

public class LogQueryOptimizer {

    public List<String> optimize(List<String> tokens) {

        // Simple query
        if (tokens.size() == 6) {
            return new ArrayList<>(tokens);
        }

        // Query with AND
        // FIND LOGS WHERE level = ERROR AND service = PaymentService

        if (tokens.size() == 10) {

            String field1 = tokens.get(3);
            String value1 = tokens.get(5);

            String field2 = tokens.get(7);
            String value2 = tokens.get(9);

            // Check whether both conditions are exactly the same
            if (field1.equalsIgnoreCase(field2)
                    && value1.equalsIgnoreCase(value2)) {

                List<String> optimized = new ArrayList<>();

                optimized.add("FIND");
                optimized.add("LOGS");
                optimized.add("WHERE");
                optimized.add(field1);
                optimized.add("=");
                optimized.add(value1);

                return optimized;
            }
        }

        return new ArrayList<>(tokens);
    }
}

