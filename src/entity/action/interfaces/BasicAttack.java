package entity.action.interfaces;

import entity.action.ActionContext;
import entity.combatant.Combatant;

public interface BasicAttack extends Action {
    default int getDamage(Combatant target, ActionContext ctx) {
        return Math.max(0, ctx.actor.getAttack() - target.getEffectiveDefense());
    }

    default void displayDamage(Combatant target, ActionContext ctx, int dmg, String verb) {
        ctx.ui.displayActionResult(ctx.actor.getName() + " " + verb + " " +
                target.getName() + " for " + dmg + " dmg! HP: " +
                target.getHp() + "/" + target.getMaxHp());
        if (!target.isAlive())
            ctx.ui.displayActionResult(target.getName() + " is ELIMINATED!");
    }

    @Override
    default void executeOn(Combatant target, ActionContext ctx) {
        int dmg = getDamage(target, ctx);
        target.takeDamage(dmg);
        displayDamage(target, ctx, dmg, "attacks");
    }

    @Override 
    default String getLabel() { return "Basic Attack"; }
}