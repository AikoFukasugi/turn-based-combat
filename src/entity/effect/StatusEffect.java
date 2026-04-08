package entity.effect;

import boundary.GameUI;

public abstract class StatusEffect {
    protected String name;
    protected int duration;
    protected boolean begin = true;
    protected boolean stackable = false;
    protected int value = 0;

    public String getName() { return name; }
    public int getDuration() { return duration; }
    public int getValue() { return value; }
    public boolean isExpired() { return duration <= 0; }
    public boolean isStackable() { return stackable; }
    public void decrementDuration() { duration--; }
    public boolean isBegin() { return begin; }

    public void onApply(String c, GameUI ui) {}
    public void onExpire(String c, GameUI ui) {}
    public String toString() {
        return "[" + name + " " + duration + "]";
    }
}