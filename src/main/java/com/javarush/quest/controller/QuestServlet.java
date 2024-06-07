package com.javarush.quest.controller;

import com.javarush.quest.entity.QuestionAnswer;
import com.javarush.quest.util.QuestionLoader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/game")
public class QuestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(QuestServlet.class.getName());
    protected List<QuestionAnswer> questions;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            String fileName = getServletContext().getInitParameter("questionsFileName");
            if (fileName == null || fileName.isEmpty()) {
                fileName = "questions_answer.yml";
            }
            QuestionLoader questionLoader = new QuestionLoader(fileName);
            questions = questionLoader.getQuestions();
            LOGGER.log(Level.INFO, "QuestServlet initialized successfully with " + (questions != null ? questions.size() : 0) + " questions.");
            getServletContext().setAttribute("questions", questions);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing QuestServlet", e);
            throw new ServletException("Error initializing QuestServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer questionIndex = (Integer) session.getAttribute("questionIndex");
        if (questionIndex == null) {
            questionIndex = 0;
            session.setAttribute("questionIndex", questionIndex);
        }
        if (questionIndex >= questions.size()) {
            response.sendRedirect("success.jsp");
            return;
        }
        QuestionAnswer currentQuestion = questions.get(questionIndex);
        request.setAttribute("question", currentQuestion.getQuestion());
        request.setAttribute("answers", currentQuestion.getAnswers());
//        request.getRequestDispatcher("question.jsp").forward(request, response);
        RequestDispatcher dispatcher = request.getRequestDispatcher("question.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        String playerName = request.getParameter("playerName");
        if (playerName == null || playerName.isEmpty()) {
            playerName = "test"; // Default name
        }
        session.setAttribute("playerName", playerName);

        Integer gamesPlayed = (Integer) session.getAttribute("gamesPlayed");
        if (gamesPlayed == null) {
            gamesPlayed = 0; // Default to 0
        }
        gamesPlayed++;
        session.setAttribute("gamesPlayed", gamesPlayed);

        String ipAddress = request.getRemoteAddr();
        session.setAttribute("ipAddress", ipAddress);

        Integer questionIndex = (Integer) session.getAttribute("questionIndex");
        if (questionIndex == null) {
            questionIndex = 0;
        }
        if (questionIndex >= questions.size()) {
            response.sendRedirect("success.jsp");
            return;
        }

        String answer = request.getParameter("answer");
        QuestionAnswer currentQuestion = questions.get(questionIndex);
        String correctAnswer = currentQuestion != null ? currentQuestion.getCorrect() : null;

        request.setAttribute("correctAnswer", correctAnswer);
        request.setAttribute("selectedAnswer", answer);
        request.setAttribute("session", session);
        request.setAttribute("questions", questions);

        if (correctAnswer != null && correctAnswer.trim().equalsIgnoreCase(answer != null ? answer.trim() : "")) {
            Integer wins = (Integer) session.getAttribute("wins");
            if (wins == null) {
                wins = 0; // Default to 0
            }
            wins++;
            session.setAttribute("wins", wins);

            questionIndex++;
            session.setAttribute("questionIndex", questionIndex);
            if (questionIndex >= questions.size()) {
                request.getRequestDispatcher("success.jsp").forward(request, response);
                return;
            }
        } else {
            Integer losses = (Integer) session.getAttribute("losses");
            if (losses == null) {
                losses = 0; // Default to 0
            }
            losses++;
            session.setAttribute("losses", losses);

            Integer attempts = (Integer) session.getAttribute("attempts");
            if (attempts == null) {
                attempts = 1;
            } else {
                attempts++;
            }
            if (attempts >= 2) {
                request.getRequestDispatcher("failure.jsp").forward(request, response);
                return;
            }
            session.setAttribute("attempts", attempts);
            request.setAttribute("retry", true);
        }

        doGet(request, response);

        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.contains("Firefox")) {
            // For Firefox browser
            response.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; SameSite=None; Secure");
        } else {
            // For other browsers
            response.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; SameSite=None; Secure; HttpOnly");
        }

    }


//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        HttpSession session = request.getSession();
//
//        String playerName = (String) session.getAttribute("playerName");
//        if (playerName == null) {
//            playerName = "test"; // Default name, or get from request parameter
//            session.setAttribute("playerName", playerName);
//        }
//
//        Integer gamesPlayed = (Integer) session.getAttribute("gamesPlayed");
//        if (gamesPlayed == null) {
//            gamesPlayed = 0; // Default to 0
//        }
//        gamesPlayed++;
//        session.setAttribute("gamesPlayed", gamesPlayed);
//
//        String ipAddress = request.getRemoteAddr();
//        session.setAttribute("ipAddress", ipAddress);
//
//        Integer questionIndex = (Integer) session.getAttribute("questionIndex");
//        if (questionIndex == null) {
//            questionIndex = 0;
//        }
//        String answer = request.getParameter("answer");
//        QuestionAnswer currentQuestion = questions.get(questionIndex);
//        String correctAnswer = currentQuestion.getCorrect();
//
//        request.setAttribute("correctAnswer", correctAnswer);
//        request.setAttribute("selectedAnswer", answer);
//
//        LOGGER.log(Level.INFO, "Comparing answers: User answer = [{0}], Correct answer = [{1}]", new Object[]{answer, correctAnswer});
//
//        if (correctAnswer == null) {
//            LOGGER.log(Level.SEVERE, "Correct answer is null for question index: {0}", questionIndex);
//            throw new ServletException("Correct answer is null for question index: " + questionIndex);
//        }
//        if (correctAnswer.trim().equalsIgnoreCase(answer.trim())) {
//            Integer wins = (Integer) session.getAttribute("wins");
//            if (wins == null) {
//                wins = 0; // Default to 0
//            }
//            wins++;
//            session.setAttribute("wins", wins);
//
//            questionIndex++;
//            session.setAttribute("questionIndex", questionIndex);
//            if (questionIndex >= questions.size()) {
//                request.getRequestDispatcher("success.jsp").forward(request, response);
//                return;
//            }
//        } else {
//            Integer losses = (Integer) session.getAttribute("losses");
//            if (losses == null) {
//                losses = 0; // Default to 0
//            }
//            losses++;
//            session.setAttribute("losses", losses);
//
//            Integer attempts = (Integer) session.getAttribute("attempts");
//            if (attempts == null) {
//                attempts = 1;
//            } else {
//                attempts++;
//            }
//            if (attempts >= 2) {
//                request.getRequestDispatcher("failure.jsp").forward(request, response);
//                return;
//            }
//            session.setAttribute("attempts", attempts);
//            request.setAttribute("retry", true);
//        }
//        doGet(request, response);
//    }
}
