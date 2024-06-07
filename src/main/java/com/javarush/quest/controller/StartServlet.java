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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String playerName = request.getParameter("playerName");
        HttpSession session = request.getSession();
        session.setAttribute("playerName", playerName);
        session.setAttribute("questionIndex", 0); // Corrected attribute name
        session.setAttribute("attempts", 0);
        session.setAttribute("wins", 0);
        session.setAttribute("losses", 0);
        session.setAttribute("gamesPlayed", 0); // Make sure to set this attribute
        session.setAttribute("ipAddress", request.getRemoteAddr()); // Set IP address
        response.sendRedirect("/game");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
