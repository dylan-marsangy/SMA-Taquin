package puzzle.agent.state;

public interface StateListener {

    void onStateChanged(State oldState, State newState);
}
