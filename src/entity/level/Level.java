package entity.level;

import java.util.ArrayList;
import java.util.List;

import entity.combatant.enemy.Enemy;
import entity.combatant.enemy.Goblin;
import entity.combatant.enemy.Wolf;

public class Level {
    private final Difficulty difficulty;
    private final List<Enemy> initialSpawn;
    private List<Enemy> backupSpawn;
    private boolean backupTriggered = false;

    public Level(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.initialSpawn = buildInitial();
        this.backupSpawn = buildBackup();
    }

    private List<Enemy> buildInitial() {
        List<Enemy> list = new ArrayList<>();
        switch (difficulty) {
            case EASY:
                list.add(new Goblin()); list.add(new Goblin()); list.add(new Goblin());
                break;
            case MEDIUM:
                list.add(new Goblin()); list.add(new Wolf());
                break;
            case HARD:
                list.add(new Goblin()); list.add(new Goblin());
                break;
        }
        return list;
    }

    private List<Enemy> buildBackup() {
        List<Enemy> list = new ArrayList<>();
        switch (difficulty) {
            case MEDIUM:
                list.add(new Wolf()); list.add(new Wolf());
                break;
            case HARD:
                list.add(new Goblin()); list.add(new Wolf()); list.add(new Wolf());
                break;
            default: break;
        }
        return list;
    }

    public List<Enemy> getInitialEnemies() { return new ArrayList<>(initialSpawn); }

    public boolean isBackupAvailable() {
        return !backupTriggered && !backupSpawn.isEmpty();
    }

    public List<Enemy> triggerBackup() {
        backupTriggered = true;
        return new ArrayList<>(backupSpawn);
    }
}