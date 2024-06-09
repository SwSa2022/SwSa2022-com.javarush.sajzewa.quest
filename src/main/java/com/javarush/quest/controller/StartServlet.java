package com.javarush.quest.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/start")
public class StartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String playerName = request.getParameter("playerName");

        if (playerName != null && !playerName.isEmpty()) {
            session.setAttribute("playerName", playerName);
        } else {
            session.setAttribute("playerName", "test");
        }

        session.setAttribute("wins", session.getAttribute("wins") != null ? session.getAttribute("wins") : 0);
        session.setAttribute("losses", session.getAttribute("losses") != null ? session.getAttribute("losses") : 0);
        session.setAttribute("gamesPlayed", session.getAttribute("gamesPlayed") != null ? session.getAttribute("gamesPlayed") : 0);
        session.setAttribute("ipAddress", request.getRemoteAddr());

        session.setAttribute("questionIndex", 0);
        session.setAttribute("attempts", 0);

        response.sendRedirect("/game");    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}

