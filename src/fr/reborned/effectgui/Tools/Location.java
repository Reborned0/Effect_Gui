package fr.reborned.effectgui.Tools;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class Location extends org.bukkit.Location {
    public Location(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }
}
