package com.gmail.plundermc.MovingCreatures;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFallingSand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EntityFallingBlock;

public class FloatingBlock {

	private FloatingCart cart;
	private EntityFallingBlock block;

	@SuppressWarnings("deprecation")
	public FloatingBlock(Location l, ItemStack item) {
		FallingBlock fb = l.getWorld().spawnFallingBlock(l, item.getType(), item.getData().getData());
		EntityFallingBlock efb = ((CraftFallingSand) fb).getHandle();
		efb.ticksLived = Integer.MIN_VALUE;
		FloatingCart cart = new FloatingCart(l);
		cart.passenger = efb;
		efb.vehicle = cart;
		cart.setFlyingVelocityMod(new Vector());
		cart.setDerailedVelocityMod(new Vector());
		this.cart = cart;
		this.block = efb;
	}

	public boolean isAlive() {
		return this.cart.isAlive() && this.block.isAlive();
	}

	public void setVelocity(Vector v) {
		this.cart.getBukkitEntity().setVelocity(v);
	}

	public void teleport(Location l) {
		this.cart.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
	}

	public EntityFallingBlock getBlock() {
		return block;
	}

	public FloatingCart getCart() {
		return this.cart;
	}
}
