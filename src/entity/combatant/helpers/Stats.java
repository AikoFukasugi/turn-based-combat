package entity.combatant.helpers;

import java.lang.reflect.Field;

public class Stats {
    protected int maxHp = 0;
    protected int attack = 0;
    protected int defense = 0;
    protected int speed = 0;

    public Stats(int maxHp, int attack, int defense, int speed) {
        this.maxHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }


    public int get(StatField attr) {
        try {
            Field field = Stats.class.getDeclaredField(attr.toString());
            field.setAccessible(true);
            return (int) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Stat field access failed", e);
        }
    }

    public void set(StatField attr, int value) {
        try {
            Field field = Stats.class.getDeclaredField(attr.toString());
            field.setAccessible(true);
            field.setInt(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Stat field access failed", e);
        }
    }

    public void add(StatField attr, int amount) {
        int value = get(attr);
        set(attr, value + amount);
    }

    public void subtract(StatField attr, int amount) {
        int value = get(attr);
        set(attr, value - amount);
    }

    public Stats add(Stats other) {
        Stats result = new Stats(maxHp, attack, defense, speed);
        for (Field field : Stats.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                try {
                    field.setInt(result, field.getInt(this) + field.getInt(other));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to add stat: " + field.getName(), e);
                }
            }
        }
        return result;
    }

    public void print() {
        for (Field field : Stats.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                System.out.println(field.getName() + ": " + field.getInt(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to get stat: " + field.getName(), e);
            }
        }
    }

}
