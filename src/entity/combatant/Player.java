package entity.combatant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entity.combatant.interfaces.Healable;
import entity.combatant.interfaces.SmokeBombable;
import entity.item.Item;

public abstract class Player extends Combatant implements Healable, SmokeBombable {
    protected List<Item> inventory;
    protected int specialCooldown = 0;

    public Player(String name, int hp, int attack, int defense, int speed, List<Item> items) {
        super(name, hp, attack, defense, speed);
        this.inventory = new ArrayList<>(items);
    }

    public List<Item> getUsableItems() {
        return inventory.stream().filter(i -> !i.isUsed()).collect(Collectors.toList());
    }

    public int getSpecialCooldown() { return specialCooldown; }

    public void setSpecialCooldown(int cd) { specialCooldown = cd; }

    public void decrementCooldown() {
        if (specialCooldown > 0) specialCooldown--;
    }

    public abstract void executeSpecialSkill(List<Combatant> targets, boundary.GameUI ui);


}