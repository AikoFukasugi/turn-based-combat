package entity.combatant;

import java.util.List;

import boundary.GameUI;
import entity.combatant.interfaces.Stunnable;
import entity.item.Item;

public class Warrior extends Player {


    public Warrior(List<Item> items) {
        super("Warrior", 260, 40, 20, 30, items);
    }

    @Override
    public void executeSpecialSkill(List<Combatant> targets, GameUI ui) {
        if (targets.isEmpty()) return;
        // Shield Bash: prompt is handled by caller; targets list has the chosen enemy
        // ? Is target supposed to be selected by player?
        Combatant target = targets.get(0);
        int dmg = Math.max(0, attack - target.getEffectiveDefense());
        target.takeDamage(dmg);
        if (target instanceof Stunnable) {
            ((Stunnable) target).applyStun(2);
        }
        ui.displayActionResult(name + " uses Shield Bash on " + target.getName() +
                "! Deals " + dmg + " dmg, STUNNED for 2 turns!");
        if (!target.isAlive())
            ui.displayActionResult(target.getName() + " is ELIMINATED!");
    }
}