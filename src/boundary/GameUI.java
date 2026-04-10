package boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entity.action.interfaces.Action;
import entity.combatant.Combatant;
import entity.combatant.helpers.StatField;
import entity.combatant.player.Player;
import entity.item.Item;
import entity.level.Difficulty;

public class GameUI {

    private final Scanner scanner = new Scanner(System.in);

    public void displayWelcome() {
        System.out.println("===========================================");
        System.out.println("   WELCOME TO TURN-BASED COMBAT ARENA");
        System.out.println("===========================================");
    }

    public int selectPlayerType() {
        System.out.println("\n--- SELECT YOUR PLAYER ---");
        System.out.println("1. Warrior  [HP:260 | ATK:40 | DEF:20 | SPD:30]");
        System.out.println("   Special: Shield Bash -- deal damage + stun target 2 turns");
        System.out.println("2. Wizard   [HP:200 | ATK:50 | DEF:10 | SPD:20]");
        System.out.println("   Special: Arcane Blast -- damage all enemies; +10 ATK per kill");
        return readChoice(1, 2);
    }

    public List<Item> selectItems() {
        List<Item> chosen = new ArrayList<>();
        System.out.println("\n--- SELECT 2 ITEMS (duplicates allowed) ---");
        System.out.println("1. Potion       -- Heal 100 HP");
        System.out.println("2. Power Stone  -- Free use of special skill (no cooldown change)");
        System.out.println("3. Smoke Bomb   -- Enemy attacks deal 0 dmg this turn + next");
        for (int i = 1; i <= 2; i++) {
            System.out.print("Item " + i + ": ");
            int pick = readChoice(1, 3);
            chosen.add(createItem(pick));
        }
        return chosen;
    }

    private Item createItem(int pick) {
        switch (pick) {
            case 1: return new entity.item.Potion();
            case 2: return new entity.item.PowerStone();
            default: return new entity.item.SmokeBomb();
        }
    }

    public Difficulty selectDifficulty() {
        System.out.println("\n--- SELECT DIFFICULTY ---");
        System.out.println("1. Easy   -- 3 Goblins");
        System.out.println("2. Medium -- 1 Goblin + 1 Wolf | Backup: 2 Wolves");
        System.out.println("3. Hard   -- 2 Goblins | Backup: 1 Goblin + 2 Wolves");
        int pick = readChoice(1, 3);
        switch (pick) {
            case 1: return Difficulty.EASY;
            case 2: return Difficulty.MEDIUM;
            default: return Difficulty.HARD;
        }
    }

    public void displayRoundStart(int round, List<Combatant> combatants) {
        System.out.println("\n=================== ROUND " + round + " ===================");
        for (Combatant c : combatants) {
            if (c.isAlive()) {
                System.out.printf("  %-14s HP: %3d/%-3d  %s%n",
                        c.getName(), c.getHp(), c.stats().get(StatField.maxHp), c.getStatus().toString());
            }
        }
        System.out.println("=====================================================");
    }

    public Combatant selectTarget(List<Combatant> combatants) {
        System.out.println("Select target:");
        for (int i = 0; i < combatants.size(); i++) {
            Combatant c = combatants.get(i);
            System.out.printf("  %d. %-10s HP: %d%n", i + 1, c.getName(), c.getHp());
        }
        int idx = readChoice(1, combatants.size()) - 1;
        return combatants.get(idx);
    }

    public Item selectItem(List<Item> items) {
        System.out.println("Select item:");
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, items.get(i).getName());
        }
        return items.get(readChoice(1, items.size()) - 1);
    }

    public void displayActionResult(String msg) {
        System.out.println("  >> " + msg);
    }

    public void displayBattleEnd(boolean playerWon, Player player, int rounds) {
        System.out.println("\n=====================================================");
        if (playerWon) {
            System.out.println("VICTORY! Congratulations, you defeated all enemies!");
            System.out.printf("  Remaining HP: %d/%d  |  Total Rounds: %d%n",
                    player.getHp(), player.stats().get(StatField.maxHp), rounds);
        } else {
            System.out.println("DEFEATED. Don't give up, try again!");
            System.out.printf("  Total Rounds Survived: %d%n", rounds);
        }
        System.out.println("=====================================================");
    }

    public int askReplay() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Replay with same settings");
        System.out.println("2. Start a new game");
        System.out.println("3. Exit");
        return readChoice(1, 3);
    }

    private int readChoice(int min, int max) {
        while (true) {
            System.out.print("> ");
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    /**
     * Displays all actions the combatant possesses.
     * Greys out unready actions and only accepts valid input from ready ones.
     *
     * @param allActions   full action list to display
     * @param readyActions subset that can currently be chosen
     * @param owner        the acting combatant
     */
    public Action selectAction(List<Action> allActions,
                                List<Action> readyActions,
                                Combatant owner) {
        System.out.println("\n" + owner.getName() + "'s turn -- choose an action:");

        for (int i = 0; i < allActions.size(); i++) {
            Action action = allActions.get(i);
            boolean ready = readyActions.contains(action);

            if (ready) {
                System.out.printf("  %d. %s%n", i + 1, action.getLabel());
            } else {
                // Display but visually indicate unavailable
                System.out.printf("  %d. %s  [UNAVAILABLE]%n", i + 1, action.getLabel());
            }
        }

        // Only accept numbers that correspond to a ready action
        while (true) {
            System.out.print("> ");
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= 1 && input <= allActions.size()) {
                    Action chosen = allActions.get(input - 1);
                    if (readyActions.contains(chosen)) {
                        return chosen;
                    }
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid choice. Please select a ready action.");
        }
}
}