package readability;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        int charactersNumber = 0;
        int wordsNumber = 0;
        int sentencesNumber = 0;
        int syllablesNumber = 0;
        int polysyllablesNumber = 0;
        double score;
        String outputFormat;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(args[0]))) {
            //try (BufferedReader fileReader = new BufferedReader(new FileReader("C:\\Users\\Magda\\Java\\projekty cwiczeniowe\\Readability Score\\Readability Score\\task\\src\\readability\\in.txt"))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                charactersNumber += line.length() - line.replaceAll("[\\.\\?\\!]", "").length();

                String[] sentences = line.split("[\\.\\?\\!]");
                sentencesNumber += sentences.length;
                for (String sentence : sentences) {
                    String[] words = sentence.split("\\s");
                    wordsNumber += words.length;
                    for (String word : words) {
                        if (word.length() == 0) {
                            wordsNumber--;
                        }
                        charactersNumber += word.length();
                        syllablesNumber += syllables(word);
                        polysyllablesNumber += polysyllables(word);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println("Words: " + wordsNumber);
        System.out.println("Sentences: " + sentencesNumber);
        System.out.println("Characters: " + charactersNumber);
        System.out.println("Syllables: " + syllablesNumber);
        System.out.println("Polysyllables: " + polysyllablesNumber);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();
        scanner.close();

        outputFormat = "%s: %.2f (about %d year olds).";

        switch (option) {
            case "ARI":
                score = automatedReadabilityIndex(charactersNumber, wordsNumber, sentencesNumber);
                System.out.println(String.format(outputFormat, "Automated Readability Index", score, getAge(score)));
                break;

            case "FK":
                score = fleschKincaid(wordsNumber, sentencesNumber, syllablesNumber);
                System.out.println(String.format(outputFormat, "Flesch–Kincaid readability tests", score, getAge(score)));
                break;

            case "SMOG":
                score = simpleMeasureOfGobbledygook(polysyllablesNumber, sentencesNumber);
                System.out.println(String.format(outputFormat, "Simple Measure of Gobbledygook", score, getAge(score)));
                break;

            case "CL":
                score = colemanLiau(charactersNumber, sentencesNumber, wordsNumber);
                System.out.println(String.format(outputFormat, "Coleman–Liau index", score, getAge(score)));
                break;

            case "all":
                double avgAge = 0;

                score = automatedReadabilityIndex(charactersNumber, wordsNumber, sentencesNumber);
                System.out.println(String.format(outputFormat, "Automated Readability Index", score, getAge(score)));
                avgAge += getAge(score);

                score = fleschKincaid(wordsNumber, sentencesNumber, syllablesNumber);
                System.out.println(String.format(outputFormat, "Flesch–Kincaid readability tests", score, getAge(score)));
                avgAge += getAge(score);

                score = simpleMeasureOfGobbledygook(polysyllablesNumber, sentencesNumber);
                System.out.println(String.format(outputFormat, "Simple Measure of Gobbledygook", score, getAge(score)));
                avgAge += getAge(score);

                score = colemanLiau(charactersNumber, sentencesNumber, wordsNumber);
                System.out.println(String.format(outputFormat, "Coleman–Liau index", score, getAge(score)));
                avgAge += getAge(score);

                avgAge /= 4;
                System.out.println(String.format("\nThis text should be understood in average by %.2f year olds.", avgAge));
                break;
        }

    }

    private static int syllables(String word) {
        int vowelsCount = 0;
        for (int i = 0; i < word.length() - 1; i++) {
            if (word.substring(i, i + 1).matches("[aeiouy]")) {
                vowelsCount++;
                i++;
            }
        }

        return vowelsCount == 0 ? 1 : vowelsCount;
    }

    private static int polysyllables(String word) {

        return syllables(word) > 2 ? 1 : 0;
    }

    private static double automatedReadabilityIndex(int characters, int words, int sentences) {
        return 4.71 * characters / words + 0.5 * words / sentences - 21.43;
    }

    private static double fleschKincaid(int words, int sentences, int syllables) {
        return 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
    }

    private static double simpleMeasureOfGobbledygook(int polysyllables, int sentences) {
        return 1.043 * Math.sqrt(polysyllables * 30.0 / sentences) + 3.1291;
    }

    private static double colemanLiau(int characters, int sentences, int words) {
        double l = (double) characters / words * 100;
        double s = (double) sentences / words * 100;

        return 0.0588 * l - 0.296 * s - 15.8;
    }


    private static int getAge(double score) {
        int scoreRound = (int) Math.ceil(score);
        if (scoreRound == 1) {
            return 6;
        } else if (scoreRound == 2) {
            return 7;
        } else if (scoreRound == 3) {
            return 9;
        } else if (scoreRound == 4) {
            return 10;
        } else if (scoreRound == 5) {
            return 11;
        } else if (scoreRound == 6) {
            return 12;
        } else if (scoreRound == 7) {
            return 13;
        } else if (scoreRound == 8) {
            return 14;
        } else if (scoreRound == 9) {
            return 15;
        } else if (scoreRound == 10) {
            return 16;
        } else if (scoreRound == 11) {
            return 17;
        } else if (scoreRound == 12) {
            return 18;
        } else if (scoreRound == 13) {
            return 24;
        } else {
            return 25;
        }
    }
}
