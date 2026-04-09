package entity.action.interfaces;

import java.util.List;

import entity.action.ActionContext;
import entity.combatant.Combatant;

public interface SingleTargetAttack extends BasicAttack {
    Combatant selectTarget(ActionContext ctx);
    default List<Combatant> selectTargets(ActionContext ctx) { return List.of(selectTarget(ctx)); }

}
