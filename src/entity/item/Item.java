package entity.item;

import entity.action.ActionContext;

public abstract class Item {
    protected String name;
    protected boolean used = false;

    public String getName() { return name; }
    public boolean isUsed() { return used; }

    public abstract void use(ActionContext ctx);
}