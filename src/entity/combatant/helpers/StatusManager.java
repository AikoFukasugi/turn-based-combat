package entity.combatant.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import boundary.GameUI;
import entity.combatant.Combatant;
import entity.effect.NonStackableEffect;
import entity.effect.StatusEffect;

/**
 * Manages all status effects for a single Combatant.
 * Uses a HashMap for both stackable and non-stackable effects.
 * - Non-stackable: one entry per effect type (highest duration wins)
 * - Stackable: List<StatusEffect> per type, allowing duplicates
 */
public class StatusManager {

    // Non-stackable: one effect per type
    private final Map<Class<? extends StatusEffect>, StatusEffect> nonStackable = new HashMap<>();

    // Stackable: multiple effects of the same type, keyed by type
    private final Map<Class<? extends StatusEffect>, List<StatusEffect>> stackable = new HashMap<>();

    private final Combatant owner;

    public StatusManager(Combatant owner) {
        this.owner = owner;
    }

    public void add(StatusEffect effect, GameUI ui) {
        if (effect instanceof NonStackableEffect) {
            NonStackableEffect e = (NonStackableEffect) effect;
            // Since effects are hashed by class, can cast exsiting to non-stackable
            NonStackableEffect existing = (NonStackableEffect) nonStackable.get(effect.getClass());
            if (existing != null) { ((StatusEffect) existing).remove(owner, ui); }
            // Type cast is safe as all non-stackable effects extend status effect
            StatusEffect combined = (StatusEffect) e.combine(existing);
            nonStackable.put(effect.getClass(), combined);
            combined.apply(owner, ui);
        } else {
            stackable.computeIfAbsent(effect.getClass(), k -> new ArrayList<>()).add(effect);
            effect.apply(owner, ui);
        }
    }


    /**
     * Returns all active effects of a given type.
     * - Non-stackable: returns a single-element list, or empty if not present.
     * - Stackable: returns all stacked instances of that type.
     */
    public List<StatusEffect> get(Class<? extends StatusEffect> type) {
        if (nonStackable.containsKey(type)) {
            List<StatusEffect> result = new ArrayList<>();
            result.add(nonStackable.get(type));
            return result;
        }
        return stackable.getOrDefault(type, new ArrayList<>());
    }


    /**
     * Returns all active effects across both maps as a flat list.
     */
    public List<StatusEffect> all() {
        List<StatusEffect> all = new ArrayList<>(nonStackable.values());
        stackable.values().forEach(all::addAll);
        return all;
    }

    /**
     * Returns true if any effect of the given type is currently active.
     */
    public boolean contains(Class<? extends StatusEffect> type) {
        return nonStackable.containsKey(type) ||
            stackable.getOrDefault(type, new ArrayList<>()).stream().anyMatch(e -> !e.isExpired());
    }


    /**
     * Ticks all effects matching the given begin flag.
     * begin=true  → start-of-turn effects
     * begin=false → end-of-turn effects
     */
    public void tick(GameUI ui, boolean begin) {
        // Tick non-stackable effects
        Iterator<Map.Entry<Class<? extends StatusEffect>, StatusEffect>> mapIt = nonStackable.entrySet().iterator();
        while (mapIt.hasNext()) {
            StatusEffect e = mapIt.next().getValue();
            if (e.isBegin() != begin) continue;
            e.tick(owner, ui);
            if (e.isExpired()) {
                mapIt.remove();
            }
        }

        // Tick stackable effects — iterate each type's list
        for (List<StatusEffect> effects : stackable.values()) {
            Iterator<StatusEffect> listIt = effects.iterator();
            while (listIt.hasNext()) {
                StatusEffect e = listIt.next();
                if (e.isBegin() != begin) continue;
                e.tick(owner, ui);
                if (e.isExpired()) {
                    listIt.remove();
                }
            }
        }

        // Clean up empty stackable lists from the map
        stackable.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    @Override
    public String toString() {
        List<StatusEffect> all = all();
        if (all.isEmpty()) return "";
        return all.stream()
                .map(StatusEffect::toString)
                .collect(Collectors.joining(" "));
    }


}