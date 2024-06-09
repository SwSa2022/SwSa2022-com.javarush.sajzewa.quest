package com.javarush.quest.controller;

import com.javarush.quest.entity.QuestionAnswer;
import com.javarush.quest.util.QuestionLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestServletTest {

    @InjectMocks
    private QuestServlet questServlet;

    @Mock
    private QuestionLoader questionLoader;

    @Mock
    private ServletConfig config;

    @Mock
    private ServletContext context;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    private List<QuestionAnswer> questions;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(config.getServletContext()).thenReturn(context);
        when(context.getInitParameter("questionsFileName")).thenReturn("questions_answer.yml");

        questions = new ArrayList<>();
        questions.add(new QuestionAnswer("Question 1", Arrays.asList("Answer 1", "Answer 2"), "Answer 1"));
        questions.add(new QuestionAnswer("Question 2", Arrays.asList("Answer 3", "Answer 4"), "Answer 3"));

        // Ensure the mock questionLoader returns the controlled list of questions
        when(questionLoader.getQuestions()).thenReturn(questions);
// Use the setter method to inject the mock questionLoader
        questServlet.setQuestionLoader(questionLoader);

        // Call init method
        questServlet.init(config);    }

    @Test
    void testInitSuccess() throws Exception {
        assertEquals(2, questServlet.questions.size());
        verify(context).setAttribute("questions", questions);
    }

    @Test
    void testInitFailure() throws Exception {
        doThrow(new RuntimeException("Failed to load questions")).when(questionLoader).getQuestions();
        ServletException exception = assertThrows(ServletException.class, () -> {
            questServlet.init(config);
        });        assertEquals("Error initializing QuestServlet", exception.getMessage());
    }

    @Test
    void testDoGet_NoSession() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        questServlet.doGet(request, response);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void testDoGet_FirstQuestion() throws Exception {
        // Mocking session
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null); // Using anyString() matcher

        // Mocking dispatcher
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher); // Using anyString() matcher

        // Prepare the expected question and answers
        String expectedQuestion = "Question 1";
        List<String> expectedAnswers = Arrays.asList("Answer 1", "Answer 2");

        // Creating a mock Questions list
        List<QuestionAnswer> questions = new ArrayList<>();
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestion("Question 1");
        questionAnswer.setAnswers(Arrays.asList("Answer 1", "Answer 2"));
        questions.add(questionAnswer);

        // Injecting the mock questions list into the servlet
        QuestServlet questServlet = new QuestServlet();
        questServlet.setQuestions(questions);

        // Call the servlet doGet method
        questServlet.doGet(request, response);

        // Verify interactions
        verify(session).setAttribute(eq("questionIndex"), eq(0));

        // Verify that the correct question and answers are set as request attributes
        ArgumentCaptor<String> questionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> answersCaptor = ArgumentCaptor.forClass(List.class);
        verify(request).setAttribute(eq("question"), questionCaptor.capture());
        verify(request).setAttribute(eq("answers"), answersCaptor.capture());

        // Assert that captured values match expected ones
        assertEquals(expectedQuestion, questionCaptor.getValue()); // Ensure question is set
        assertEquals(expectedAnswers, answersCaptor.getValue()); // Ensure answers are set

        // Verify forward dispatch
        verify(dispatcher).forward(request, response);
    }
    @Test
    void testDoGet_LastQuestion() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("questionIndex")).thenReturn(questions.size());
        questServlet.doGet(request, response);
        verify(response).sendRedirect("success.jsp");
    }

    @Test
    void testDoPost_CorrectAnswer() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("questionIndex")).thenReturn(0);

        // Mocking current question and correct answer
        List<QuestionAnswer> questions = new ArrayList<>();
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestion("Question 1");
        questionAnswer.setCorrect("Answer 1");
        questions.add(questionAnswer);

        // Injecting the mock questions list into the servlet
        QuestServlet questServlet = new QuestServlet();
        questServlet.setQuestions(questions);

        // Setting wins attribute in session
        when(session.getAttribute("wins")).thenReturn(0);
        when(session.getAttribute("gamesPlayed")).thenReturn(0);

        // Call the servlet doPost method with correct answer
        when(request.getParameter("answer")).thenReturn("Answer 1");
        questServlet.doPost(request, response);

        // Verify interactions
        verify(session).setAttribute("questionIndex", 1); // Question index should be incremented
        verify(request).setCharacterEncoding("UTF-8"); // Verify setCharacterEncoding is called once
        verify(response).setCharacterEncoding("UTF-8"); // Verify setCharacterEncoding is called once
        verify(request, never()).getRequestDispatcher("question.jsp"); // Should not forward to question.jsp
    }

    @Test
    void testDoPost_IncorrectAnswer() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("questionIndex")).thenReturn(0);

        List<QuestionAnswer> questions = new ArrayList<>();
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestion("Question 1");
        questionAnswer.setCorrect("Answer 1");
        questions.add(questionAnswer);

        QuestServlet questServlet = new QuestServlet();
        questServlet.setQuestions(questions);

        when(request.getParameter("answer")).thenReturn("Wrong Answer");
        when(session.getAttribute("attempts")).thenReturn(0);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("question.jsp")).thenReturn(dispatcher);

        questServlet.doPost(request, response);

        verify(session).setAttribute("attempts", 1);
        verify(request).setAttribute("retry", true);
        verify(session, never()).setAttribute("questionIndex", 1); // Question index should not be incremented
        verify(request, times(2)).setCharacterEncoding("UTF-8"); // Verify setCharacterEncoding is called twice
        verify(response, times(2)).setCharacterEncoding("UTF-8"); // Verify setCharacterEncoding is called twice
        verify(request, never()).getRequestDispatcher("failure.jsp"); // Should not forward to question.jsp
    }

    @Test
    void testDoPost_MaxAttempts() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("questionIndex")).thenReturn(0);
        when(request.getParameter("answer")).thenReturn("Wrong Answer");
        when(session.getAttribute("attempts")).thenReturn(2);
        when(session.getAttribute("losses")).thenReturn(0);
        when(session.getAttribute("gamesPlayed")).thenReturn(0);

        questServlet.doPost(request, response);

        verify(session).setAttribute("losses", 1);
        verify(session).setAttribute("gamesPlayed", 1);
        verify(response).sendRedirect("failure.jsp");
    }

    @Test
    void testDoPost_CorrectAnswerLastQuestion() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("questionIndex")).thenReturn(0);

        List<QuestionAnswer> questions = new ArrayList<>();
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestion("Question 2");
        questionAnswer.setCorrect("Answer 3");
        questions.add(questionAnswer);

        QuestServlet questServlet = new QuestServlet();
        questServlet.setQuestions(questions);

        when(session.getAttribute("questionIndex")).thenReturn(questions.size() - 1);
        when(request.getParameter("answer")).thenReturn("Answer 3");
        when(session.getAttribute("wins")).thenReturn(0);
        when(session.getAttribute("gamesPlayed")).thenReturn(0);

        questServlet.doPost(request, response);

        verify(session).setAttribute("wins", 1);
        verify(session).setAttribute("gamesPlayed", 1);
        verify(request, never()).getRequestDispatcher("success.jsp");
    }

    @Test
    void testDoPost_SessionCookieSetting() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("questionIndex")).thenReturn(0);
        when(request.getParameter("answer")).thenReturn("Answer 1");
        when(session.getId()).thenReturn("testSessionId");

        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0");

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("question.jsp")).thenReturn(dispatcher);

        questServlet.doPost(request, response);

        verify(response).setHeader("Set-Cookie", "JSESSIONID=testSessionId; SameSite=None; Secure");
    }
}
