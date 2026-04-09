package entity.item;

import entity.action.ActionContext;
import entity.combatant.interfaces.SmokeBombable;

public class SmokeBomb extends Item {
    public SmokeBomb() { this.name = "Smoke Bomb"; }

    @Override
    public void use(ActionContext ctx) {
        if (ctx.actor instanceof SmokeBombable && !used) {
            ((SmokeBombable) ctx.actor).applySmokeBomb(2);
            used = true;
            ctx.ui.displayActionResult(ctx.actor.getName() + " throws a Smoke Bomb! Enemy attacks deal 0 damage for 2 turns.");
        }
    }
}