package entity.combatant;

import java.util.List;

import boundary.GameUI;
import entity.item.Item;

public class Wizard extends Player {

    public Wizard(List<Item> items) {
        super("Wizard", 200, 50, 10, 20, items);
    }

    @Override
    public void executeSpecialSkill(List<Combatant> targets, GameUI ui) {
        // Arcane Blast: hit all living enemies
        int kills = 0;
        for (Combatant target : targets) {
            if (!target.isAlive()) continue;
            int dmg = Math.max(0, attack - target.getEffectiveDefense());
            target.takeDamage(dmg);
            ui.displayActionResult(name + " Arcane Blasts " + target.getName() +
                    " for " + dmg + " dmg! HP: " + target.getHp());
            if (!target.isAlive()) {
                kills++;
                ui.displayActionResult(target.getName() + " is ELIMINATED!");
            }
        }
        if (kills > 0) {
            // TODO: implement as status effect instead
            attack += kills * 10;
            ui.displayActionResult("Arcane Bonus! ATK +" + (kills * 10) + " → ATK now " + attack);
        }
    }

    public void addBonusAttack(int amount) { this.attack += amount; }
}