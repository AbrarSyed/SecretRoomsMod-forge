package mods.SecretRoomsMod.common.fake;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FakeProvider extends WorldProvider
{
	WorldProvider	provider;

	public FakeProvider(WorldProvider provider)
	{
		this.provider = provider;
	}

	@Override
	public String getDimensionName()
	{
		return "SRM_FAKE_" + provider.getDimensionName();
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		// no
	}

	@Override
	protected void registerWorldChunkManager()
	{
		// no
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return provider.createChunkGenerator();
	}

	@Override
	public boolean canCoordinateBeSpawn(int par1, int par2)
	{
		return provider.canCoordinateBeSpawn(par1, par2);
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		return provider.calculateCelestialAngle(par1, par3);
	}

	@Override
	public int getMoonPhase(long par1)
	{
		return provider.getMoonPhase(par1);
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return provider.isSurfaceWorld();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float par1, float par2)
	{
		return provider.calcSunriseSunsetColors(par1, par2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float par1, float par2)
	{
		return provider.getFogColor(par1, par2);
	}

	@Override
	public boolean canRespawnHere()
	{
		return provider.canRespawnHere();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight()
	{
		return provider.getCloudHeight();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored()
	{
		return provider.isSkyColored();
	}

	@Override
	public ChunkCoordinates getEntrancePortalLocation()
	{
		return provider.getEntrancePortalLocation();
	}

	@Override
	public int getAverageGroundLevel()
	{
		return provider.getAverageGroundLevel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean getWorldHasVoidParticles()
	{
		return provider.getWorldHasVoidParticles();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getVoidFogYFactor()
	{
		return provider.getVoidFogYFactor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int par1, int par2)
	{
		return provider.doesXZShowFog(par1, par2);
	}

	@Override
	public void setDimension(int dim)
	{
		// no
		provider.setDimension(dim);
	}

	@Override
	public String getSaveFolder()
	{
		return provider.getSaveFolder();
	}

	@Override
	public String getWelcomeMessage()
	{
		return provider.getWelcomeMessage();
	}

	@Override
	public String getDepartMessage()
	{
		return provider.getDepartMessage();
	}

	@Override
	public double getMovementFactor()
	{
		return provider.getMovementFactor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer()
	{
		return provider.getSkyRenderer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSkyRenderer(IRenderHandler skyRenderer)
	{
		// no
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getCloudRenderer()
	{
		return provider.getCloudRenderer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setCloudRenderer(IRenderHandler renderer)
	{
		// no
	}

	@Override
	public ChunkCoordinates getRandomizedSpawnPoint()
	{
		return provider.getRandomizedSpawnPoint();
	}

	@Override
	public boolean shouldMapSpin(String entity, double x, double y, double z)
	{
		return provider.shouldMapSpin(entity, x, y, z);
	}

	@Override
	public int getRespawnDimension(EntityPlayerMP player)
	{
		return provider.getRespawnDimension(player);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z)
	{
		return provider.getBiomeGenForCoords(x, z);
	}

	@Override
	public boolean isDaytime()
	{
		return provider.isDaytime();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return provider.getSkyColor(cameraEntity, partialTicks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 drawClouds(float partialTicks)
	{
		return provider.drawClouds(partialTicks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		return provider.getStarBrightness(par1);
	}

	@Override
	public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
	{
		// nope
	}

	@Override
	public void calculateInitialWeather()
	{
		// nothing
	}

	@Override
	public void updateWeather()
	{
		// no
	}

	@Override
	public void toggleRain()
	{
		// no.
	}

	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
	{
		return provider.canBlockFreeze(x, y, z, byWater);
	}

	@Override
	public boolean canSnowAt(int x, int y, int z)
	{
		return provider.canSnowAt(x, y, z);
	}

	@Override
	public void setWorldTime(long time)
	{
		// no touch
	}

	@Override
	public long getSeed()
	{
		return provider.getSeed();
	}

	@Override
	public long getWorldTime()
	{
		return provider.getWorldTime();
	}

	@Override
	public ChunkCoordinates getSpawnPoint()
	{
		return provider.getSpawnPoint();
	}

	@Override
	public void setSpawnPoint(int x, int y, int z)
	{
		// no touch
	}

	@Override
	public boolean canMineBlock(EntityPlayer player, int x, int y, int z)
	{
		return provider.canMineBlock(player, x, y, z);
	}

	@Override
	public boolean isBlockHighHumidity(int x, int y, int z)
	{
		return provider.isBlockHighHumidity(x, y, z);
	}

	@Override
	public int getHeight()
	{
		return provider.getHeight();
	}

	@Override
	public int getActualHeight()
	{
		return provider.getActualHeight();
	}

	@Override
	public double getHorizon()
	{
		return provider.getHorizon();
	}

	@Override
	public void resetRainAndThunder()
	{
		// nothing
	}

	@Override
	public boolean canDoLightning(Chunk chunk)
	{
		return provider.canDoLightning(chunk);
	}

	@Override
	public boolean canDoRainSnowIce(Chunk chunk)
	{
		return provider.canDoRainSnowIce(chunk);
	}

}
