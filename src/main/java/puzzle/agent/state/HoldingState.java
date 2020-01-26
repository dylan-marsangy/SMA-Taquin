package puzzle.agent.state;

import puzzle.action.ActionObserver;
import puzzle.agent.AgentPiece;

public abstract class HoldingState extends State {

    protected AgentPiece holding;

    public HoldingState(AgentPiece automate, AgentPiece holding) {
        super(automate);

        this.holding = holding;
    }

    public AgentPiece getHolding() {
        return holding;
    }

    public void setHolding(AgentPiece holding) {
        this.holding = holding;
    }
}
