package boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entity.action.Action;
import entity.action.BasicAttack;
import entity.action.DefendAction;
import entity.action.ItemAction;
import entity.action.SpecialSkillAction;
import entity.combatant.Combatant;
import entity.combatant.Enemy;
import entity.combatant.Player;
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
                        c.getName(), c.getHp(), c.getMaxHp(), c.getStatus().toString());
            }
        }
        System.out.println("=====================================================");
    }

    public Action getPlayerAction(Player player, List<Enemy> livingEnemies) {
        System.out.println("\nYour turn! Choose an action:");
        System.out.println("1. Basic Attack");
        System.out.println("2. Defend (+10 DEF this round + next)");

        List<Item> usable = player.getUsableItems();
        if (!usable.isEmpty()) {
            System.out.println("3. Use Item");
        } else {
            System.out.println("3. Use Item  [NONE AVAILABLE]");
        }

        String cooldownMsg = player.getSpecialCooldown() > 0
                ? "[COOLDOWN: " + player.getSpecialCooldown() + "]" : "[READY]";
        System.out.println("4. Special Skill " + cooldownMsg);

        int choice = readChoice(1, 4);
        switch (choice) {
            case 1: {
                Combatant target = selectTarget(livingEnemies);
                return new BasicAttack(target);
            }
            case 2:
                return new DefendAction();
            case 3: {
                if (usable.isEmpty()) {
                    System.out.println("No items available! Defaulting to Basic Attack.");
                    Combatant target = selectTarget(livingEnemies);
                    return new BasicAttack(target);
                }
                Item item = selectItem(usable);
                List<Combatant> targets = new ArrayList<>(livingEnemies);
                return new ItemAction(item, targets);
            }
            case 4: {
                if (player.getSpecialCooldown() > 0) {
                    System.out.println("Special skill on cooldown! Defaulting to Basic Attack.");
                    Combatant target = selectTarget(livingEnemies);
                    return new BasicAttack(target);
                }
                List<Combatant> targets = new ArrayList<>(livingEnemies);
                return new SpecialSkillAction(targets);
            }
            default:
                return new BasicAttack(livingEnemies.get(0));
        }
    }

    private Combatant selectTarget(List<Enemy> enemies) {
        System.out.println("Select target:");
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            System.out.printf("  %d. %-10s HP: %d%n", i + 1, e.getName(), e.getHp());
        }
        int idx = readChoice(1, enemies.size()) - 1;
        return enemies.get(idx);
    }

    private Item selectItem(List<Item> items) {
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
            System.out.println("  ★ VICTORY! Congratulations, you defeated all enemies!");
            System.out.printf("  Remaining HP: %d/%d  |  Total Rounds: %d%n",
                    player.getHp(), player.getMaxHp(), rounds);
        } else {
            System.out.println("  ✗ DEFEATED. Don't give up, try again!");
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
}