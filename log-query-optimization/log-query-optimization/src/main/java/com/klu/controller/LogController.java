package com.klu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klu.compiler.LogQueryLexer;
import com.klu.compiler.LogQueryParser;
import com.klu.compiler.LogQuerySemanticAnalyzer;
import com.klu.compiler.LogQueryOptimizer;
import com.klu.model.Log;
import com.klu.repository.LogRepository;

@RestController
@RequestMapping("/logs")
public class LogController {

    private LogRepository logRepository;

    public LogController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    @PostMapping
    public Log addLog(@RequestBody Log log) {
        return logRepository.save(log);
    }

    @GetMapping("/search")
    public List<Log> searchLogs(@RequestParam String keyword) {
        return logRepository.findByMessageContainingIgnoreCase(keyword);
    }

    @GetMapping("/level")
    public List<Log> getLogsByLevel(@RequestParam String level) {
        return logRepository.findByLevel(level);
    }

    @GetMapping("/service")
    public List<Log> getLogsByService(@RequestParam String service) {
        return logRepository.findByService(service);
    }

    @GetMapping("/count")
    public Map<String, Integer> countLogsByLevel() {

        return Map.of(
            "ERROR", logRepository.findByLevel("ERROR").size(),
            "INFO", logRepository.findByLevel("INFO").size(),
            "WARNING", logRepository.findByLevel("WARNING").size()
        );
    }

    @GetMapping("/performance")
    public Map<String, Object> measurePerformance() {

        long startTime = System.nanoTime();

        List<Log> logs = logRepository.findByLevel("ERROR");

        long endTime = System.nanoTime();

        long executionTime = endTime - startTime;

        return Map.of(
            "query", "Find ERROR logs",
            "recordsFound", logs.size(),
            "executionTimeNanoseconds", executionTime
        );
    }

    @PostMapping("/process")
    public Map<String, Object> processQuery(
            @RequestBody Map<String, String> request) {

        String query = request.get("query");

        LogQueryLexer lexer = new LogQueryLexer();
        LogQueryParser parser = new LogQueryParser();
        LogQuerySemanticAnalyzer semanticAnalyzer =
                new LogQuerySemanticAnalyzer();
        LogQueryOptimizer optimizer =
                new LogQueryOptimizer();

        // Lexical Analysis
        List<String> tokens = lexer.tokenize(query);

        // Syntax Analysis
        boolean syntaxValid = parser.parse(tokens);

        // Semantic Analysis
        boolean semanticValid = false;

        if (syntaxValid) {
            semanticValid = semanticAnalyzer.analyze(tokens);
        }

        // Query Optimization
        List<String> optimizedTokens = new ArrayList<>();

        if (semanticValid) {
            optimizedTokens = optimizer.optimize(tokens);
        }

        // Execute optimized query
        List<Log> results = new ArrayList<>();

        if (semanticValid) {

            // Simple query
            if (optimizedTokens.size() == 6) {

                String field = optimizedTokens.get(3);
                String value = optimizedTokens.get(5);

                if (field.equalsIgnoreCase("level")) {

                    results = logRepository.findByLevel(value);

                } else if (field.equalsIgnoreCase("service")) {

                    results = logRepository.findByService(value);

                } else if (field.equalsIgnoreCase("message")) {

                    results =
                            logRepository.findByMessageContainingIgnoreCase(value);
                }
            }

            // Query with AND
            else if (optimizedTokens.size() == 10) {

                String field1 = optimizedTokens.get(3);
                String value1 = optimizedTokens.get(5);

                String field2 = optimizedTokens.get(7);
                String value2 = optimizedTokens.get(9);

                if (field1.equalsIgnoreCase("level")
                        && field2.equalsIgnoreCase("service")) {

                    results =
                            logRepository.findByLevelAndService(
                                    value1, value2);
                }
            }
        }

        return Map.of(
                "query", query,
                "tokens", tokens,
                "syntaxValid", syntaxValid,
                "semanticValid", semanticValid,
                "optimizedTokens", optimizedTokens,
                "results", results
        );
    }
}