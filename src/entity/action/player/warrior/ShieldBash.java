package entity.action.player.warrior;

import entity.action.ActionContext;
import entity.action.interfaces.PlayerSingleTargetAttack;
import entity.action.interfaces.SpecialAttack;
import entity.combatant.Combatant;
import entity.combatant.interfaces.Stunnable;

public class ShieldBash extends SpecialAttack implements PlayerSingleTargetAttack {
    public ShieldBash() { super(2); }

    @Override
    public void executeOn(Combatant target, ActionContext ctx) {
        int dmg = getDamage(target, ctx);
        target.takeDamage(dmg);
        displayDamage(target, ctx, dmg, "Shield Bashes");
        if (target instanceof Stunnable && target.isAlive()) {
            ((Stunnable) target).applyStun(2, ctx.ui);
        } 
    }

    @Override public String getLabel() { return "Shield Bash"; }
}
