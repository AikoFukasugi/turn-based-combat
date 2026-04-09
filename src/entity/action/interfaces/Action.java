package entity.action.interfaces;

import java.util.List;

import entity.action.ActionContext;
import entity.combatant.Combatant;

/**
 * All actions implement this interface.
 * execute() receives the full ActionContext — no extra parameters needed.
 */
public interface Action {
    String getLabel();
    List<Combatant> selectTargets(ActionContext ctx);
    void executeOn(Combatant target, ActionContext ctx);
    default boolean isReady(ActionContext ctx) { return true; }


    default boolean execute(ActionContext ctx) {
        if (!isReady(ctx)) return false;
        List<Combatant> targets = selectTargets(ctx);
        for (Combatant t : targets) { executeOn(t, ctx); }
        return true;
    }
}