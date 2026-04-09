package entity.action.interfaces;

import java.util.List;

import entity.action.ActionContext;
import entity.combatant.Combatant;

/**
 * Target is always the actor — no selection needed.
 */
public interface SelfAction extends Action {

    @Override
    default List<Combatant> selectTargets(ActionContext ctx) { return List.of(ctx.actor); }

}