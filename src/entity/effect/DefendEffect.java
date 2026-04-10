package entity.effect;

import entity.combatant.helpers.StatField;

public class DefendEffect extends DurationStatEffect {
    public DefendEffect() { 
        super("Defend", 2, true, 10, StatField.defense); 
    }
}