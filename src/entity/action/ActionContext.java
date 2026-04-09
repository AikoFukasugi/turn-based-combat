package entity.action;

import java.util.List;
import java.util.stream.Collectors;

import boundary.GameUI;
import entity.combatant.Combatant;
import entity.combatant.Enemy;
import entity.combatant.Player;
import entity.item.Item;

/**
 * Carries all runtime data an Action needs during execution.
 * Target resolution is handled here — Actions read from context, never prompt UI directly.
 */
public class ActionContext {

    public final Combatant actor;
    public final List<Combatant> allCombatants;
    public final GameUI ui;
    public Item selectedItem;

    public ActionContext(Combatant actor,
                         List<Combatant> allCombatants,
                         Item selectedItem,
                         GameUI ui) {
        this.actor          = actor;
        this.allCombatants  = allCombatants;
        this.selectedItem   = selectedItem;
        this.ui             = ui;
    }

    // ── Team helpers ─────────────────────────────────────────

    /**
     * Returns all living combatants on the same team as the actor.
     * Player → all living Players
     * Enemy  → all living Enemies
     */
    public List<Combatant> getLivingAllies() {
        return allCombatants.stream()
                .filter(c -> c.isAlive() && isSameTeam(actor, c))
                .collect(Collectors.toList());
    }

    /**
     * Returns all living combatants on the opposing team.
     * Player → all living Enemies
     * Enemy  → all living Players
     */
    public List<Combatant> getLivingOpponents() {
        return allCombatants.stream()
                .filter(c -> c.isAlive() && isOpposingTeam(actor, c))
                .collect(Collectors.toList());
    }

    /**
     * Returns all living combatants regardless of team.
     */
    public List<Combatant> getLivingAll() {
        return allCombatants.stream()
                .filter(Combatant::isAlive)
                .collect(Collectors.toList());
    }

    // ── Team checks ───────────────────────────────────────────

    /**
     * Returns true if both combatants are on the same team.
     */
    public static boolean isSameTeam(Combatant a, Combatant b) {
        return teamOf(a) == teamOf(b);
    }

    /**
     * Returns true if the two combatants are on opposing teams.
     */
    public static boolean isOpposingTeam(Combatant a, Combatant b) {
        return teamOf(a) != teamOf(b);
    }

    /**
     * Resolves the team of a combatant.
     * Player → Team.PLAYER
     * Enemy  → Team.ENEMY
     */
    public static Team teamOf(Combatant c) {
        if (c instanceof Player) return Team.PLAYER;
        if (c instanceof Enemy)  return Team.ENEMY;
        throw new IllegalArgumentException(
                "Unknown combatant type: " + c.getClass().getSimpleName());
    }

    // ── Team enum ─────────────────────────────────────────────

    public enum Team {
        PLAYER,
        ENEMY
    }
}