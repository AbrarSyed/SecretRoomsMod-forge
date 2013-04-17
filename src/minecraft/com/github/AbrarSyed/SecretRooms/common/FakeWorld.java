package com.github.AbrarSyed.SecretRooms.common;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.logging.ILogAgent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.collect.ImmutableSetMultimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FakeWorld extends World implements IBlockAccess
{
	private final World									world;
	private final HashMap<ChunkPosition, BlockHolder>	overrideMap;

	private FakeWorld(World world, WorldSettings settings)
	{
		super(null, "SRM_FAKE_" + world.getWorldInfo().getWorldName(), null, settings, null, null);
		this.world = world;
		overrideMap = new HashMap<ChunkPosition, BlockHolder>();
		worldInfo = world.getWorldInfo();
	}

	public static FakeWorld getFakeWorldFor(World world)
	{
		WorldSettings settings = new WorldSettings(world.getWorldInfo());
		return new FakeWorld(world, settings);
	}

	// actual stuff...

	public void addOverrideBlock(int x, int y, int z, BlockHolder holder)
	{
		ChunkPosition position = new ChunkPosition(x, y, z);
		overrideMap.put(position, holder);
	}

	public void removeOverrideBlock(int x, int y, int z)
	{
		ChunkPosition position = new ChunkPosition(x, y, z);
		overrideMap.remove(position);
	}

	// overrides...

	@Override
	public int getBlockId(int x, int y, int z)
	{
		ChunkPosition pos = new ChunkPosition(x, y, z);
		if (overrideMap.containsKey(pos))
			return overrideMap.get(pos).blockID;
		else
			return world.getBlockId(x, y, z);
	}

	@Override
	public TileEntity getBlockTileEntity(int x, int y, int z)
	{
		ChunkPosition pos = new ChunkPosition(x, y, z);
		if (overrideMap.containsKey(pos))
			return overrideMap.get(pos).getTileEntity(world, x, y, z);
		else
			return world.getBlockTileEntity(x, y, z);
	}

	@Override
	public int getBlockMetadata(int x, int y, int z)
	{
		ChunkPosition pos = new ChunkPosition(x, y, z);
		if (overrideMap.containsKey(pos))
			return overrideMap.get(pos).metadata;
		else
			return world.getBlockMetadata(x, y, z);
	}

	// SUPER necessary overrides

	@Override
	protected IChunkProvider createChunkProvider()
	{
		// hope noone ever calls this....
		return null;
	}

	@Override
	public Entity getEntityByID(int i)
	{
		return world.getEntityByID(i);
	}

	// extra necessary overrides

	@Override
	public BiomeGenBase getBiomeGenForCoords(int par1, int par2)
	{

		return world.getBiomeGenForCoords(par1, par2);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoordsBody(int par1, int par2)
	{

		return world.getBiomeGenForCoordsBody(par1, par2);
	}

	@Override
	public WorldChunkManager getWorldChunkManager()
	{

		return world.getWorldChunkManager();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnLocation()
	{

		world.setSpawnLocation();
	}

	@Override
	public int getFirstUncoveredBlock(int par1, int par2)
	{

		return world.getFirstUncoveredBlock(par1, par2);
	}

	@Override
	public boolean isAirBlock(int par1, int par2, int par3)
	{

		return world.isAirBlock(par1, par2, par3);
	}

	@Override
	public boolean blockHasTileEntity(int par1, int par2, int par3)
	{

		return world.blockHasTileEntity(par1, par2, par3);
	}

	@Override
	public int blockGetRenderType(int par1, int par2, int par3)
	{

		return world.blockGetRenderType(par1, par2, par3);
	}

	@Override
	public boolean blockExists(int par1, int par2, int par3)
	{

		return world.blockExists(par1, par2, par3);
	}

	@Override
	public boolean doChunksNearChunkExist(int par1, int par2, int par3, int par4)
	{

		return world.doChunksNearChunkExist(par1, par2, par3, par4);
	}

	@Override
	public boolean checkChunksExist(int par1, int par2, int par3, int par4, int par5, int par6)
	{

		return world.checkChunksExist(par1, par2, par3, par4, par5, par6);
	}

	@Override
	public Chunk getChunkFromBlockCoords(int par1, int par2)
	{

		return world.getChunkFromBlockCoords(par1, par2);
	}

	@Override
	public Chunk getChunkFromChunkCoords(int par1, int par2)
	{

		return world.getChunkFromChunkCoords(par1, par2);
	}

	@Override
	public boolean setBlock(int par1, int par2, int par3, int par4, int par5, int par6)
	{

		return world.setBlock(par1, par2, par3, par4, par5, par6);
	}

	@Override
	public Material getBlockMaterial(int par1, int par2, int par3)
	{

		return world.getBlockMaterial(par1, par2, par3);
	}

	@Override
	public boolean setBlockMetadataWithNotify(int par1, int par2, int par3, int par4, int par5)
	{

		return world.setBlockMetadataWithNotify(par1, par2, par3, par4, par5);
	}

	@Override
	public boolean setBlockToAir(int par1, int par2, int par3)
	{

		return world.setBlockToAir(par1, par2, par3);
	}

	@Override
	public boolean destroyBlock(int par1, int par2, int par3, boolean par4)
	{

		return world.destroyBlock(par1, par2, par3, par4);
	}

	@Override
	public boolean setBlock(int par1, int par2, int par3, int par4)
	{

		return world.setBlock(par1, par2, par3, par4);
	}

	@Override
	public void markBlockForUpdate(int par1, int par2, int par3)
	{

		world.markBlockForUpdate(par1, par2, par3);
	}

	@Override
	public void notifyBlockChange(int par1, int par2, int par3, int par4)
	{

		world.notifyBlockChange(par1, par2, par3, par4);
	}

	@Override
	public void markBlocksDirtyVertical(int par1, int par2, int par3, int par4)
	{

		world.markBlocksDirtyVertical(par1, par2, par3, par4);
	}

	@Override
	public void markBlockRangeForRenderUpdate(int par1, int par2, int par3, int par4, int par5, int par6)
	{

		world.markBlockRangeForRenderUpdate(par1, par2, par3, par4, par5, par6);
	}

	@Override
	public void notifyBlocksOfNeighborChange(int par1, int par2, int par3, int par4)
	{

		world.notifyBlocksOfNeighborChange(par1, par2, par3, par4);
	}

	@Override
	public void notifyBlocksOfNeighborChange(int par1, int par2, int par3, int par4, int par5)
	{

		world.notifyBlocksOfNeighborChange(par1, par2, par3, par4, par5);
	}

	@Override
	public void notifyBlockOfNeighborChange(int par1, int par2, int par3, int par4)
	{

		world.notifyBlockOfNeighborChange(par1, par2, par3, par4);
	}

	@Override
	public boolean isBlockTickScheduled(int par1, int par2, int par3, int par4)
	{

		return world.isBlockTickScheduled(par1, par2, par3, par4);
	}

	@Override
	public boolean canBlockSeeTheSky(int par1, int par2, int par3)
	{

		return world.canBlockSeeTheSky(par1, par2, par3);
	}

	@Override
	public int getFullBlockLightValue(int par1, int par2, int par3)
	{

		return world.getFullBlockLightValue(par1, par2, par3);
	}

	@Override
	public int getBlockLightValue(int par1, int par2, int par3)
	{

		return world.getBlockLightValue(par1, par2, par3);
	}

	@Override
	public int getBlockLightValue_do(int par1, int par2, int par3, boolean par4)
	{

		return world.getBlockLightValue_do(par1, par2, par3, par4);
	}

	@Override
	public int getHeightValue(int par1, int par2)
	{

		return world.getHeightValue(par1, par2);
	}

	@Override
	public int getChunkHeightMapMinimum(int par1, int par2)
	{

		return world.getChunkHeightMapMinimum(par1, par2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
	{

		return world.getSkyBlockTypeBrightness(par1EnumSkyBlock, par2, par3, par4);
	}

	@Override
	public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
	{

		return world.getSavedLightValue(par1EnumSkyBlock, par2, par3, par4);
	}

	@Override
	public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5)
	{

		world.setLightValue(par1EnumSkyBlock, par2, par3, par4, par5);
	}

	@Override
	public void markBlockForRenderUpdate(int par1, int par2, int par3)
	{

		world.markBlockForRenderUpdate(par1, par2, par3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4)
	{

		return world.getLightBrightnessForSkyBlocks(par1, par2, par3, par4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getBrightness(int par1, int par2, int par3, int par4)
	{

		return world.getBrightness(par1, par2, par3, par4);
	}

	@Override
	public float getLightBrightness(int par1, int par2, int par3)
	{

		return world.getLightBrightness(par1, par2, par3);
	}

	@Override
	public boolean isDaytime()
	{

		return world.isDaytime();
	}

	@Override
	public MovingObjectPosition rayTraceBlocks(Vec3 par1Vec3, Vec3 par2Vec3)
	{

		return world.rayTraceBlocks(par1Vec3, par2Vec3);
	}

	@Override
	public MovingObjectPosition rayTraceBlocks_do(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3)
	{

		return world.rayTraceBlocks_do(par1Vec3, par2Vec3, par3);
	}

	@Override
	public MovingObjectPosition rayTraceBlocks_do_do(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3, boolean par4)
	{

		return world.rayTraceBlocks_do_do(par1Vec3, par2Vec3, par3, par4);
	}

	@Override
	public void playSoundAtEntity(Entity par1Entity, String par2Str, float par3, float par4)
	{

		world.playSoundAtEntity(par1Entity, par2Str, par3, par4);
	}

	@Override
	public void playSoundToNearExcept(EntityPlayer par1EntityPlayer, String par2Str, float par3, float par4)
	{

		world.playSoundToNearExcept(par1EntityPlayer, par2Str, par3, par4);
	}

	@Override
	public void playSoundEffect(double par1, double par3, double par5, String par7Str, float par8, float par9)
	{

		world.playSoundEffect(par1, par3, par5, par7Str, par8, par9);
	}

	@Override
	public void playSound(double par1, double par3, double par5, String par7Str, float par8, float par9, boolean par10)
	{

		world.playSound(par1, par3, par5, par7Str, par8, par9, par10);
	}

	@Override
	public void playRecord(String par1Str, int par2, int par3, int par4)
	{

		world.playRecord(par1Str, par2, par3, par4);
	}

	@Override
	public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12)
	{

		world.spawnParticle(par1Str, par2, par4, par6, par8, par10, par12);
	}

	@Override
	public boolean addWeatherEffect(Entity par1Entity)
	{

		return world.addWeatherEffect(par1Entity);
	}

	@Override
	public boolean spawnEntityInWorld(Entity par1Entity)
	{

		return world.spawnEntityInWorld(par1Entity);
	}

	@Override
	public void releaseEntitySkin(Entity par1Entity)
	{

		world.releaseEntitySkin(par1Entity);
	}

	@Override
	public void removeEntity(Entity par1Entity)
	{

		world.removeEntity(par1Entity);
	}

	@Override
	public void removePlayerEntityDangerously(Entity par1Entity)
	{

		world.removePlayerEntityDangerously(par1Entity);
	}

	@Override
	public void addWorldAccess(IWorldAccess par1iWorldAccess)
	{

		world.addWorldAccess(par1iWorldAccess);
	}

	@Override
	public List getCollidingBoundingBoxes(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB)
	{

		return world.getCollidingBoundingBoxes(par1Entity, par2AxisAlignedBB);
	}

	@Override
	public int calculateSkylightSubtracted(float par1)
	{

		return world.calculateSkylightSubtracted(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void removeWorldAccess(IWorldAccess par1iWorldAccess)
	{

		world.removeWorldAccess(par1iWorldAccess);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getSunBrightness(float par1)
	{

		return world.getSunBrightness(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getSkyColor(Entity par1Entity, float par2)
	{

		return world.getSkyColor(par1Entity, par2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getSkyColorBody(Entity par1Entity, float par2)
	{

		return world.getSkyColorBody(par1Entity, par2);
	}

	@Override
	public float getCelestialAngle(float par1)
	{

		return world.getCelestialAngle(par1);
	}

	@Override
	public int getMoonPhase()
	{

		return world.getMoonPhase();
	}

	@Override
	public float getCelestialAngleRadians(float par1)
	{

		return world.getCelestialAngleRadians(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getCloudColour(float par1)
	{

		return world.getCloudColour(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 drawCloudsBody(float par1)
	{

		return world.drawCloudsBody(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float par1)
	{

		return world.getFogColor(par1);
	}

	@Override
	public int getPrecipitationHeight(int par1, int par2)
	{

		return world.getPrecipitationHeight(par1, par2);
	}

	@Override
	public int getTopSolidOrLiquidBlock(int par1, int par2)
	{

		return world.getTopSolidOrLiquidBlock(par1, par2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{

		return world.getStarBrightness(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightnessBody(float par1)
	{

		return world.getStarBrightnessBody(par1);
	}

	@Override
	public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5)
	{

		world.scheduleBlockUpdate(par1, par2, par3, par4, par5);
	}

	@Override
	public void func_82740_a(int par1, int par2, int par3, int par4, int par5, int par6)
	{

		world.func_82740_a(par1, par2, par3, par4, par5, par6);
	}

	@Override
	public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5, int par6)
	{

		world.scheduleBlockUpdateFromLoad(par1, par2, par3, par4, par5, par6);
	}

	@Override
	public void updateEntities()
	{

		world.updateEntities();
	}

	@Override
	public void addTileEntity(Collection par1Collection)
	{

		world.addTileEntity(par1Collection);
	}

	@Override
	public void updateEntity(Entity par1Entity)
	{

		world.updateEntity(par1Entity);
	}

	@Override
	public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2)
	{

		world.updateEntityWithOptionalForce(par1Entity, par2);
	}


	@Override
	public boolean isAnyLiquid(AxisAlignedBB par1AxisAlignedBB)
	{

		return world.isAnyLiquid(par1AxisAlignedBB);
	}

	@Override
	public boolean isBoundingBoxBurning(AxisAlignedBB par1AxisAlignedBB)
	{

		return world.isBoundingBoxBurning(par1AxisAlignedBB);
	}

	@Override
	public boolean handleMaterialAcceleration(AxisAlignedBB par1AxisAlignedBB, Material par2Material, Entity par3Entity)
	{

		return world.handleMaterialAcceleration(par1AxisAlignedBB, par2Material, par3Entity);
	}

	@Override
	public boolean isMaterialInBB(AxisAlignedBB par1AxisAlignedBB, Material par2Material)
	{

		return world.isMaterialInBB(par1AxisAlignedBB, par2Material);
	}

	@Override
	public boolean isAABBInMaterial(AxisAlignedBB par1AxisAlignedBB, Material par2Material)
	{

		return world.isAABBInMaterial(par1AxisAlignedBB, par2Material);
	}

	@Override
	public Explosion createExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9)
	{

		return world.createExplosion(par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10)
	{

		return world.newExplosion(par1Entity, par2, par4, par6, par8, par9, par10);
	}

	@Override
	public float getBlockDensity(Vec3 par1Vec3, AxisAlignedBB par2AxisAlignedBB)
	{

		return world.getBlockDensity(par1Vec3, par2AxisAlignedBB);
	}

	@Override
	public boolean extinguishFire(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5)
	{

		return world.extinguishFire(par1EntityPlayer, par2, par3, par4, par5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getDebugLoadedEntities()
	{

		return world.getDebugLoadedEntities();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getProviderName()
	{

		return world.getProviderName();
	}

	@Override
	public void setBlockTileEntity(int par1, int par2, int par3, TileEntity par4TileEntity)
	{

		world.setBlockTileEntity(par1, par2, par3, par4TileEntity);
	}

	@Override
	public void removeBlockTileEntity(int par1, int par2, int par3)
	{

		world.removeBlockTileEntity(par1, par2, par3);
	}

	@Override
	public void markTileEntityForDespawn(TileEntity par1TileEntity)
	{

		world.markTileEntityForDespawn(par1TileEntity);
	}

	@Override
	public boolean isBlockOpaqueCube(int par1, int par2, int par3)
	{

		return world.isBlockOpaqueCube(par1, par2, par3);
	}

	@Override
	public boolean isBlockNormalCube(int par1, int par2, int par3)
	{

		return world.isBlockNormalCube(par1, par2, par3);
	}

	@Override
	public boolean func_85174_u(int par1, int par2, int par3)
	{

		return world.func_85174_u(par1, par2, par3);
	}

	@Override
	public boolean doesBlockHaveSolidTopSurface(int par1, int par2, int par3)
	{

		return world.doesBlockHaveSolidTopSurface(par1, par2, par3);
	}

	@Override
	@Deprecated
	public boolean func_102026_a(Block par1Block, int par2)
	{

		return world.func_102026_a(par1Block, par2);
	}

	@Override
	public boolean isBlockNormalCubeDefault(int par1, int par2, int par3, boolean par4)
	{

		return world.isBlockNormalCubeDefault(par1, par2, par3, par4);
	}

	@Override
	public void calculateInitialSkylight()
	{

		world.calculateInitialSkylight();
	}

	@Override
	public void setAllowedSpawnTypes(boolean par1, boolean par2)
	{

		world.setAllowedSpawnTypes(par1, par2);
	}

	@Override
	public void tick()
	{

		world.tick();
	}

	@Override
	public void calculateInitialWeatherBody()
	{

		world.calculateInitialWeatherBody();
	}

	@Override
	public void updateWeatherBody()
	{

		world.updateWeatherBody();
	}

	@Override
	public void toggleRain()
	{

		world.toggleRain();
	}

	@Override
	public boolean isBlockFreezable(int par1, int par2, int par3)
	{

		return world.isBlockFreezable(par1, par2, par3);
	}

	@Override
	public boolean isBlockFreezableNaturally(int par1, int par2, int par3)
	{

		return world.isBlockFreezableNaturally(par1, par2, par3);
	}

	@Override
	public boolean canBlockFreeze(int par1, int par2, int par3, boolean par4)
	{

		return world.canBlockFreeze(par1, par2, par3, par4);
	}

	@Override
	public boolean canBlockFreezeBody(int par1, int par2, int par3, boolean par4)
	{

		return world.canBlockFreezeBody(par1, par2, par3, par4);
	}

	@Override
	public boolean canSnowAt(int par1, int par2, int par3)
	{

		return world.canSnowAt(par1, par2, par3);
	}

	@Override
	public boolean canSnowAtBody(int par1, int par2, int par3)
	{

		return world.canSnowAtBody(par1, par2, par3);
	}

	@Override
	public void updateAllLightTypes(int par1, int par2, int par3)
	{

		world.updateAllLightTypes(par1, par2, par3);
	}

	@Override
	public void updateLightByType(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
	{

		world.updateLightByType(par1EnumSkyBlock, par2, par3, par4);
	}

	@Override
	public boolean tickUpdates(boolean par1)
	{

		return world.tickUpdates(par1);
	}

	@Override
	public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2)
	{

		return world.getPendingBlockUpdates(par1Chunk, par2);
	}

	@Override
	public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB)
	{

		return world.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB);
	}

	@Override
	public List getEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB)
	{

		return world.getEntitiesWithinAABB(par1Class, par2AxisAlignedBB);
	}

	@Override
	public List selectEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, IEntitySelector par3iEntitySelector)
	{

		return world.selectEntitiesWithinAABB(par1Class, par2AxisAlignedBB, par3iEntitySelector);
	}

	@Override
	public Entity findNearestEntityWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, Entity par3Entity)
	{

		return world.findNearestEntityWithinAABB(par1Class, par2AxisAlignedBB, par3Entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List getLoadedEntityList()
	{

		return world.getLoadedEntityList();
	}

	@Override
	public void updateTileEntityChunkAndDoNothing(int par1, int par2, int par3, TileEntity par4TileEntity)
	{

		world.updateTileEntityChunkAndDoNothing(par1, par2, par3, par4TileEntity);
	}

	@Override
	public int countEntities(Class par1Class)
	{

		return world.countEntities(par1Class);
	}

	@Override
	public void addLoadedEntities(List par1List)
	{

		world.addLoadedEntities(par1List);
	}

	@Override
	public void unloadEntities(List par1List)
	{

		world.unloadEntities(par1List);
	}

	@Override
	public boolean canPlaceEntityOnSide(int par1, int par2, int par3, int par4, boolean par5, int par6, Entity par7Entity, ItemStack par8ItemStack)
	{

		return world.canPlaceEntityOnSide(par1, par2, par3, par4, par5, par6, par7Entity, par8ItemStack);
	}

	@Override
	public PathEntity getPathEntityToEntity(Entity par1Entity, Entity par2Entity, float par3, boolean par4, boolean par5, boolean par6, boolean par7)
	{

		return world.getPathEntityToEntity(par1Entity, par2Entity, par3, par4, par5, par6, par7);
	}

	@Override
	public PathEntity getEntityPathToXYZ(Entity par1Entity, int par2, int par3, int par4, float par5, boolean par6, boolean par7, boolean par8, boolean par9)
	{

		return world.getEntityPathToXYZ(par1Entity, par2, par3, par4, par5, par6, par7, par8, par9);
	}

	@Override
	public int isBlockProvidingPowerTo(int par1, int par2, int par3, int par4)
	{

		return world.isBlockProvidingPowerTo(par1, par2, par3, par4);
	}

	@Override
	public int getBlockPowerInput(int par1, int par2, int par3)
	{

		return world.getBlockPowerInput(par1, par2, par3);
	}

	@Override
	public boolean getIndirectPowerOutput(int par1, int par2, int par3, int par4)
	{

		return world.getIndirectPowerOutput(par1, par2, par3, par4);
	}

	@Override
	public int getIndirectPowerLevelTo(int par1, int par2, int par3, int par4)
	{

		return world.getIndirectPowerLevelTo(par1, par2, par3, par4);
	}

	@Override
	public boolean isBlockIndirectlyGettingPowered(int par1, int par2, int par3)
	{

		return world.isBlockIndirectlyGettingPowered(par1, par2, par3);
	}

	@Override
	public int getStrongestIndirectPower(int par1, int par2, int par3)
	{

		return world.getStrongestIndirectPower(par1, par2, par3);
	}

	@Override
	public EntityPlayer getClosestPlayerToEntity(Entity par1Entity, double par2)
	{

		return world.getClosestPlayerToEntity(par1Entity, par2);
	}

	@Override
	public EntityPlayer getClosestPlayer(double par1, double par3, double par5, double par7)
	{

		return world.getClosestPlayer(par1, par3, par5, par7);
	}

	@Override
	public EntityPlayer getClosestVulnerablePlayerToEntity(Entity par1Entity, double par2)
	{

		return world.getClosestVulnerablePlayerToEntity(par1Entity, par2);
	}

	@Override
	public EntityPlayer getClosestVulnerablePlayer(double par1, double par3, double par5, double par7)
	{

		return world.getClosestVulnerablePlayer(par1, par3, par5, par7);
	}

	@Override
	public EntityPlayer getPlayerEntityByName(String par1Str)
	{

		return world.getPlayerEntityByName(par1Str);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void sendQuittingDisconnectingPacket()
	{

		world.sendQuittingDisconnectingPacket();
	}

	@Override
	public void checkSessionLock() throws MinecraftException
	{

		world.checkSessionLock();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void func_82738_a(long par1)
	{

		world.func_82738_a(par1);
	}

	@Override
	public long getSeed()
	{

		return world.getSeed();
	}

	@Override
	public long getTotalWorldTime()
	{

		return world.getTotalWorldTime();
	}

	@Override
	public long getWorldTime()
	{

		return world.getWorldTime();
	}

	@Override
	public void setWorldTime(long par1)
	{

		world.setWorldTime(par1);
	}

	@Override
	public ChunkCoordinates getSpawnPoint()
	{

		return world.getSpawnPoint();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setSpawnLocation(int par1, int par2, int par3)
	{
		world.setSpawnLocation(par1, par2, par3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void joinEntityInSurroundings(Entity par1Entity)
	{
		world.joinEntityInSurroundings(par1Entity);
	}

	@Override
	public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4)
	{
		return world.canMineBlock(par1EntityPlayer, par2, par3, par4);
	}

	@Override
	public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4)
	{
		return world.canMineBlockBody(par1EntityPlayer, par2, par3, par4);
	}

	@Override
	public void setEntityState(Entity par1Entity, byte par2)
	{
		world.setEntityState(par1Entity, par2);
	}

	@Override
	public IChunkProvider getChunkProvider()
	{
		return world.getChunkProvider();
	}

	@Override
	public void addBlockEvent(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		world.addBlockEvent(par1, par2, par3, par4, par5, par6);
	}

	@Override
	public ISaveHandler getSaveHandler()
	{
		return world.getSaveHandler();
	}

	@Override
	public WorldInfo getWorldInfo()
	{
		return world.getWorldInfo();
	}

	@Override
	public GameRules getGameRules()
	{
		return world.getGameRules();
	}

	@Override
	public void updateAllPlayersSleepingFlag()
	{
		world.updateAllPlayersSleepingFlag();
	}

	@Override
	public float getWeightedThunderStrength(float par1)
	{
		return world.getWeightedThunderStrength(par1);
	}

	@Override
	public float getRainStrength(float par1)
	{
		return world.getRainStrength(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setRainStrength(float par1)
	{
		world.setRainStrength(par1);
	}

	@Override
	public boolean isThundering()
	{
		return world.isThundering();
	}

	@Override
	public boolean isRaining()
	{
		return world.isRaining();
	}

	@Override
	public boolean canLightningStrikeAt(int par1, int par2, int par3)
	{
		return world.canLightningStrikeAt(par1, par2, par3);
	}

	@Override
	public boolean isBlockHighHumidity(int par1, int par2, int par3)
	{
		return world.isBlockHighHumidity(par1, par2, par3);
	}

	@Override
	public void setItemData(String par1Str, WorldSavedData par2WorldSavedData)
	{
		world.setItemData(par1Str, par2WorldSavedData);
	}

	@Override
	public WorldSavedData loadItemData(Class par1Class, String par2Str)
	{
		return world.loadItemData(par1Class, par2Str);
	}

	@Override
	public int getUniqueDataId(String par1Str)
	{
		return world.getUniqueDataId(par1Str);
	}

	@Override
	public void func_82739_e(int par1, int par2, int par3, int par4, int par5)
	{
		world.func_82739_e(par1, par2, par3, par4, par5);
	}

	@Override
	public void playAuxSFX(int par1, int par2, int par3, int par4, int par5)
	{
		world.playAuxSFX(par1, par2, par3, par4, par5);
	}

	@Override
	public void playAuxSFXAtEntity(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6)
	{
		world.playAuxSFXAtEntity(par1EntityPlayer, par2, par3, par4, par5, par6);
	}

	@Override
	public int getHeight()
	{
		return world.getHeight();
	}

	@Override
	public int getActualHeight()
	{
		return world.getActualHeight();
	}

	@Override
	public IUpdatePlayerListBox func_82735_a(EntityMinecart par1EntityMinecart)
	{
		return world.func_82735_a(par1EntityMinecart);
	}

	@Override
	public Random setRandomSeed(int par1, int par2, int par3)
	{
		return world.setRandomSeed(par1, par2, par3);
	}

	@Override
	public ChunkPosition findClosestStructure(String par1Str, int par2, int par3, int par4)
	{
		return world.findClosestStructure(par1Str, par2, par3, par4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache()
	{

		return world.extendedLevelsInChunkCache();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getHorizon()
	{
		return world.getHorizon();
	}

	@Override
	public CrashReportCategory addWorldInfoToCrashReport(CrashReport par1CrashReport)
	{
		return world.addWorldInfoToCrashReport(par1CrashReport);
	}

	@Override
	public void destroyBlockInWorldPartially(int par1, int par2, int par3, int par4, int par5)
	{
		world.destroyBlockInWorldPartially(par1, par2, par3, par4, par5);
	}

	@Override
	public Vec3Pool getWorldVec3Pool()
	{
		return world.getWorldVec3Pool();
	}

	@Override
	public Calendar getCurrentDate()
	{
		return world.getCurrentDate();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void func_92088_a(double par1, double par3, double par5, double par7, double par9, double par11, NBTTagCompound par13nbtTagCompound)
	{
		world.func_92088_a(par1, par3, par5, par7, par9, par11, par13nbtTagCompound);
	}

	@Override
	public Scoreboard getScoreboard()
	{
		return world.getScoreboard();
	}

	@Override
	public void func_96440_m(int par1, int par2, int par3, int par4)
	{
		world.func_96440_m(par1, par2, par3, par4);
	}

	@Override
	public ILogAgent getWorldLogAgent()
	{
		return world.getWorldLogAgent();
	}

	@Override
	public void addTileEntity(TileEntity entity)
	{
		world.addTileEntity(entity);
	}

	@Override
	public boolean isBlockSolidOnSide(int x, int y, int z, ForgeDirection side)
	{
		return world.isBlockSolidOnSide(x, y, z, side);
	}

	@Override
	public boolean isBlockSolidOnSide(int x, int y, int z, ForgeDirection side, boolean _default)
	{
		return world.isBlockSolidOnSide(x, y, z, side, _default);
	}

	@Override
	public ImmutableSetMultimap<ChunkCoordIntPair, Ticket> getPersistentChunks()
	{
		return world.getPersistentChunks();
	}

	@Override
	public int getBlockLightOpacity(int x, int y, int z)
	{
		return world.getBlockLightOpacity(x, y, z);
	}

	@Override
	public int countEntities(EnumCreatureType type, boolean forSpawnCount)
	{
		return world.countEntities(type, forSpawnCount);
	}

	@Override
	public List getCollidingBlockBounds(AxisAlignedBB par1AxisAlignedBB)
	{
		return world.getCollidingBlockBounds(par1AxisAlignedBB);
	}

	@Override
	public boolean checkNoEntityCollision(AxisAlignedBB par1AxisAlignedBB)
	{
		return world.checkNoEntityCollision(par1AxisAlignedBB);
	}

	@Override
	public boolean checkNoEntityCollision(AxisAlignedBB par1AxisAlignedBB, Entity par2Entity)
	{
		return world.checkNoEntityCollision(par1AxisAlignedBB, par2Entity);
	}

	@Override
	public boolean checkBlockCollision(AxisAlignedBB par1AxisAlignedBB)
	{
		return world.checkBlockCollision(par1AxisAlignedBB);
	}

	@Override
	public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB, IEntitySelector par3iEntitySelector)
	{
		return world.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB, par3iEntitySelector);
	}
}
