package control;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boundary.GameUI;
import control.strategy.TurnOrderStrategy;
import entity.action.ActionContext;
import entity.action.interfaces.Action;
import entity.combatant.Combatant;
import entity.combatant.enemy.Enemy;
import entity.combatant.player.Player;
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
                // Status effects applied by self tick at the beginning of the next turn
                // Cooldowns and Status effects applied by other combatants tick at the end of the turn
                if (!combatant.isAlive()) continue;
                combatant.getStatus().tick(ui, true);
                if (!combatant.isAlive()) continue;
                takeTurn(combatant);
                combatant.getStatus().tick(ui, false);
                if (checkBattleEnd()) {
                    return player.isAlive();
                }
            }
        }
    }


    public void takeTurn(Combatant combatant) {
        ActionContext ctx = new ActionContext(combatant, allCombatants, null, ui);
        Action chosen = combatant.chooseAction(ctx);
        combatant.getActions().decrementCooldowns();
        if (chosen == null) return;
        chosen.execute(ctx);
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