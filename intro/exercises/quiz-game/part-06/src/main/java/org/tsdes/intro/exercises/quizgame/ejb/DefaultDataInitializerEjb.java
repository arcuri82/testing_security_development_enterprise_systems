package org.tsdes.intro.exercises.quizgame.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.function.Supplier;

@Singleton
@Startup
public class DefaultDataInitializerEjb {

    @EJB
    private CategoryEjb categoryEjb;

    @EJB
    private QuizEjb quizEjb;

    @PostConstruct
    public void initialize(){

        Long ctgSE = attempt(() -> categoryEjb.createCategory("Software Engineering"));
        Long ctgH = attempt(() -> categoryEjb.createCategory("History"));

        Long sEP = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Enterprise Programming"));
        Long sIS = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Information Security"));
        Long sJ = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Java"));
        Long sA = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Algorithms and Data Structure"));

        Long sRE = attempt(() -> categoryEjb.createSubCategory(ctgH, "Roman Empire"));

        createEnterpriseQuestions(sEP);
        createJavaQuestions(sJ);
        createSecurityQuestions(sIS);
        createAlgorithmQuestions(sA);
        createRomeEmpireQuestions(sRE);
    }

    private void createEnterpriseQuestions(Long sub) {

        createQuiz(
                sub,
                "What does JPA stands for?",
                "Java Pale Ale",
                "Java Persistence API",
                "Java Process Analyzer",
                "Java Persistence Analyzer",
                1);

        createQuiz(
                sub,
                "What does JEE stands for?",
                "Java Embedded Edition",
                "Java Extended Edition",
                "Java Enterprise Edition",
                "Java Excelsior Edition",
                2);
        createQuiz(
                sub,
                "Which of these is a JPA implementation?",
                "Hibernate",
                "Wildfly",
                "Glassfish",
                "Jackson",
                0);
    }

    private void createJavaQuestions(Long sub){

        createQuiz(
                sub,
                "What is a 'volatile' variable?",
                "Java does not have volatile variables",
                "A variable whose value cannot be changed",
                "A variable whose value might change every time it is read",
                "A variable whose value is never cached",
                3);
        createQuiz(
                sub,
                "What is a 'final' variable?",
                "A variable that can be used only when the JVM is shutting down",
                "A variable whose value cannot be changed once initialized",
                "A variable whose value might change every time it is read",
                "Java does not have final variables",
                1);
    }

    private void createSecurityQuestions(Long sub) {
        createQuiz(
                sub,
                "Why should hashed passwords be 'salted'?",
                "You cannot 'salt' a password",
                "It makes the passwords easier to remember",
                "Help to defend from dictionary attacks",
                "They taste better",
                2);

        createQuiz(
                sub,
                "Which grade will you get if you submit a project that is vulnerable to SQL Injection attacks?",
                "One grade less (e.g., a B turns into a C)",
                "Two grades less (e.g., a B turns into a D)",
                "An A, because so your lecturer can have fun hacking your web site",
                "A straight F, regardless of what done in the rest of the project",
                3);

    }

    private void createAlgorithmQuestions(Long sub) {
        createQuiz(
                sub,
                "What best describe the runtime complexity of binary search?",
                "5n",
                "O(n)",
                "O(log n)",
                "O(n log n)",
                2);

        createQuiz(
                sub,
                "What can you say about sorting algorithms?",
                "Merge Sort is strictly better than Quick Sort",
                "Quick Sort is strictly better than Merge Sort",
                "Bubble Sort is better than Merge/Quick Sort, as it uses less space",
                "Merge Sort is asymptotically optimal in the number of comparisons",
                3);

    }

    private void createRomeEmpireQuestions(Long sub) {
        createQuiz(
                sub,
                "Who was the first Roman Emperor?",
                "Caligula",
                "Tiberius",
                "Augustus",
                "Caesar",
                2);

        createQuiz(
                sub,
                "After which god is the month 'Mars' named?",
                "God of Thunders",
                "God of Love",
                "God of Wars",
                "God of Pestilence",
                2);

        createQuiz(
                sub,
                "According to the legend, who founded Rome ?",
                "Romulus and Remus",
                "Augustus and Caesar",
                "Tiberius and Claudius",
                "Erik and Olav",
                0);

        createQuiz(
                sub,
                "Which was the largest empire in history?",
                "Mongol Empire",
                "Russian Empire",
                "English Empire",
                "Roman Empire",
                2);

        createQuiz(
                sub,
                "Who were the Praetorians?",
                "Priests of the God Pratunus",
                "Slaves",
                "Barbarians",
                "Elite soldiers",
                3);
    }



    private void createQuiz(
            long subCategoryId,
            String question,
            String firstAnswer,
            String secondAnswer,
            String thirdAnswer,
            String fourthAnswer,
            int indexOfCorrectAnswer){
        attempt(() -> quizEjb.createQuiz(
                subCategoryId,
                question,
                firstAnswer,
                secondAnswer,
                thirdAnswer,
                fourthAnswer,
                indexOfCorrectAnswer));
    }

    private  <T> T attempt(Supplier<T> lambda){
        try{
            return lambda.get();
        }catch (Exception e){
            return null;
        }
    }
}
