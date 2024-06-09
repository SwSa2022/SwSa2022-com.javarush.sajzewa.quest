package com.javarush.quest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StartServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Test
    public void testDoPost() throws Exception {
        StartServlet startServlet = new StartServlet();

        when(request.getParameter("playerName")).thenReturn("TestPlayer");
        when(request.getSession()).thenReturn(session);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        startServlet.doPost(request, response);

        verify(session).setAttribute("playerName", "TestPlayer");
        verify(session).setAttribute("questionIndex", 0);
        verify(session).setAttribute("attempts", 0);
        verify(session).setAttribute("wins", 0);
        verify(session).setAttribute("losses", 0);
        verify(session).setAttribute("gamesPlayed", 0);
        verify(session).setAttribute("ipAddress", "127.0.0.1");

        verify(response).sendRedirect("/game");
    }
@Test
    public void testDoPostWithData()throws Exception {
        StartServlet startServlet = new StartServlet();

        when(request.getParameter("playerName")).thenReturn("Swetlana");
        when(request.getSession()).thenReturn(session);
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");

        startServlet.doPost(request, response);
    verify(session).setAttribute("playerName", "Swetlana");
    verify(session).setAttribute("questionIndex", 0);
    verify(session).setAttribute("attempts", 0);
    verify(session).setAttribute("wins", 0);
    verify(session).setAttribute("losses", 0);
    verify(session).setAttribute("gamesPlayed", 0);
    verify(session).setAttribute("ipAddress", "192.168.1.100");

    verify(response).sendRedirect("/game");
    }
}
