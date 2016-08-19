package com.gmail.plundermc.MovingCreatures;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;

public class FloatingBoat extends EntityBoat {

	private boolean a;
	private int c;
	private double d;
	private double e;
	private double f;
	private double g;
	private double h;

	public FloatingBoat(Location l) {
		this(((CraftWorld) l.getWorld()).getHandle(), l.getX(), l.getY(), l.getZ());
	}

	public FloatingBoat(World world, double d0, double d1, double d2) {
		super(world, d0, d1, d2);
		this.setInvisible(true);
	}

	@Override
	public void collide(Entity e) {
		return;
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		return false;
	}

	@Override
	public void t_() {
		super.t_();
		if (l() > 0) {
			a(l() - 1);
		}
		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;
		if ((this.world.isClientSide) && (this.a)) {
			if (this.c > 0) {
				double d4 = this.locX + (this.d - this.locX) / this.c;
				double d5 = this.locY + (this.e - this.locY) / this.c;
				double d10 = this.locZ + (this.f - this.locZ) / this.c;
				double d11 = MathHelper.g(this.g - this.yaw);
				this.yaw = ((float) (this.yaw + d11 / this.c));
				this.pitch = ((float) (this.pitch + (this.h - this.pitch) / this.c));
				this.c -= 1;
				setPosition(d4, d5, d10);
				setYawPitch(this.yaw, this.pitch);
			} else {
				double d4 = this.locX + this.motX;
				double d5 = this.locY + this.motY;
				double d10 = this.locZ + this.motZ;
				setPosition(d4, d5, d10);
				if (this.onGround) {
					this.motX *= 0.5D;
					this.motY *= 0.5D;
					this.motZ *= 0.5D;
				}
				this.motX *= 0.9900000095367432D;
				this.motY *= 0.949999988079071D;
				this.motZ *= 0.9900000095367432D;
			}
		} else {
			move(this.motX, this.motY, this.motZ);

			this.pitch = 0.0F;
			double d5 = this.yaw;
			double d10 = this.lastX - this.locX;
			double d11 = this.lastZ - this.locZ;
			if (d10 * d10 + d11 * d11 > 0.001D) {
				d5 = (float) (MathHelper.b(d11, d10) * 180.0D / 3.141592653589793D);
			}
			double d12 = MathHelper.g(d5 - this.yaw);
			if (d12 > 20.0D) {
				d12 = 20.0D;
			}
			if (d12 < -20.0D) {
				d12 = -20.0D;
			}
			this.yaw = ((float) (this.yaw + d12));
			setYawPitch(this.yaw, this.pitch);

			if (!this.world.isClientSide) {
				if ((this.passenger != null) && (this.passenger.dead)) {
					this.passenger.vehicle = null;
					this.passenger = null;
				}
			}
		}
	}
}
