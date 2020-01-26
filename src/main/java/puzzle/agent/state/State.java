package puzzle.agent.state;

import puzzle.agent.AgentPiece;
import puzzle.agent.state.name.AutomateState;

public abstract class State {

    protected AgentPiece automate;

    public State(AgentPiece automate) {
        this.automate = automate;
    }

    public abstract void execute();

    public void fireOnStateChanged() {}

    @Override
    public String toString() {
        return this.getClass().getAnnotation(AutomateState.class).name().toString();
    }
}
