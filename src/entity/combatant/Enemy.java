package entity.combatant;

import entity.action.ActionContext;
import entity.action.enemy.EnemyBasicAttack;
import entity.action.interfaces.Action;
import entity.combatant.interfaces.Stunnable;

public class Enemy extends Combatant implements Stunnable {

    public Enemy(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        actions.add(new EnemyBasicAttack());
    }

    public Action chooseAction(ActionContext ctx) {
        if (isStunned()) {
            showStun(ctx.ui);
            return null;
        }
        return actions.ready(ctx).get(0);
    }
}
