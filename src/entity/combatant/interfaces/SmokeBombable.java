package entity.combatant.interfaces;

import boundary.GameUI;
import entity.combatant.Combatant;
import entity.combatant.CombatantStatusEffects;
import entity.effect.SmokeBombEffect;

public interface SmokeBombable {

    /**
     * Grants access to the combatant's active status effects.
     * Implemented by Combatant — no duplication needed.
     */
    CombatantStatusEffects getStatus();
    String getName();


    /**
     * Returns true if a SmokeBombEffect is currently active.
     * Default implementation checks the status effect list —
     * no state field needed in any class.
     */
    default boolean isSmokeBombActive() {
        CombatantStatusEffects status = getStatus();
        return status.contains(SmokeBombEffect.class);
    }

    default void applySmokeBomb(int duration) { 
        CombatantStatusEffects status = getStatus();
        status.add(new SmokeBombEffect(duration)); 
    }

    default void attackedWithSmokeBomb(Combatant attacker, GameUI ui) {
        ui.displayActionResult(attacker.getName() + " attacks " + getName() + " -- 0 damage (Smoke Bomb active)!");
    }
}
