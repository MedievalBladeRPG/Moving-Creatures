package com.gmail.plundermc.MovingCreatures;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFallingSand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EntityFallingBlock;

public class FloatingBlock {

	private FloatingBoat boat;
	private EntityFallingBlock block;

	@SuppressWarnings("deprecation")
	public FloatingBlock(Location l, ItemStack item) {
		FallingBlock fb = l.getWorld().spawnFallingBlock(l, item.getType(), item.getData().getData());
		EntityFallingBlock efb = ((CraftFallingSand) fb).getHandle();
		efb.ticksLived = Integer.MIN_VALUE;
		FloatingBoat boat = new FloatingBoat(l);
		boat.passenger = efb;
		efb.vehicle = boat;
		this.boat = boat;
		this.block = efb;
	}

	public boolean isAlive() {
		return this.boat.isAlive() && this.block.isAlive();
	}

	public void setVelocity(Vector v) {
		this.boat.getBukkitEntity().setVelocity(v);
	}

	public void teleport(Location l) {
		this.boat.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
	}

	public EntityFallingBlock getBlock() {
		return block;
	}

	public FloatingBoat getboat() {
		return this.boat;
	}
}
