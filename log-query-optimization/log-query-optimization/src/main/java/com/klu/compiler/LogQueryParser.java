
package com.klu.compiler;

import java.util.List;

public class LogQueryParser {

    public boolean parse(List<String> tokens) {

        // Simple query:
        // FIND LOGS WHERE level = ERROR

        if (tokens.size() == 6) {

            if (!tokens.get(0).equalsIgnoreCase("FIND")) {
                return false;
            }

            if (!tokens.get(1).equalsIgnoreCase("LOGS")) {
                return false;
            }

            if (!tokens.get(2).equalsIgnoreCase("WHERE")) {
                return false;
            }

            if (!tokens.get(4).equals("=")) {
                return false;
            }

            return true;
        }

        // Query with AND:
        // FIND LOGS WHERE level = ERROR AND service = PaymentService

        if (tokens.size() == 10) {

            if (!tokens.get(0).equalsIgnoreCase("FIND")) {
                return false;
            }

            if (!tokens.get(1).equalsIgnoreCase("LOGS")) {
                return false;
            }

            if (!tokens.get(2).equalsIgnoreCase("WHERE")) {
                return false;
            }

            if (!tokens.get(4).equals("=")) {
                return false;
            }

            if (!tokens.get(6).equalsIgnoreCase("AND")) {
                return false;
            }

            if (!tokens.get(8).equals("=")) {
                return false;
            }

            return true;
        }

        return false;
    }
}
