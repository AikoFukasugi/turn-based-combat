package entity.combatant;

import entity.effect.DefendEffect;

public class Combatant {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected int speed;
    protected boolean alive = true;
    protected CombatantStatusEffects status;

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.status = new CombatantStatusEffects(name);
    }

    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
        if (hp == 0) alive = false;
    }
    
    public int getEffectiveDefense() {
        int bonus = status.getValue(DefendEffect.class);
        return defense + bonus;
    }

    public CombatantStatusEffects getStatus() { return status; }


    public void setHp(int hp) { this.hp = hp; }

    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getSpeed() { return speed; }
    public boolean isAlive() { return alive; }
}