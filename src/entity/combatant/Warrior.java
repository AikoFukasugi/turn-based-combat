package entity.combatant;

import java.util.List;

import entity.action.player.warrior.ShieldBash;
import entity.action.player.warrior.WarriorBasicAttack;
import entity.item.Item;

public class Warrior extends Player {
    public Warrior(List<Item> items) {
        super("Warrior", 260, 40, 20, 30, items);
        actions.add(new WarriorBasicAttack());
        actions.add(new ShieldBash());
    }
}