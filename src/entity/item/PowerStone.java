package entity.item;

import entity.action.ActionContext;
import entity.action.interfaces.SpecialAttack;

public class PowerStone extends Item {
    public PowerStone() { this.name = "Power Stone"; }

    @Override
    public void use(ActionContext ctx) {
        SpecialAttack specialAttack = ctx.actor.getActions().getSpecial();
        if (specialAttack == null) return;
        ctx.ui.displayActionResult(ctx.actor.getName() + " uses Power Stone -- Special Skill triggered (no cooldown change)!");
        int cooldown = specialAttack.getCooldown();
        specialAttack.setCooldown(0);
        specialAttack.execute(ctx);
        specialAttack.setCooldown(cooldown);
        used = true;
    }
}