import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class Recipe {
    private LinkedHashMap<PN.Transitions, Runnable> transitionMap;

    Recipe() {
        transitionMap = new LinkedHashMap<>();
    }

    public void addMapping(PN.Transitions t, Runnable r) {
        transitionMap.put(t, r);
    }

    public Set<PN.Transitions> getTransitions() {
        return this.transitionMap.keySet();
    }

    public LinkedHashMap<PN.Transitions, Runnable> getTransitionMap() {
        return transitionMap;
    }

}
