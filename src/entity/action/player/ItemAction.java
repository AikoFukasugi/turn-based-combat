package entity.action.player;

import entity.action.ActionContext;
import entity.action.interfaces.SelfAction;
import entity.combatant.Combatant;
import entity.combatant.Player;

public class ItemAction implements SelfAction {

    @Override
    public boolean isReady(ActionContext ctx) {
        return ctx.actor instanceof Player &&
                ((Player) ctx.actor).hasUsableItem();
    }

    @Override
    public void executeOn(Combatant target, ActionContext ctx) {
        if (ctx.selectedItem == null) {
            ctx.ui.displayActionResult("No item selected!");
            return;
        }
        ctx.selectedItem.use(ctx);
    }

    @Override public String getLabel() { return "Use Item"; }
}