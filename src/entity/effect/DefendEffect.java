package entity.effect;

import boundary.GameUI;

public class DefendEffect extends StatusEffect {
    public DefendEffect() { name = "Defend"; duration = 2; value = 10; stackable = true; }

    @Override
    public void onExpire(String c, GameUI ui) {
        ui.displayActionResult(c + "'s Defend effect expires.");
    }
}