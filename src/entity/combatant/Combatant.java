package entity.combatant;

import entity.action.ActionContext;
import entity.action.interfaces.Action;
import entity.combatant.helpers.ActionMenu;
import entity.combatant.helpers.Stats;
import entity.combatant.helpers.StatusManager;

public abstract class Combatant {
    protected final String name;
    protected int hp;
    protected final Stats baseStats;
    protected final Stats statEffects;
    protected final StatusManager status;
    protected final ActionMenu actions;

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.hp = hp;
        this.baseStats = new Stats(hp, attack, defense, speed);
        this.statEffects = new Stats(0, 0, 0, 0);
        this.status = new StatusManager(this);
        this.actions = new ActionMenu();
    }


    public abstract Action chooseAction(ActionContext ctx);

    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
    }
    
    public Stats stats() { return baseStats.add(statEffects); }
    public void setHp(int hp) { this.hp = hp; }
    
    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public boolean isAlive() { return hp > 0; }
    public StatusManager getStatus() { return status; }
    public ActionMenu getActions() { return actions; }
    public Stats getBaseStats() { return baseStats; }
    public Stats getStatEffects() { return statEffects; }
}