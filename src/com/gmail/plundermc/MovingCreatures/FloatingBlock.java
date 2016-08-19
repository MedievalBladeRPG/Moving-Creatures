package com.gmail.plundermc.MovingCreatures;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFallingSand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;

public class FloatingBlock {

	private ArmorStand armorstand;
	private EntityFallingBlock block;

	private float pitch, yaw;

	@SuppressWarnings("deprecation")
	public FloatingBlock(Location l, ItemStack item) {
		FallingBlock fb = l.getWorld().spawnFallingBlock(l, item.getType(), item.getData().getData());
		EntityFallingBlock efb = ((CraftFallingSand) fb).getHandle();
		efb.ticksLived = Integer.MIN_VALUE;
		ArmorStand a = l.getWorld().spawn(l, ArmorStand.class);
		a.setGravity(false);
		a.setVisible(false);
		a.setPassenger(fb);
		this.block = efb;
		this.armorstand = a;
		this.pitch = l.getPitch();
		this.yaw = l.getYaw();
	}

	public void move(double x, double y, double z) {
		EntityArmorStand eas = ((CraftArmorStand) armorstand).getHandle();
		eas.locX += x;
		eas.locY += y;
		eas.locZ += z;
		eas.setLocation(eas.locX, eas.locY, eas.locZ, pitch, yaw);
	}

	public void move(Vector v) {
		move(v.getX(), v.getY(), v.getZ());
	}

	public void teleport(Location l) {
		move(l.subtract(armorstand.getLocation()).toVector());
	}

	public void setYaw(float f) {
		this.yaw = f;
		this.block.yaw = f;
	}

	public void setPitch(float p) {
		this.pitch = p;
		this.block.pitch = p;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public boolean isAlive() {
		return !this.armorstand.isDead() && this.block.isAlive();
	}
}
