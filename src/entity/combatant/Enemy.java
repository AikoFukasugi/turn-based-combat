package entity.combatant;

import java.util.List;

import boundary.GameUI;
import entity.combatant.interfaces.SmokeBombable;
import entity.combatant.interfaces.Stunnable;

public class Enemy extends Combatant implements Stunnable {

    public Enemy(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
    }

    public void takeTurn(List<Combatant> targets, GameUI ui) {
        if (targets.isEmpty()) return;
        Combatant target = targets.get(0);
        int dmg = Math.max(0, attack - target.getEffectiveDefense());

        // SmokeBombEffect on target overrides damage to 0 — handled in effect
        // We query effectiveIncomingDamage if target has smoke bomb
        if (target instanceof SmokeBombable && ((SmokeBombable) target).isSmokeBombActive()) {
            ((SmokeBombable) target).attackedWithSmokeBomb(this, ui);
            return;
        }
        target.takeDamage(dmg);
        ui.displayActionResult(name + " attacks " + target.getName() +
                " for " + dmg + " dmg! HP: " + target.getHp() + "/" + target.getMaxHp());
        if (!target.isAlive())
            ui.displayActionResult(target.getName() + " has been DEFEATED!");
    }
}
