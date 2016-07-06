package com.teamtreehouse.techdegree.overboard.model;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by zzhea on 07.07.2016.
 */
public class UserTest {

    private Board board;
    private User user_1;
    private User user_2;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        board = new Board("About life");
        user_1 = new User (board, "Rashid");
        user_2 = new User (board, "Vahtang");
        question = user_1.askQuestion("Whatz a fack?");
        user_2.upVote(question);
        answer = user_2.answerQuestion(question, "Thatz a fack!");
        user_1.upVote(answer);
    }

    @Test
    public void testAskQuestion() throws Exception {
        assertEquals(5, user_1.getReputation());
    }

    @Test
    public void testAnswerQuestion() throws Exception {
        assertEquals(10, user_2.getReputation());
    }

    @Test
    public void testAcceptAnswer() throws Exception {
        user_1.acceptAnswer(answer);
        assertEquals(25, user_2.getReputation());
    }

    @Test
    public void testAvoidVotingYourQuestion() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        user_1.upVote(question);
    }

    @Test
    public void testAvoidVotingYourAnswer() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        user_2.upVote(answer);
    }

    @Test
    public void testAvoidAcceptAnswerByAnswerer() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage("Only Rashid can accept this answer as it is their question");

        user_2.acceptAnswer(answer);
    }

    //Extra tests
    @Test
    public void testDownVoting() throws Exception {
        user_1.downVote(answer);
        assertEquals(9, user_2.getReputation());
    }

    @Test
    public void testAvoidDownVotingYourPost() throws Exception {
        user_1.downVote(question);
        assertEquals(5,user_1.getReputation()); // Well, downVoter added, but reputation was not changed, looks like we have to avoid downvoting our posts OR allow it, but change reputation also?
    }

    @Test
    public void testAvoidAnsweringMyQuestion() throws Exception {
        user_1.answerQuestion(question, "Whatz a fack! (that was an answer by the way"); // OK, we can answer our questions! Lets see..
    }

    @Test
    public void testAvoidAcceptingAnswersForMyQuestion() throws Exception {
        Answer answerSelf = user_1.answerQuestion(question, "Whatz a fack! (that was an answer by the way"); // OK, we can answer our questions! Lets see..
        user_1.acceptAnswer(answerSelf);

        assertEquals(20, user_1.getReputation());       // Oh, WE CAN answer for our questions and then accept those answers - free boost reputation
    }
}