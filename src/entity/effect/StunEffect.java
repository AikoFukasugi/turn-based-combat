package entity.effect;

import boundary.GameUI;

public class StunEffect extends StatusEffect {
    public StunEffect(int duration) { 
        name = "Stun"; 
        this.duration = duration;
        begin = false; 
    }

    @Override
    public void onExpire(String c, GameUI ui) {
        ui.displayActionResult("Stun effect on " + c + " has expired.");
    }
}