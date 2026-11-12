package com.klu.compiler;

import java.util.ArrayList;
import java.util.List;

public class LogQueryLexer {

    public List<String> tokenize(String query) {

        List<String> tokens = new ArrayList<>();

        String[] words = query.split("\\s+");

        for (String word : words) {

            if (!word.isEmpty()) {
                tokens.add(word);
            }
        }

        return tokens;
    }
}