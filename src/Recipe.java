import java.util.Queue;

public class Recipe {
    private PN.Transitions[] transitions;
    private Queue<String> buffer;

    Recipe(Queue<String> buffer, PN.Transitions... transitions) {
        this.transitions = transitions;
        this.buffer = buffer;
    }

    public PN.Transitions[] getTransitions() {
        return this.transitions;
    }

    public Queue<String> getBuffer() {
        return this.buffer;
    }
}
