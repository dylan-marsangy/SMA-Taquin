package puzzle.agent.state;

import puzzle.agent.AgentPiece;
import puzzle.agent.state.name.AutomateState;
import puzzle.agent.state.name.StateName;

@AutomateState(name = StateName.SLEEPING)
public class SleepingState extends State {

    public SleepingState(AgentPiece automate) {
        super(automate);
    }

    @Override
    public void execute() {
        if (automate.isAtFinalLocation()) {
//            System.out.println(String.format("%s...%s",
//                    automate, automate.getCurrentLocation()));
        } else {
            automate.setState(new MovingState(automate));
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
