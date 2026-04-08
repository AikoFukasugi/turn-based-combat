package entity.action;

import java.util.List;

import boundary.GameUI;
import entity.combatant.Combatant;
import entity.effect.DefendEffect;

public class DefendAction implements Action {
    @Override
    public void execute(Combatant actor, List<Combatant> allCombatants, GameUI ui) {
        actor.getStatus().add(new DefendEffect());
        ui.displayActionResult(actor.getName() + " takes a defensive stance! +10 DEF for 2 turns.");
    }
}