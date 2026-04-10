package control;

import java.util.List;

import boundary.GameUI;
import control.strategy.SpeedBasedTurnOrder;
import entity.combatant.player.Player;
import entity.combatant.player.Warrior;
import entity.combatant.player.Wizard;
import entity.item.Item;
import entity.level.Difficulty;
import entity.level.Level;

// SRP: orchestrates game setup and loop
public class GameManager {

    private final GameUI ui = new GameUI();

    public void startGame() {
        ui.displayWelcome();
        int playerType = -1;
        List<Item> items = null;
        Difficulty difficulty = null;
        boolean replayWithSame = false;

        while (true) {
            if (!replayWithSame) {
                playerType = ui.selectPlayerType();
                items = ui.selectItems();
                difficulty = ui.selectDifficulty();
            }

            Player player = createPlayer(playerType, items);
            Level level = new Level(difficulty);

            BattleEngine engine = new BattleEngine(ui, new SpeedBasedTurnOrder(), level, player);
            boolean won = engine.startBattle();

            ui.displayBattleEnd(won, player, engine.getRound());

            int choice = ui.askReplay();
            if (choice == 1) {
                replayWithSame = true;
            } else if (choice == 2) {
                replayWithSame = false;
            } else {
                System.out.println("Thanks for playing! Goodbye.");
                break;
            }
        }
    }

    private Player createPlayer(int type, List<Item> items) {
        if (type == 1) return new Warrior(items);
        else return new Wizard(items);
    }
}