package dev.doctor4t.trainmurdermystery.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;

public class SnowflakeParticle extends SpriteBillboardParticle {
	private final float yRand;
	private final float zRand;

	public SnowflakeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);

		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;

		this.zRand = world.random.nextFloat() * 2 - 1;
		this.yRand = world.random.nextFloat() * 2 - 1;

		this.maxAge = 30 + world.random.nextInt(10);
		this.scale = .1f + world.random.nextFloat() * .1f;
		this.alpha = 0f;

		this.setSprite(spriteProvider.getSprite(world.random));
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public void tick() {
		super.tick();
		this.alpha+=0.01f;

		float v = .2f;
		this.velocityZ = Math.sin(this.zRand + this.age / 2f + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)) * v;
		this.velocityY = -.1f + Math.sin(this.yRand + this.age / 2f + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)) * v;

		if (this.onGround || this.velocityX == 0) {
			this.markDead();
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new SnowflakeParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
