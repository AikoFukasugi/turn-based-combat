package entity.combatant.interfaces;

import boundary.GameUI;
import entity.combatant.Combatant;
import entity.combatant.helpers.StatusManager;
import entity.effect.SmokeBombEffect;

public interface SmokeBombable {

    /**
     * Grants access to the combatant's active status effects.
     * Implemented by Combatant — no duplication needed.
     */
    StatusManager getStatus();
    String getName();


    /**
     * Returns true if a SmokeBombEffect is currently active.
     * Default implementation checks the status effect list —
     * no state field needed in any class.
     */
    default boolean isSmokeBombActive() {
        StatusManager status = getStatus();
        return status.contains(SmokeBombEffect.class);
    }

    default void applySmokeBomb(int duration, GameUI ui) {
        StatusManager status = getStatus();
        status.add(new SmokeBombEffect(duration), ui); 
    }

    default void attackedWithSmokeBomb(Combatant attacker, GameUI ui) {
        ui.displayActionResult(attacker.getName() + " attacks " + getName() + " -- 0 damage (Smoke Bomb active)!");
    }
}
