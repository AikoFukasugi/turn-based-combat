package entity.combatant.player;

import java.util.List;

import entity.action.player.wizard.ArcaneBlast;
import entity.action.player.wizard.WizardBasicAttack;
import entity.item.Item;

public class Wizard extends Player {

    public Wizard(List<Item> items) {
        super("Wizard", 200, 50, 10, 20, items);
        actions.add(new WizardBasicAttack());
        actions.add(new ArcaneBlast());
    }
}