package entity.combatant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import boundary.GameUI;
import entity.effect.StatusEffect;

/**
 * Manages all status effects for a single Combatant.
 * Uses a HashMap for both stackable and non-stackable effects.
 * - Non-stackable: one entry per effect type (highest duration wins)
 * - Stackable: List<StatusEffect> per type, allowing duplicates
 */
public class CombatantStatusEffects {

    // Non-stackable: one effect per type
    private final Map<Class<? extends StatusEffect>, StatusEffect> nonStackable = new HashMap<>();

    // Stackable: multiple effects of the same type, keyed by type
    private final Map<Class<? extends StatusEffect>, List<StatusEffect>> stackable = new HashMap<>();

    private final String owner;

    public CombatantStatusEffects(String owner) {
        this.owner = owner;
    }

    // ── Add ──────────────────────────────────────────────────

    public void add(StatusEffect effect) {
        if (effect.isStackable()) {
            stackable.computeIfAbsent(effect.getClass(), k -> new ArrayList<>()).add(effect);
        } else {
            StatusEffect existing = nonStackable.get(effect.getClass());
            int existingDuration = existing != null ? existing.getDuration() : 0;
            if (effect.getDuration() > existingDuration) {
                nonStackable.put(effect.getClass(), effect);
            }
        }
    }

    // ── Query ─────────────────────────────────────────────────

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

    public int getValue(Class<? extends StatusEffect> type) {
        List<StatusEffect> effects = get(type);
        return effects.stream().mapToInt(e -> e.getValue()).sum();
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

    // ── Tick ──────────────────────────────────────────────────

    /**
     * Ticks all effects matching the given begin flag.
     * begin=true  → start-of-turn effects
     * begin=false → end-of-turn effects
     */
    public void tick(GameUI ui, boolean begin) {
        // Tick non-stackable effects
        Iterator<Map.Entry<Class<? extends StatusEffect>, StatusEffect>> mapIt =
                nonStackable.entrySet().iterator();
        while (mapIt.hasNext()) {
            StatusEffect e = mapIt.next().getValue();
            if (e.isBegin() != begin) continue;
            e.decrementDuration();
            if (e.isExpired()) {
                e.onExpire(owner, ui);
                mapIt.remove();
            }
        }

        // Tick stackable effects — iterate each type's list
        for (List<StatusEffect> effects : stackable.values()) {
            Iterator<StatusEffect> listIt = effects.iterator();
            while (listIt.hasNext()) {
                StatusEffect e = listIt.next();
                if (e.isBegin() != begin) continue;
                e.decrementDuration();
                if (e.isExpired()) {
                    e.onExpire(owner, ui);
                    listIt.remove();
                }
            }
        }

        // Clean up empty stackable lists from the map
        stackable.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    // ── Display ───────────────────────────────────────────────

    @Override
    public String toString() {
        List<StatusEffect> all = all();
        if (all.isEmpty()) return "";
        return all.stream()
                .map(StatusEffect::toString)
                .collect(Collectors.joining(" "));
    }


}