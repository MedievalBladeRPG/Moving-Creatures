package com.gmail.plundermc.MovingCreatures;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFallingSand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.gmail.plundermc.MovingCreatures.Creatures.BodyPart;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;

public class FloatingBlock {

	private ArmorStand armorstand;
	private EntityFallingBlock block;
	private ItemStack stack;
	private BodyPart parent;
	private JavaPlugin plugin;
	private float pitch, yaw;

	@SuppressWarnings("deprecation")
	public FloatingBlock(Location l, ItemStack item) {
		FallingBlock fb = l.getWorld().spawnFallingBlock(l, item.getType(), item.getData().getData());
		EntityFallingBlock efb = ((CraftFallingSand) fb).getHandle();
		efb.ticksLived = Integer.MIN_VALUE;
		ArmorStand a = l.getWorld().spawn(l, ArmorStand.class);
		a.setGravity(false);
		a.setVisible(false);
		this.armorstand.setMetadata("movingcreature", new FixedMetadataValue(plugin, ""));
		a.setPassenger(fb);
		this.block = efb;
		this.armorstand = a;
		this.pitch = l.getPitch();
		this.yaw = l.getYaw();
		this.stack = item;
		this.plugin = JavaPlugin.getPlugin(Main.class);
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

	public Location getLocation() {
		return this.armorstand.getLocation();
	}

	@SuppressWarnings("deprecation")
	public void changeBlockType(ItemStack item) {
		this.stack = item;
		if (!item.getType().isBlock())
			return;
		FallingBlock fb = getLocation().getWorld().spawnFallingBlock(getLocation(), item.getType(), item.getData().getData());
		EntityFallingBlock efb = ((CraftFallingSand) fb).getHandle();
		efb.ticksLived = Integer.MIN_VALUE;
		Entity e = armorstand.getPassenger();
		armorstand.eject();
		if (e != null)
			e.remove();
		armorstand.setPassenger(fb);
	}

	public ItemStack getBlockType() {
		return this.stack;
	}

	public void setParent(BodyPart b) {
		this.parent = b;
		this.armorstand.setMetadata("bodypart", new FixedMetadataValue(plugin, parent));
	}
}
