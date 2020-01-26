package puzzle.agent.state;

import puzzle.agent.AgentPiece;
import puzzle.agent.state.name.AutomateState;
import puzzle.agent.state.name.StateName;

@AutomateState(name = StateName.WAITING)
public class WaitingState extends HoldingState {

    private Long counter = 0L;

    public WaitingState(AgentPiece automate, AgentPiece holding) {
        super(automate, holding);
    }

    @Override
    public void execute() {
        counter++;

        if (counter > 20) {
            automate.setState(new MovingState(automate));
        }

        System.out.println(String.format("%s...%s",
                automate, automate.getCurrentLocation()));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
