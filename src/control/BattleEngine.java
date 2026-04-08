package control;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boundary.GameUI;
import control.strategy.TurnOrderStrategy;
import entity.combatant.Combatant;
import entity.combatant.Enemy;
import entity.combatant.Player;
import entity.combatant.interfaces.Stunnable;
import entity.level.Level;

// SRP: manages battle flow only
public class BattleEngine {

    private final GameUI ui;
    private final TurnOrderStrategy turnStrategy;
    private final Level level;
    private final Player player;
    private final List<Combatant> allCombatants = new ArrayList<>();
    private int currentRound = 0;

    public BattleEngine(GameUI ui, TurnOrderStrategy turnStrategy, Level level, Player player) {
        this.ui = ui;
        this.turnStrategy = turnStrategy;
        this.level = level;
        this.player = player;
        allCombatants.add(player);
        allCombatants.addAll(level.getInitialEnemies());
    }

    public boolean startBattle() {
        while (true) {
            currentRound++;

            // Backup spawn check at round start
            if (getLivingEnemies().isEmpty() && level.isBackupAvailable()) {
                List<Enemy> backup = level.triggerBackup();
                allCombatants.addAll(backup);
                ui.displayActionResult("--- BACKUP SPAWN! " + backup.stream()
                        .map(Enemy::getName).collect(Collectors.joining(", ")) + " enter the arena! ---");
            }
            
            ui.displayRoundStart(currentRound, allCombatants);
            
            List<Combatant> turnOrder = turnStrategy.determineTurnOrder(
                allCombatants.stream().filter(Combatant::isAlive).collect(Collectors.toList()));
                
            for (Combatant combatant : turnOrder) {
                if (!combatant.isAlive()) continue;
                // Status effects applied by self tick at the beginning of the next turn
                combatant.getStatus().tick(ui, true);
                if (!combatant.isAlive()) continue;

                processTurn(combatant);
                
                // Decrement special cooldown after each of the player's turns
                if (combatant instanceof Player) {
                    ((Player) combatant).decrementCooldown();
                }
                // Status effects applied by other combatants tick at the end of the turn
                combatant.getStatus().tick(ui, false);
                
                if (checkBattleEnd()) {
                    return player.isAlive();
                }
            }
        }
    }

    private void processTurn(Combatant combatant) {
        if (combatant instanceof Stunnable && ((Stunnable) combatant).isStunned()) {
            ((Stunnable) combatant).showStun(ui);
            return;
        }
        if (combatant instanceof Player) {
            List<Enemy> living = getLivingEnemies();
            var action = ui.getPlayerAction((Player) combatant, living);
            action.execute(combatant, allCombatants, ui);
        } else if (combatant instanceof Enemy) {
            List<Combatant> targets = new ArrayList<>();
            if (player.isAlive()) targets.add(player);
            ((Enemy) combatant).takeTurn(targets, ui);
        }
    }

    private boolean checkBattleEnd() {
        if (!player.isAlive()) return true;
        if (getLivingEnemies().isEmpty() && !level.isBackupAvailable()) return true;
        return false;
    }

    private List<Enemy> getLivingEnemies() {
        return allCombatants.stream()
                .filter(c -> c instanceof Enemy && c.isAlive())
                .map(c -> (Enemy) c)
                .collect(Collectors.toList());
    }

    public int getRound() { return currentRound; }
}