package entity.action.interfaces;

import java.util.List;

import entity.action.ActionContext;
import entity.combatant.Combatant;

public abstract class SpecialAttack implements BasicAttack {
    protected int cooldown;
    protected int specialCooldown;

    public SpecialAttack(int cooldown) { this.cooldown = 0; this.specialCooldown = cooldown; }

    public void resetCooldown() { cooldown = specialCooldown; }
    public int getCooldown() { return cooldown; }
    public void setCooldown(int cooldown) { this.cooldown = cooldown; }
    public void decrementCooldown() { cooldown--; }
    public boolean isReady(ActionContext ctx) { return cooldown <= 0; }

    @Override
    public boolean execute(ActionContext ctx) {
        if (!isReady(ctx)) return false;
        List<Combatant> targets = selectTargets(ctx);
        for (Combatant t : targets) { executeOn(t, ctx); }
        resetCooldown();
        return true;
    };
}
