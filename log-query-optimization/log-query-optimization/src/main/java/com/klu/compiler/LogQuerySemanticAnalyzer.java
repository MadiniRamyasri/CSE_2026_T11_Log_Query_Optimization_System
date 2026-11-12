
package com.klu.compiler;

import java.util.Arrays;
import java.util.List;

public class LogQuerySemanticAnalyzer {

    public boolean analyze(List<String> tokens) {

        // Simple query
        // FIND LOGS WHERE level = ERROR

        if (tokens.size() == 6) {

            String field = tokens.get(3);

            return isValidField(field);
        }

        // Query with AND
        // FIND LOGS WHERE level = ERROR AND service = PaymentService

        if (tokens.size() == 10) {

            String field1 = tokens.get(3);
            String field2 = tokens.get(7);

            return isValidField(field1)
                    && isValidField(field2);
        }

        return false;
    }

    private boolean isValidField(String field) {

        List<String> validFields = Arrays.asList(
                "level",
                "service",
                "message"
        );

        return validFields.contains(field.toLowerCase());
    }
}
