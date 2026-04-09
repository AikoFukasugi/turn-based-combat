package entity.action.player;
import entity.action.ActionContext;
import entity.action.interfaces.SelfAction;
import entity.combatant.Combatant;
import entity.effect.DefendEffect;

public class DefendAction implements SelfAction {

    @Override
    public void executeOn(Combatant target, ActionContext ctx) {
        target.getStatus().add(new DefendEffect());
        ctx.ui.displayActionResult(target.getName() +
                " defends! +10 DEF for 2 turns.");
    }

    @Override public String getLabel() { return "Defend"; }
}