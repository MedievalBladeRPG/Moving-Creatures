package com.gmail.plundermc.MovingCreatures;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;

import net.minecraft.server.v1_8_R3.BlockMinecartTrackAbstract;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockPoweredRail;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartRideable;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;

public class FloatingCart extends EntityMinecartRideable {

	private boolean a;
	private int d;
	private double e;
	private double f;
	private double g;
	private double h;
	private double i;
	public boolean slowWhenEmpty = true;
	public double maxSpeed = 0.4D;

	public FloatingCart(org.bukkit.Location l) {
		this(((CraftWorld) l.getWorld()).getHandle(), l.getX(), l.getY(), l.getZ());
	}

	public FloatingCart(World w, double x, double y, double z) {
		super(w, x, y, z);
	}

	// boolean e - Whether paramEntityHuman can enter the minecart
	@Override
	public void t_() {
		double prevX = this.locX;
		double prevY = this.locY;
		double prevZ = this.locZ;
		float prevYaw = this.yaw;
		float prevPitch = this.pitch;
		if (getType() > 0) {
			j(getType() - 1);
		}
		if (getDamage() > 0.0F) {
			setDamage(getDamage() - 1.0F);
		}
		if (this.locY < -64.0D) {
			O();
		}
		if ((!this.world.isClientSide) && ((this.world instanceof WorldServer))) {
			this.world.methodProfiler.a("portal");

			int i = L();
			if (this.ak) {
				if ((this.vehicle == null) && (this.al++ >= i)) {
					this.al = i;
					this.portalCooldown = aq();
					byte b0;
					if (this.world.worldProvider.getDimension() == -1) {
						b0 = 0;
					} else {
						b0 = -1;
					}
					c(b0);
				}
				this.ak = false;
			} else {
				if (this.al > 0) {
					this.al -= 4;
				}
				if (this.al < 0) {
					this.al = 0;
				}
			}
			if (this.portalCooldown > 0) {
				this.portalCooldown -= 1;
			}
			this.world.methodProfiler.b();
		}
		if (this.world.isClientSide) {
			if (this.d > 0) {
				double d0 = this.locX + (this.e - this.locX) / this.d;
				double d1 = this.locY + (this.f - this.locY) / this.d;
				double d2 = this.locZ + (this.g - this.locZ) / this.d;
				double d3 = MathHelper.g(this.h - this.yaw);

				this.yaw = ((float) (this.yaw + d3 / this.d));
				this.pitch = ((float) (this.pitch + (this.i - this.pitch) / this.d));
				this.d -= 1;
				setPosition(d0, d1, d2);
				setYawPitch(this.yaw, this.pitch);
			} else {
				setPosition(this.locX, this.locY, this.locZ);
				setYawPitch(this.yaw, this.pitch);
			}
		} else {
			this.lastX = this.locX;
			this.lastY = this.locY;
			this.lastZ = this.locZ;
			int j = MathHelper.floor(this.locX);

			int i = MathHelper.floor(this.locY);
			int k = MathHelper.floor(this.locZ);
			if (BlockMinecartTrackAbstract.e(this.world, new BlockPosition(j, i - 1, k))) {
				i--;
			}
			BlockPosition blockposition = new BlockPosition(j, i, k);
			IBlockData iblockdata = this.world.getType(blockposition);
			if (BlockMinecartTrackAbstract.d(iblockdata)) {
				a(blockposition, iblockdata);
				if (iblockdata.getBlock() == Blocks.ACTIVATOR_RAIL) {
					a(j, i, k, iblockdata.get(BlockPoweredRail.POWERED).booleanValue());
				}
			} else {
				n();
			}
			// checkBlockCollisions();
			this.pitch = 0.0F;

			// sets yaw of minecart to current direction you are moving
			// MathHelper.b must be atan2
			double d4 = this.lastX - this.locX;
			double d5 = this.lastZ - this.locZ;
			if (d4 * d4 + d5 * d5 > 0.001D) {
				this.yaw = ((float) (MathHelper.b(d5, d4) * 180.0D / 3.141592653589793D));
				// This.a indicates whether the minecart has collided with
				// something?
				if (this.a) {
					this.yaw += 180.0F;
				}
			}
			double d6 = MathHelper.g(this.yaw - this.lastYaw);
			if ((d6 < -170.0D) || (d6 >= 170.0D)) {
				this.yaw += 180.0F;
				this.a = (!this.a);
			}
			setYawPitch(this.yaw, this.pitch);

			org.bukkit.World bworld = this.world.getWorld();
			Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
			Location to = new Location(bworld, this.locX, this.locY, this.locZ, this.yaw, this.pitch);
			Vehicle vehicle = (Vehicle) getBukkitEntity();

			this.world.getServer().getPluginManager().callEvent(new VehicleUpdateEvent(vehicle));
			if (!from.equals(to)) {
				this.world.getServer().getPluginManager().callEvent(new VehicleMoveEvent(vehicle, from, to));
			}
			Iterator<Entity> iterator = this.world.getEntities(this, getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D)).iterator();
			while (iterator.hasNext()) {
				Entity entity = iterator.next();
				if ((entity != this.passenger) && (entity.ae()) && ((entity instanceof EntityMinecartAbstract))) {
					entity.collide(this);
				}
			}
			if ((this.passenger != null) && (this.passenger.dead)) {
				if (this.passenger.vehicle == this) {
					this.passenger.vehicle = null;
				}
				this.passenger = null;
			}
			W();
		}
	}
}
