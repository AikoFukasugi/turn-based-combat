package entity.combatant.interfaces;

import entity.combatant.helpers.StatField;
import entity.combatant.helpers.Stats;

public interface Healable {
    int getHp();
    Stats stats();
    void setHp(int hp);
    String getName();

    /**
     * Heals the combatant by the given amount, capped at maxHp.
     * Default implementation delegates to the concrete heal logic.
     */
    default void heal(int amount) {
        int new_hp = Math.min(stats().get(StatField.maxHp), getHp() + amount);
        setHp(new_hp);
    }
}