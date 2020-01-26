package puzzle.agent;

import mailbox.Mailable;

public interface MessageObservable extends Mailable {

    void notifyMessage(AgentPiece... observers);
}
