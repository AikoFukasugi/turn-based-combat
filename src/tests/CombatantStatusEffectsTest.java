package tests;

import boundary.GameUI;
import entity.combatant.CombatantStatusEffects;
import entity.effect.DefendEffect;
import entity.effect.SmokeBombEffect;
import entity.effect.StatusEffect;
import entity.effect.StunEffect;

public class CombatantStatusEffectsTest {

    public static void main(String[] args) {
        GameUI ui = new GameUI();
        CombatantStatusEffects cse = new CombatantStatusEffects("Hero");

        // ── add() + contains() ───────────────────────────────
        System.out.println("--- add() + contains() ---");
        cse.add(new StunEffect(2));           // non-stackable, duration 2
        cse.add(new StunEffect(2));           // equal duration — ignored
        cse.add(new DefendEffect());         // stackable
        cse.add(new DefendEffect());         // second stack — allowed
        cse.add(new SmokeBombEffect(2));      // non-stackable, duration 2
        System.out.println("Has StunEffect:      " + cse.contains(StunEffect.class));      // true
        System.out.println("Has DefendEffect:    " + cse.contains(DefendEffect.class));    // true
        System.out.println("Has SmokeBombEffect: " + cse.contains(SmokeBombEffect.class)); // true
        System.out.println("Has TestEffect (absent): " + cse.contains(TestEffect.class)); // false

        // ── get() ────────────────────────────────────────────
        System.out.println("\n--- get() ---");
        System.out.println("StunEffect list size:      " + cse.get(StunEffect.class).size());      // 1
        System.out.println("DefendEffect list size:    " + cse.get(DefendEffect.class).size());    // 2
        System.out.println("SmokeBombEffect list size: " + cse.get(SmokeBombEffect.class).size()); // 1
        System.out.println("Absent type list size:     " + cse.get(TestEffect.class).size()); // 0

        // ── all() ─────────────────────────────────────────────
        System.out.println("\n--- all() ---");
        System.out.println("Total effects: " + cse.all().size()); // 4 (1 stun + 2 defend + 1 smoke)

        // ── getValue() ───────────────────────────────────────
        System.out.println("\n--- getValue() ---");
        System.out.println("StunEffect value:           " + cse.getValue(StunEffect.class));      // 0
        System.out.println("DefendEffect value (20+20): " + cse.getValue(DefendEffect.class));    // 20
        System.out.println("Absent type value:          " + cse.getValue(TestEffect.class)); // 0

        // ── toString() ───────────────────────────────────────
        System.out.println("\n--- toString() ---");
        System.out.println("Status summary: " + cse);

        // ── tick() begin=true — decrement ────────────────────
        System.out.println("\n--- tick(begin=true) x1 ---");
        cse.tick(ui, true);
        System.out.println("StunEffect duration after tick 1: " +
                cse.get(StunEffect.class).stream()
                   .mapToInt(StatusEffect::getDuration)
                   .findFirst().orElse(-1)); // 1
        System.out.println("Total effects after tick 1: " + cse.all().size()); // 4

        // ── tick() begin=true — expiry ────────────────────────
        System.out.println("\n--- tick(begin=true) x2 ---");
        cse.tick(ui, true); // all duration=1 effects expire
        System.out.println("Has StunEffect:      " + cse.contains(StunEffect.class));      // false
        System.out.println("Has SmokeBombEffect: " + cse.contains(SmokeBombEffect.class)); // false
        System.out.println("Has DefendEffect:    " + cse.contains(DefendEffect.class));    // false
        System.out.println("Total effects:       " + cse.all().size());                    // 0

        // ── tick() begin=false — respects flag ────────────────
        System.out.println("\n--- tick(begin=false) ---");
        CombatantStatusEffects cse2 = new CombatantStatusEffects("Hero2");
        StunEffect endOfTurnStun = new StunEffect(1);
        // end-of-turn effect
        cse2.add(endOfTurnStun);
        cse2.tick(ui, true);               // begin=true tick — should NOT touch it
        System.out.println("After begin=false tick 1 -- Has StunEffect: " + cse2.contains(StunEffect.class)); // true
        cse2.tick(ui, false);              // begin=false tick — expires
        System.out.println("After begin=false tick 2 -- Has StunEffect:" + cse2.contains(StunEffect.class)); // false

        // ── higher duration wins on non-stackable ─────────────
        System.out.println("\n--- add() duration priority ---");
        CombatantStatusEffects cse3 = new CombatantStatusEffects("Hero3");
        StunEffect weak   = new StunEffect(1);
        StunEffect strong = new StunEffect(3);
        cse3.add(weak);
        cse3.add(strong); // replaces weak
        System.out.println("Duration after adding weak then strong: " +
                cse3.get(StunEffect.class).get(0).getDuration()); // 3
        cse3.add(weak);   // lower — ignored
        System.out.println("Duration after adding weak again:       " +
                cse3.get(StunEffect.class).get(0).getDuration()); // still 3

        System.out.println("\n--- All tests complete ---");
    }
}