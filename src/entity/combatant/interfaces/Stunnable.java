package entity.combatant.interfaces;

import boundary.GameUI;
import entity.combatant.helpers.StatusManager;
import entity.effect.StunEffect;

public interface Stunnable {

    /**
     * Applies a stun of the given duration in turns.
     * Implementations should take the max of current and new duration.
     */
    StatusManager getStatus();
    String getName();

    /**
     * Returns true if a StunEffect is currently present in the status effect list.
     */
    default boolean isStunned() {
        StatusManager status = getStatus(); 
        return status.contains(StunEffect.class);
    }

    default void applyStun(int duration, GameUI ui) {
        StatusManager status = getStatus();
        status.add(new StunEffect(duration), ui);
    }

    default void showStun(GameUI ui) {
        ui.displayActionResult(getName() + " is STUNNED -- turn skipped!");
    }


}