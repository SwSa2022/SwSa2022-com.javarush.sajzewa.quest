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

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("/index.jsp");
            return;
        }

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
        request.setAttribute("playerName", session.getAttribute("playerName"));

        RequestDispatcher dispatcher = request.getRequestDispatcher("question.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("/index.jsp");
            return;
        }

        Integer questionIndex = (Integer) session.getAttribute("questionIndex");
        if (questionIndex == null) {
            questionIndex = 0;
        }

        String answer = request.getParameter("answer");
        QuestionAnswer currentQuestion = questions.get(questionIndex);
        String correctAnswer = currentQuestion != null ? currentQuestion.getCorrect() : null;
        Integer gamesPlayed = (Integer) session.getAttribute("gamesPlayed");

        if (correctAnswer != null && correctAnswer.trim().equalsIgnoreCase(answer != null ? answer.trim() : "")) {
            questionIndex++;
            session.setAttribute("questionIndex", questionIndex);

            if (questionIndex >= questions.size()) {

                Integer wins = (Integer) session.getAttribute("wins");
                session.setAttribute("wins", wins + 1);

                session.setAttribute("gamesPlayed", gamesPlayed +1);
                response.sendRedirect("success.jsp");
                return;
            }
        } else {
            Integer attempts = (Integer) session.getAttribute("attempts");
            if (attempts == null) {
                attempts = 0;
            }
            attempts++;
            session.setAttribute("attempts", attempts);

            if (attempts >= 2) {
                Integer losses = (Integer) session.getAttribute("losses");
                session.setAttribute("losses", losses + 1);
                session.setAttribute("gamesPlayed", gamesPlayed +1);
                response.sendRedirect("failure.jsp");
                return;
            }

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
}
