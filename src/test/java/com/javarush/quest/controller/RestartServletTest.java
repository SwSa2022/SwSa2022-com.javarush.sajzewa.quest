package com.javarush.quest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestartServletTest {

    @InjectMocks
    private RestartServlet restartServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Test
    public void testDoGet_WithSession() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);

        restartServlet.doGet(request, response);

        verify(session).setAttribute("questionIndex", 0);
        verify(session).setAttribute("attempts", 0);
        verify(response).sendRedirect("index.jsp");
    }

    @Test
    public void testDoGet_WithoutSession() throws ServletException, IOException {

        when(request.getSession(false)).thenReturn(null);
        restartServlet.doGet(request, response);

        verify(response).sendRedirect("index.jsp");
        verifyNoMoreInteractions(session);
    }
}
