package entity.effect;

import boundary.GameUI;
import entity.combatant.Combatant;
import entity.combatant.helpers.StatField;

public class PermanentStatEffect extends PermanentEffect {
    protected final int value;
    protected final StatField stat;
    
    public PermanentStatEffect(String name, boolean begin, int value, StatField stat) {
        super(name, begin);
        this.value = value;
        this.stat = stat;
    }

    public int getValue() { return value; }
    public StatField getStat() { return stat; }

    @Override
    public void apply(Combatant target, GameUI ui) {
        target.getStatEffects().add(stat, value);
    }

    @Override
    public void remove(Combatant target, GameUI ui) {
        target.getStatEffects().subtract(stat, value);
    }
}
