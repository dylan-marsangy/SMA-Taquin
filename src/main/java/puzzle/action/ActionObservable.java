package puzzle.action;

public interface ActionObservable {

    void notifyActionObserver(ActionObserver... observers);

}
