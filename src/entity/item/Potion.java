package entity.item;

import entity.action.ActionContext;
import entity.combatant.interfaces.Healable;;

public class Potion extends Item {
    public Potion() { this.name = "Potion"; }

    @Override
    public void use(ActionContext ctx) {
        int before = ctx.actor.getHp();
        if (ctx.actor instanceof Healable && !used) {
            ((Healable) ctx.actor).heal(100);
            used = true;
            ctx.ui.displayActionResult(ctx.actor.getName() + " uses Potion! HP: " + before +
                    " → " + ctx.actor.getHp() + "/" + ctx.actor.getMaxHp());
        }
    }
}
