package puzzle.agent.state;

import puzzle.agent.AgentPiece;
import puzzle.agent.state.name.AutomateState;
import puzzle.agent.state.name.StateName;

@AutomateState(name = StateName.STARTING)
public class StartingState extends State {

    public StartingState(AgentPiece automate) {
        super(automate);
    }

    @Override
    public void execute() {
        System.out.println(String.format("Starting thread %s", automate));
        automate.setState(new MovingState(automate));
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
