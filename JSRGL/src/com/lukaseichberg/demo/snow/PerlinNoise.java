package com.lukaseichberg.demo.snow;

import java.util.Random;

import com.lukaseichberg.maths.Maths;

public class PerlinNoise {
	
	private Random r;
	private int xmul, ymul, zmul;
	
	public PerlinNoise(long seed) {
		r = new Random();
		r.setSeed(seed);
		xmul = r.nextInt();
		ymul = r.nextInt();
		zmul = r.nextInt();
	}
	
	public float getHeight(int x, int y, int scale) {
		return getHeightScale(x, y, scale) * 0.5f + getHeightScale(x, y, scale / 2) * 0.25f + getHeightScale(x, y, scale / 4) * 0.125f + getHeightScale(x, y, scale / 8) * 0.0625f;
	}
	
	public float getBiome(int x, int y) {
		return getHeightScale(x, y, 4);
	}
	
	public float getHeightScale(int x, int y, int scale) {
		int xx = Math.floorDiv(x, scale);
		int yy = Math.floorDiv(y, scale);
		float valueX = (float) Math.floorMod(x, scale) / scale;
		float valueY = (float) Math.floorMod(y, scale) / scale;
		r.setSeed((xx * xmul) + (yy * ymul) * zmul);
		float p00 = r.nextFloat();
		r.setSeed((xx * xmul) + ((yy + 1) * ymul) * zmul);
		float p01 = r.nextFloat();
		r.setSeed(((xx + 1) * xmul) + (yy * ymul) * zmul);
		float p10 = r.nextFloat();
		r.setSeed(((xx + 1) * xmul) + ((yy + 1) * ymul) * zmul);
		float p11 = r.nextFloat();

		float p0 = Maths.cosint(p00, p01, valueY);
		float p1 = Maths.cosint(p10, p11, valueY);
		
		return Maths.cosint(p0, p1, valueX);
	}

	public float getFloat(int x, int y) {
		r.setSeed((x * xmul) + (y * ymul) * zmul);
		return r.nextFloat();
	}

}
