package com.gmail.plundermc.MovingCreatures.Creatures;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Schematic {

	public static boolean isSchematic(File f) {
		if (f.exists() && f.isFile() && f.getName().contains(".")) {
			String[] fsplit = f.getName().split("\\.");
			if (fsplit[fsplit.length - 1].equalsIgnoreCase("schematic")) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static Schematic read(File f) throws Exception {
		if (isSchematic(f)) {
			FileInputStream fs = new FileInputStream(f);
			NBTTagCompound nbtc = NBTCompressedStreamTools.a(fs);
			short length = nbtc.getShort("Length");
			short width = nbtc.getShort("Width");
			short height = nbtc.getShort("Height");
			byte[] blocks = nbtc.getByteArray("Blocks");
			byte[] data = nbtc.getByteArray("Data");
			HashSet<SBlock> blockset = new HashSet<>();
			for (int x = 0; x < width; x++)
				for (int z = 0; z < length; z++)
					for (int y = 0; y < height; y++)
						if (blocks[(y * length + z) * width + x] != 0)
							blockset.add(new SBlock(Material.getMaterial(blocks[(y * length + z) * width + x]), data[(y * length + z) * width + x], new Vector(x, y, z)));
			fs.close();
			return new Schematic(length, width, height, blockset);
		} else
			throw new Exception("File is not a schematic");
	}

	private short length;
	private short width;
	private short height;
	private HashSet<SBlock> blocks;

	public Schematic(short l, short w, short h, HashSet<SBlock> b) {
		this.length = l;
		this.width = w;
		this.height = h;
		this.blocks = b;
	}

	public int getLength() {
		return this.length;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public HashSet<SBlock> getBlocks() {
		return this.blocks;
	}

	static class SBlock {
		private Material material;
		private byte data;
		private Vector offset;

		public SBlock(Material mat, byte data, Vector offset) {
			this.material = mat;
			this.data = data;
			this.offset = offset;
		}

		public Material getMaterail() {
			return this.material;
		}

		public byte getData() {
			return this.data;
		}

		public Vector getOffset() {
			return this.offset;
		}
	}
}