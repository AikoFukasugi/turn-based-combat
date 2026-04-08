package entity.combatant.interfaces;

import boundary.GameUI;
import entity.combatant.CombatantStatusEffects;
import entity.effect.StunEffect;

public interface Stunnable {

    /**
     * Applies a stun of the given duration in turns.
     * Implementations should take the max of current and new duration.
     */
    CombatantStatusEffects getStatus();
    String getName();

    /**
     * Returns true if a StunEffect is currently present in the status effect list.
     */
    default boolean isStunned() {
        CombatantStatusEffects status = getStatus(); 
        return status.contains(StunEffect.class);
    }

    default void applyStun(int duration) {
        CombatantStatusEffects status = getStatus();
        status.add(new StunEffect(duration));
    }

    default void showStun(GameUI ui) {
        ui.displayActionResult(getName() + " is STUNNED -- turn skipped!");
    }


}