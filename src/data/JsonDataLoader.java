package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import logic.Question;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Loads programming questions from JSON files and manages the question bank.
 * Supports generating randomized game sets with balanced subject distribution.
 */
public class JsonDataLoader {

    /** Holds all questions found in files, grouped by subject */
    private Map<String, List<Question>> questionBank = new HashMap<>();

    public void loadQuestionsFromDirectory(String rootDirectoryPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(rootDirectoryPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(this::parseFile);
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
        }
    }

    private void parseFile(Path path) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path.toFile())) {
            Type listType = new TypeToken<List<Question>>(){}.getType();
            List<Question> loadedQuestions = gson.fromJson(reader, listType);

            if (loadedQuestions != null) {
                for (Question q : loadedQuestions) {
                    questionBank.computeIfAbsent(q.getSubject(), k -> new ArrayList<>()).add(q);
                }
            }
        } catch (IOException e) {
            System.err.println("Error parsing file " + path + ": " + e.getMessage());
        }
    }

    public List<Question> generateGameSet() {
        List<Question> gameSet = new ArrayList<>();

        for (String subject : questionBank.keySet()) {
            List<Question> subjectQuestions = questionBank.get(subject);
            Collections.shuffle(subjectQuestions);
            int takeCount = Math.min(subjectQuestions.size(), 2);
            gameSet.addAll(subjectQuestions.subList(0, takeCount));
        }

        Collections.shuffle(gameSet);
        return gameSet;
    }
}
