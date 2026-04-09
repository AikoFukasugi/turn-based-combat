package entity.combatant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entity.action.ActionContext;
import entity.action.interfaces.Action;
import entity.action.player.DefendAction;
import entity.action.player.ItemAction;
import entity.combatant.interfaces.Healable;
import entity.combatant.interfaces.SmokeBombable;
import entity.item.Item;

public abstract class Player extends Combatant implements Healable, SmokeBombable {
    protected List<Item> inventory;
    protected int specialCooldown = 0;

    public Player(String name, int hp, int attack, int defense, int speed, List<Item> items) {
        super(name, hp, attack, defense, speed);
        actions.add(new DefendAction());
        actions.add(new ItemAction());
        this.inventory = new ArrayList<>(items);
    }

    public List<Item> getUsableItems() {
        return inventory.stream().filter(i -> !i.isUsed()).collect(Collectors.toList());
    }

    public boolean hasUsableItem() { return !getUsableItems().isEmpty(); }

    public Action chooseAction(ActionContext ctx) { 
        Action chosen = ctx.ui.selectAction(actions.all(), actions.ready(ctx), this);
        if (chosen instanceof ItemAction) {
            ctx.selectedItem = ctx.ui.selectItem(getUsableItems());
        }
        return chosen;
    }

}