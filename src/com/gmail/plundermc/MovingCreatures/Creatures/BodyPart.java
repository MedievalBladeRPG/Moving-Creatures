package com.gmail.plundermc.MovingCreatures.Creatures;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.gmail.plundermc.MovingCreatures.FloatingBlock;
import com.gmail.plundermc.MovingCreatures.Main;

public class BodyPart {
	private Set<FloatingBlock> floatingBlocks;
	private Set<BodyPart> children;
	private double health;
	private Location location;

	public BodyPart(Location l, double health) {
		this.health = health;
		this.location = l.clone();
		this.children = new HashSet<>();
		this.floatingBlocks = new HashSet<>();
	}

	public void setPitchYaw(float p, float y) {
		for (FloatingBlock fb : floatingBlocks) {
			Location l = fb.getLocation().clone();
			double pi = p - this.location.getPitch(), ya = y - this.location.getYaw();
			Main.rotate(l, location, Math.toRadians(pi), Math.toRadians(ya), 0.0);
			fb.teleport(l);
		}
		this.location.setPitch(p);
		this.location.setYaw(y);
	}

	public void rotateAroundLocation(Location l, Vector r) {
		for (FloatingBlock fb : floatingBlocks) {
			Location lo = fb.getLocation().clone();
			Main.rotate(lo, l, r);
			fb.teleport(lo);
		}
		for (BodyPart child : children) {
			child.rotateAroundLocation(l, r);
		}
		Main.rotate(location, l, r);
	}

	public Set<BodyPart> getChildren() {
		return this.children;
	}

	public void addChild(BodyPart b) {
		if (!children.contains(b))
			children.add(b);
	}

	public void removeChild(BodyPart b) {
		if (children.contains(b))
			children.remove(b);
	}

	public void setHealth(double h) {
		this.health = h;
	}

	public void damage(double d) {
		this.health -= d;
	}

	public boolean isDead() {
		return health <= 0.0;
	}

	public boolean isAlive() {
		return !isDead();
	}
}
