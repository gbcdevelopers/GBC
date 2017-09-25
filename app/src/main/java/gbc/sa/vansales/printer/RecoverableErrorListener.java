package gbc.sa.vansales.printer;
import java.util.EventListener;
/**
 * Created by Rakshit on 06-Feb-17.
 */
public interface RecoverableErrorListener extends EventListener {
    void receivedRecoverableError(RecoverableErrorEvent recoverableErrorEvent);
}

