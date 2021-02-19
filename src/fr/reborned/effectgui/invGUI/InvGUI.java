package fr.reborned.effectgui.invGUI;

import fr.reborned.effectgui.Tools.FastInv;

public abstract class InvGUI {
    public abstract void init();

    public abstract void openInv();

    public abstract InvGUI get();

    public abstract FastInv getFastInventory();
}
