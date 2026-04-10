package control.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import entity.combatant.Combatant;
import entity.combatant.helpers.StatField;

public class SpeedBasedTurnOrder implements TurnOrderStrategy {
    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> ordered = new ArrayList<>(combatants);
        ordered.sort(Comparator.comparingInt((Combatant c) -> c.stats().get(StatField.speed)).reversed());
        return ordered;
    }
}