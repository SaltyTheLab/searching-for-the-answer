package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedIndex {
    // defining stop words
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "and", "or", "but", "if", "in", "on", "with",
            "as", "at", "by", "for", "of", "to", "from", "this", "that", "is",
            "are", "was", "were", "it", "they", "he", "she", "we", "you", "i",
            "me", "my", "mine", "your", "yours", "their", "his", "her", "them",
            "be", "been", "being", "do", "does", "did", "will", "would", "can",
            "could", "should", "shall", "has", "have", "had", "not", "no", "yes"));

    public static void buildInvertedIndex(String inputFilePath, String outputFilePath) throws IOException {
        Map<String, Set<Integer>> index = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath))) {
            String line;
            int lineNumber = 1;

            Pattern wordPattern = Pattern.compile("\\b\\w+\\b");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = wordPattern.matcher(line.toLowerCase());
                while (matcher.find()) {
                    String word = matcher.group();
                    if(!STOP_WORDS.contains(word)){
                        index.computeIfAbsent(word, k -> new TreeSet<>()).add(lineNumber);
                    }
                }
                lineNumber++;
            }
        }

        // Sort the index by word
        TreeMap<String, Set<Integer>> sortedIndex = new TreeMap<>(index);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
            for (Map.Entry<String, Set<Integer>> entry : sortedIndex.entrySet()) {
                String word = entry.getKey();
                String lines = String.join(", ", entry.getValue().stream()
                        .map(String::valueOf)
                        .toArray(String[]::new));
                writer.write(word + ": " + lines);
                writer.newLine();
            }
        }

        System.out.println("Inverted index written to '" + outputFilePath + "'");
    }

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\micha\\Documents\\Java Data Structures\\searching-for-the-answer\\app\\src\\main\\java\\org\\example\\input.txt";
        String outputFile = "C:\\Users\\micha\\Documents\\Java Data Structures\\searching-for-the-answer\\app\\src\\main\\java\\org\\example\\inverted_index.txt";

        try {
            buildInvertedIndex(inputFile, outputFile);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}