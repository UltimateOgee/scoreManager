//Shubhankar Singh 2016
package scoremanager;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class ScoreManagerSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds;

    static {
        
        supportedApplicationIds = new HashSet<String>();
        // supportedApplicationIds.add("amzn1.echo-sdk-ams.app.[unique-value-here]");
    }

    public ScoreManagerSpeechletRequestStreamHandler() {
        super(new ScoreManagerSpeechlet(), supportedApplicationIds);
    }
}
