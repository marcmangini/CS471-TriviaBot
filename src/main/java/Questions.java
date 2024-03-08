import java.io.*;
import java.util.*;

/**
 * Creates the structures and methods necessary to have questions for the discord bot
 */
public class Questions {
    private HashMap<String, String> questionBank;
    Random picker;
    int numQs;
    private String question;
    private String correctAnswer;
    private List<String> options;

    /**
     * Constructs a question bank to be populated with as many trivia questions as we need.
     * Made of a hashmap where the Question is the key and the Answer is the value.
     */
    public Questions() {
        questionBank = new HashMap<String, String>();
        picker = new Random();
        numQs = 0;
    }

    public static List<Questions> loadQuestionsFromFile(String category) throws IOException {
        List<Questions> questions = new ArrayList<>();
        String fileName = "src/main/" + category.toLowerCase().replace(" ", "-") + ".txt";
        System.out.println("Attempting to load questions from file: " + fileName); // Debug statement
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Read line: " + line); // Debug statement
                if (!line.startsWith("#Q")) {
                    continue; // Skip lines that don't start with #Q
                }
                Questions question = new Questions();
                question.setQuestion(line.substring(3)); // Remove #Q

                // Read options and answer
                List<String> options = new ArrayList<>();
                while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                    if (line.startsWith("^")) {
                        // This is the answer line, remove the caret (^) and set it as the correct answer
                        question.setCorrectAnswer(line.substring(1).trim());
                    } else {
                        // This is an option line, add it to the options list
                        options.add(line.substring(2).trim()); // Remove ^
                    }
                }

                // Set options and add question to the list
                question.setOptions(options);
                questions.add(question);
            }
        }
        return questions;
    }



    public static Questions getRandomQuestion(List<Questions> questionPool) {
        Random random = new Random();
        return questionPool.get(random.nextInt(questionPool.size()));
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * //TODO
     * Picks a question out of the question bank to ask.
     *
     * @return The question String.
     */
//    public String pickQuestion() {
//        int pickedNum = picker.nextInt(0, numQs);
//        Set<String> possibleQs = questionBank.keySet();
//        String[] questions = (String[]) possibleQs.toArray();
//        String pickedQuestion = questions[pickedNum];
//
//        if (pickedQuestion.equals(null)) {
//            return "fail";
//        }
//        return pickedQuestion;
//    }

    /**
     * //TODO
     * Finds the answer to a given trivia question
     *
     * @param question the trivia question that requires an answer
     * @return the answer to the question
     */
    public String getAnswer(String question) {
        String answer = questionBank.get(question);
        if (answer.equals(null)) {
            return question + " is not a question";
        }
        return answer;
    }

    /**
     * Automates the process of adding trivia questions to the question bank by
     * allowing us to parse csv's with questions in first column and answers in next.
     *
     * @param csvFilePath the filepath to parse questions from to add to the question bank.
     * @return questionBank.
     */
    protected HashMap<String, String> readCSV(String csvFilePath) {
        questionBank = new HashMap<>(); // clear questionBank

        // Create new BufferedReader for csv file of questions and answers
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 2) {
                    String question = columns[0].trim(); // note questions should be in 1st column
                    String answer = columns[1].trim(); // note answers should be in 2nd column
                    questionBank.put(question, answer);
                }
                else {
                    System.out.println("Invalid record: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questionBank;
    }

    /**
     * A function to add trivia questions to the bank.
     *
     * @param question The question to add to the question bank
     * @param answer The answer of the trivia question
     */
    private void addQuestion(String question, String answer) {
        String prevAnswer = questionBank.put(question, answer);
        if (prevAnswer != null && !prevAnswer.equals(answer)) {
            System.out.println("Warning: Duplicate question added with different answer.");
        } else {
            numQs += 1;
        }
    }
}