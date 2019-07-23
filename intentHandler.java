//Shubhankar Singh 2016
package scoremanager;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import java.util.Map;

public class intentHandler {
    
    private static int numberOfPlayers;
    private static int[] scores = new int[4];
    
    private static final String PLAYERS_KEY = "PLAYERS";
    private static final String PLAYERS_SLOT = "Players";
    
    private static final String SCORES_KEY = "SCORES";
    private static final String SCORES_SLOT = "Scores";
    
    private static final String PLAYER_TYPES_KEY = "PLAYER_TYPES";
    private static final String PLAYER_TYPES_SLOT = "playerType";
    
    public static SpeechletResponse getWelcomeResponse() {
        // Create the welcome message
        String speechText = "Welcome to score manager. To begin, start by saying how many players will be playing between two and four.";
        String repromptText = "Please tell me how many players will play between two and four.";

        return getSpeechletResponse(speechText, repromptText, true);
    }
    
    public static SpeechletResponse endSession() {
        // Create the welcome message
        String speechText = "Thank you for using score manager. Goodbye.";
        
        //resets the scores
        //so that when it is time for another launch response the "session is fresh"
        for(int i = 0; i < 4; i++){
        scores[i] = 0;
        }

        return getSpeechletResponse(speechText, "", false);
    }
    
    public static SpeechletResponse setNumOfPlayers(final Intent intent, final Session session) {
        boolean isAskResponse = false;
        
        // Get the slots from the intent (numbers of players)
        Map<String, Slot> slots = intent.getSlots();

        // get the specific slot (number of players)
        Slot slot = slots.get(PLAYERS_SLOT);
        String speechText, repromptText;

        // Check for favorite color and create output to user.
        if (slot != null) 
        {
            // Store the user's favorite color in the Session and create response.
            String players = slot.getValue();
            session.setAttribute(PLAYERS_KEY, players);
            
            if(players.contains("2"))
            {
                numberOfPlayers = 2;
            }
            else if(players.contains("3"))
            {
                numberOfPlayers = 3;
            }
            else if(players.contains("4"))
            {
                numberOfPlayers = 4;
            }
            else{numberOfPlayers = 4;}
            
            //%s just puts players value into the string each time
            speechText = String.format("I have updated the game for %s players.", players);
            //There was no need for prompting the user again, successfully added players
            isAskResponse = false;
            repromptText = "NONE";
        } 
        else 
        {
            // Render an error because slot is null
            speechText = "I'm not sure how many players there are, please try again";
            repromptText = "I'm not sure how many players there are, please try again by saying a number between two and four.";
        }
        return getSpeechletResponse(speechText, repromptText, isAskResponse);
    }
    
    //MAY MOVE TO OTHER CLASS
    public static SpeechletResponse tellPlayers(final Intent intent, final Session session) {
        
        String speechText = "there are " + numberOfPlayers + " in the current game";
        
        return getSpeechletResponse(speechText, "", false);
    }
    
    public static SpeechletResponse getScore(final Intent intent, final Session session){
               
        String speechText = "Here are the current scores: ";
        
        int COUNT = 0;
        
        switch (numberOfPlayers) {
            case 2:
                COUNT = 2;
                break;
            case 3: 
                COUNT = 3;
                break;
            case 4:
                COUNT = 4;
                break;
        }
        
        for(int i = 0; i < COUNT; i++)
        {
            speechText += " Player " + (i+1) + " has " + scores[i];
        }
        
        //test line:
        //String speechText = "hello, I am here to tell you the score, LADS";
        
        return getSpeechletResponse(speechText, "", false);
    }
    //MAY MOVE TO OTHER CLASS
        
    public static SpeechletResponse setScore(final Intent intent, final Session session) {
        boolean isAskResponse = false;
        
        // Get the slots from the intent (scores)
        Map<String, Slot> slots = intent.getSlots();

        // get the specific slot (the score)
        Slot scoreSlot = slots.get(SCORES_SLOT);
        Slot playerTypeSlot = slots.get(PLAYER_TYPES_SLOT);
        
        String speechText;
        
        String score = scoreSlot.getValue();
        session.setAttribute(SCORES_KEY, score);
        
        String playerType = playerTypeSlot.getValue();
        session.setAttribute(PLAYER_TYPES_KEY, playerType);
        
        // hold the score until it has been added to the correct value in the scores array
        int temp = 0;
        
        //stage one is saving the score heard temporarily
        if(score.equalsIgnoreCase("1 point")) {temp = 1;}
        else if(score.equalsIgnoreCase("2 points")) {temp = 2;}
        else if(score.equalsIgnoreCase("3 points")) {temp = 3;}
        else if(score.equalsIgnoreCase("4 points")) {temp = 4;}
        else if(score.equalsIgnoreCase("5 points")) {temp = 5;}
        else if(score.equalsIgnoreCase("6 points")) {temp = 6;}
        else if(score.equalsIgnoreCase("7 points")) {temp = 7;}
        else if(score.equalsIgnoreCase("8 points")) {temp = 8;}
        else if(score.equalsIgnoreCase("9 points")) {temp = 9;}
        else if(score.equalsIgnoreCase("10 points")) {temp = 10;}
        else {}
        
        //stage two is adding that score to the appropriate score index (in the scores array)
        if(playerType.equalsIgnoreCase("player 1")) {scores[0] += temp;}
        else if(playerType.equalsIgnoreCase("player 2")) {scores[1] += temp;}
        else if(playerType.equalsIgnoreCase("player 3")) {scores[2] += temp;}
        else if(playerType.equalsIgnoreCase("player 4")) {scores[3] += temp;}
        else {}
        
        speechText = "Updating score. " + playerType + " now has " + score + "added to their score.";

        return getSpeechletResponse(speechText, "", isAskResponse);
    }
    
    public static SpeechletResponse getSpeechletResponse(String speechText, String repromptText,
            boolean isAskResponse) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Score Manager");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        if (isAskResponse) {
            // Create reprompt
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText(repromptText);
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);

        } else {
            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
}
