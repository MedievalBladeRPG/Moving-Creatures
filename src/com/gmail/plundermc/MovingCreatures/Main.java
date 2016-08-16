package com.gmail.plundermc.MovingCreatures;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		clearOwnedEntities();
		new Events(this);
	}

	@Override
	public void onDisable() {
	}

	public void clearOwnedEntities() {
		for (World w : getServer().getWorlds())
			for (Entity e : w.getEntities())
				if (e.hasMetadata("movingcreature"))
					e.remove();
	}

	public static void rotate(Location l, Location pivot, double a, double b, double c) {
		double ep = 0.005D;
		boolean xb = Math.abs(a) >= ep;
		boolean yb = Math.abs(b) >= ep;
		boolean zb = Math.abs(c) >= ep;

		double ca = Math.cos(a);
		double sa = Math.sin(a);
		double cb = Math.cos(b);
		double sb = Math.sin(b);
		double cc = Math.cos(c);
		double sc = Math.sin(c);
		double x = l.getX() - pivot.getX();
		double y = l.getY() - pivot.getY();
		double z = l.getZ() - pivot.getZ();
		if ((xb) && (yb) && (zb)) {
			l.setX(cb * (x * cc + y * sc) - z * sb + pivot.getX());
			l.setY(ca * (y * cc - x * sc) + sa * (sb * (x * cc + y * sc) + z * cb) + pivot.getY());
			l.setZ(ca * (sb * (x * cc + y * sc) + z * cb) - sa * (y * cc - x * sc) + pivot.getZ());
		} else if ((xb) && (yb)) {
			l.setX(x * cb - z * sb + pivot.getX());
			l.setY(y * ca + sa * (x * sb + z * cb) + pivot.getY());
			l.setZ(ca * (x * sb + z * cb) - y * sa + pivot.getZ());
		} else if ((yb) && (zb)) {
			l.setX(cb * (x * cc + y * sc) - z * sb + pivot.getX());
			l.setY(y * cc - x * sc + pivot.getY());
			l.setZ(sb * (x * cc + y * sc) - z * cb + pivot.getZ());
		} else if ((xb) && (zb)) {
			l.setX(x * cc + y * sc + pivot.getX());
			l.setY(z * sa + ca * (y * cc - x * sc) + pivot.getY());
			l.setZ(z * ca - sa * (y * cc - x * sc) + pivot.getZ());
		} else if (xb) {
			l.setY(y * ca + z * sa + pivot.getY());
			l.setZ(z * ca - y * sa + pivot.getZ());
		} else if (yb) {
			l.setX(x * cb - z * sb + pivot.getX());
			l.setZ(x * sb + z * cb + pivot.getZ());
		} else if (zb) {
			l.setX(y * sc + x * cc + pivot.getX());
			l.setY(y * cc - x * sc + pivot.getY());
		} else {
		}
	}
}
