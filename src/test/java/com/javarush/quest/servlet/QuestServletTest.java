package com.javarush.quest.servlet;

import com.javarush.quest.entity.QuestionAnswer;
import com.javarush.quest.controller.QuestServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class QuestServletTest {
    private QuestServlet questServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() throws ServletException {
        questServlet = new QuestServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Mock the QuestionLoader to return a list of questions
        List<QuestionAnswer> mockQuestions = Arrays.asList(
//                new QuestionAnswer("????? ???? ???????? ????? ??????? ?????? ????????", "???????"),
//        new QuestionAnswer("????? ???? ???????? ????? ??????? ? ???? ? ????????? ?? ???????", "???")

        );

//        questServlet.questions = mockQuestions; // Simulate successful loading
    }

    @Test
    public void testCorrectAnswer() throws IOException, ServletException {
        when(session.getAttribute("questionIndex")).thenReturn(0);
        when(request.getParameter("answer")).thenReturn("Everest");
//        questServlet.doPost(request, response);
        verify(session).setAttribute("questionIndex", 1);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testWrongAnswer() throws IOException, ServletException {
        when(session.getAttribute("questionIndex")).thenReturn(0);
        when(request.getParameter("answer")).thenReturn("Mont Blanc");
        when(session.getAttribute("attempts")).thenReturn(1);
//        questServlet.doPost(request, response);
        verify(dispatcher).forward(request, response);
        verify(request).setAttribute("retry", true);
    }

    @Test
    public void testGameOverAfterTwoWrongAnswers() throws IOException, ServletException {
        when(session.getAttribute("questionIndex")).thenReturn(0);
        when(request.getParameter("answer")).thenReturn("Mont Blanc");
        when(session.getAttribute("attempts")).thenReturn(2);
//        questServlet.doPost(request, response);
        verify(dispatcher).forward(request, response);
    }
}
