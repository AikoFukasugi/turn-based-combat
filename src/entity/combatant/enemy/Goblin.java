package entity.combatant.enemy;

public class Goblin extends Enemy {
    private static int count = 0;

    public Goblin() {
        super("Goblin-" + (char)('A' + count++), 55, 35, 15, 25); 
    }
}