package com.gmail.plundermc.MovingCreatures.Creatures;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.sainttx.holograms.HologramPlugin;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramManager;

public class Creature {

	private static HologramManager hm;
	static {
		hm = JavaPlugin.getPlugin(HologramPlugin.class).getHologramManager();
	}

	protected Set<BodyPart> bodyparts;
	protected Location location;
	protected Hologram nametag;

	public Creature(Location l, String name, Vector nametagOffset) {
		this.location = l;
		nametag = new Hologram(name, l.clone().add(nametagOffset));
		hm.addActiveHologram(nametag);
	}

	public Creature(Location l, String name) {
		this(l, name, new Vector(0, 5, 0));
	}

	public Location getLocation() {
		return this.location;
	}

	public void teleport(Location l) {
		this.location = l;
		checkRotation();
	}

	public Hologram getNametag() {
		return this.nametag;
	}

	public Set<BodyPart> getBodyParts() {
		return this.bodyparts;
	}

	public void move(double x, double y, double z) {
		this.location.add(x, y, z);
	}

	public void move(Vector v) {
		this.location.add(v);
	}

	public void rotateYaw(double d) {
		this.location.setYaw((this.location.getYaw() + (float) d) % 360.0F);
		checkRotation();
	}

	public void setYaw(double d) {
		this.location.setYaw((float) d % 360.0F);
		checkRotation();
	}

	public void rotatePitch(double d) {
		this.location.setPitch((this.location.getPitch() + (float) d));
		checkRotation();
	}

	public void setPitch(double d) {
		this.location.setPitch((float) d);
		checkRotation();
	}

	public void checkRotation() {
		for (BodyPart b : bodyparts) {
			b.setPitchYaw(this.location.getPitch(), this.location.getYaw());
		}
	}
}
