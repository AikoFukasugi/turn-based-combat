package entity.action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entity.action.interfaces.Action;
import entity.action.interfaces.SpecialAttack;

/**
 * Holds the authorised action list for one combatant.
 * Filters by readiness and drives selection + execution.
 */
public class ActionMenu {

    private final List<Action> actions = new ArrayList<>();

    public void add(Action action) {
        actions.add(action);
    }

    public SpecialAttack getSpecial() { 
        for (Action a : actions) {
            if (a instanceof SpecialAttack) return (SpecialAttack) a;
        }
        return null;
    }

    public List<Action> all() { return actions; }

    /** Returns only actions currently usable. */
    public List<Action> ready(ActionContext ctx) {
        return actions.stream()
                .filter(a -> a.isReady(ctx))
                .collect(Collectors.toList());
    }

    public void decrementCooldowns() {
        for (Action action : actions) {
            if (action instanceof SpecialAttack) ((SpecialAttack) action).decrementCooldown();
        }
    }

}