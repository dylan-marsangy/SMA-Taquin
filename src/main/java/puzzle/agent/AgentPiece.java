package puzzle.agent;

import mailbox.Postman;
import mailbox.message.Message;
import puzzle.action.ActionObservable;
import puzzle.action.ActionObserver;
import puzzle.Piece;
import puzzle.Puzzle;
import puzzle.PuzzleLocation;
import puzzle.agent.state.*;
import view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AgentPiece extends Piece implements Runnable, ActionObserver, MessageObserver, MessageObservable {

    private State state;

    public AgentPiece(View view, Puzzle puzzle, PuzzleLocation startLocation, PuzzleLocation finalLocation) {
        super(view, puzzle, startLocation, finalLocation);

        this.state = new StartingState(this);

        final AgentPiece piece = this;
        addStateListener((oldState, newState) ->
                System.out.println(String.format("-- %s:%s->%s",
                        piece,
                        oldState,
                        newState)));
    }

    @Override
    public synchronized void run() {
        if (state != null) {
            execute();
        }
    }

    public void execute() {
        state.execute();
    }

    @Override
    public void sendMessage(Message message) {
        Postman.mail(message);
        notifyMessage((AgentPiece) message.getRecipient());
    }

    @Override
    public void updateActionObserver(ActionObservable observable) {
        if (this.state instanceof HoldingState) {
            HoldingState s = (HoldingState) this.state;
            if (observable.equals(s.getHolding())) {
                setState(new MovingState(this));
            }
        }
    }

    @Override
    public void notifyMessage(AgentPiece... observers) {
        Arrays.stream(observers)
                .forEach(AgentPiece::updateMessage);
    }

    @Override
    public void updateMessage() {
        System.out.println(String.format("|| %s", this));
        setState(new ReadingState(this));
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        State old = this.state;
        this.state = state;
        fireStateListener(old, state);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // STATE LISTENER --------------------------------------------------------------------------------------------------

    private final Collection<StateListener> stateListeners = new ArrayList<>();

    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        stateListeners.remove(listener);
    }

    public Collection<StateListener> getStateListeners() {
        return stateListeners;
    }

    private void fireStateListener(State oldState, State newState) {
        for (StateListener listener : stateListeners) {
            listener.onStateChanged(oldState, newState);
        }
    }

}
