package entity.effect;

import boundary.GameUI;

public class SmokeBombEffect extends StatusEffect {
    public SmokeBombEffect(int duration) { this.name = "SmokeBomb"; this.duration = duration; }

    @Override
    public void onExpire(String c, GameUI ui) {
        ui.displayActionResult("Smoke Bomb effect on " + c + " has expired.");
    }
}