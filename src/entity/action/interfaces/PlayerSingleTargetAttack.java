package entity.action.interfaces;


import entity.action.ActionContext;
import entity.combatant.Combatant;

public interface PlayerSingleTargetAttack extends SingleTargetAttack {    
    default Combatant selectTarget(ActionContext ctx) { 
        return ctx.ui.selectTarget(ctx.getLivingOpponents());
    }
}
