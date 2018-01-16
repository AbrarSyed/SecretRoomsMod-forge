package com.wynprice.secretroomsmod.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnergizedPasteHandler 
{
	// dimension, pos, energized paste (if exist)
	private static HashMap<Integer, HashMap<BlockPos, Pair<IBlockState, IBlockState>>> energized_map = new HashMap<>();
	
	public static boolean putState(World world, BlockPos pos, IBlockState state)
	{
		return putState(world.provider.getDimension(), pos, state, world.getBlockState(pos));
	}
	
	public static boolean putState(int dim, BlockPos pos, IBlockState state, IBlockState replaceState)
	{
		pos = new BlockPos(pos);
		HashMap<BlockPos, Pair<IBlockState, IBlockState>> innerMap = new HashMap<>(energized_map.get(dim) == null || energized_map.get(dim).keySet() == null || energized_map.get(dim).keySet().isEmpty() ? new HashMap<>() : energized_map.get(dim));
		boolean hadBefore = false;
		try
		{
			ArrayList<BlockPos> remove_positions = new ArrayList<>();
			ArrayList<BlockPos> list = new ArrayList<>(innerMap.keySet());
			for(BlockPos blockpos : list)
				if(blockpos.equals(pos))
				{
					hadBefore = true;
					remove_positions.add(blockpos);
				}
			for(BlockPos blockpos : remove_positions)
				innerMap.remove(blockpos);
		}
		catch (ConcurrentModificationException e) 
		{
			e.printStackTrace();
			return false;
		}
		innerMap.put(pos, Pair.of(state, replaceState));
		energized_map.put(dim, innerMap);
		return hadBefore;
	}
	
	public static boolean hasReplacedState(World world, BlockPos pos)
	{
		pos = new BlockPos(pos);
		if(energized_map.get(world.provider.getDimension()) == null)
			return false;
		HashMap<BlockPos, Pair<IBlockState, IBlockState>> innerMap = new HashMap<>(energized_map.get(world.provider.getDimension()));
		ArrayList<BlockPos> list = new ArrayList<>(innerMap.keySet());
		for(BlockPos blockpos : list)
			if(blockpos.equals(pos)) {
				return true;
			}
		return false;
	}
	
	public static IBlockState getReplacedState(World world, BlockPos pos)
	{
		pos = new BlockPos(pos);
		if(!hasReplacedState(world, pos)) return null; // should never happen
		ArrayList<BlockPos> list = new ArrayList<>(new HashMap<>(energized_map.get(world.provider.getDimension())).keySet());
		for(BlockPos blockpos : list)
			if(blockpos.equals(pos))
				return energized_map.get(world.provider.getDimension()).get(blockpos).getLeft();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onBlockBoundingBoxDrawn(DrawBlockHighlightEvent event)
	{
		if (event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK && hasReplacedState(Minecraft.getMinecraft().world, event.getTarget().getBlockPos()))
        {
			event.setCanceled(true);
			EntityPlayer player = Minecraft.getMinecraft().player;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            BlockPos blockpos = event.getTarget().getBlockPos();
            IBlockState iblockstate = getReplacedState(Minecraft.getMinecraft().world, blockpos);

            if (iblockstate.getMaterial() != Material.AIR && Minecraft.getMinecraft().world.getWorldBorder().contains(blockpos))
            {
                double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.getPartialTicks();
                double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.getPartialTicks();
                double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.getPartialTicks();
                event.getContext().drawSelectionBoundingBox(iblockstate.getSelectedBoundingBox(Minecraft.getMinecraft().world, blockpos).grow(0.0020000000949949026D).offset(-d3, -d4, -d5), 0.0F, 0.0F, 0.0F, 0.4F);
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
	}
	
	@SubscribeEvent
	public void onWorldSaved(WorldEvent.Save event)
	{
		saveToFile();
	}
	
	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load event)
	{
		readFromFile();
	}
	
	public static void saveToFile()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound nbt_info = new NBTTagCompound();
		NBTTagCompound nbt_worlds = new NBTTagCompound();
		HashMap<Integer, HashMap<BlockPos, Pair<IBlockState, IBlockState>>> energized_map = new HashMap<>(EnergizedPasteHandler.energized_map);
		int[] dimensions = new int[energized_map.size()];
		for(int i = 0; i < dimensions.length; i++)
			dimensions[i] = new ArrayList<>(energized_map.keySet()).get(i);
		nbt_info.setIntArray("dimensions", dimensions);
		ArrayList<Integer> list = new ArrayList<>(energized_map.keySet());
		for(int i : list)
		{			
			if(energized_map.get(i) == null)
				continue;
			
			NBTTagCompound nbt_world = new NBTTagCompound();
			int[] blockPositions = new int[energized_map.get(i).size() * 3];
			int index = 0;
			ArrayList<BlockPos> list1 = new ArrayList<>(energized_map.get(i).keySet());
			for(BlockPos pos : list1)
			{
				NBTTagCompound nbt_blockstate = new NBTTagCompound();
				blockPositions[index++] = pos.getX();
				blockPositions[index++] = pos.getY();
				blockPositions[index++] = pos.getZ();
				nbt_blockstate.setString("block", energized_map.get(i).get(pos).getLeft().getBlock().getRegistryName().toString());
				nbt_blockstate.setInteger("meta", energized_map.get(i).get(pos).getLeft().getBlock().getMetaFromState(energized_map.get(i).get(pos).getLeft()));
				nbt_blockstate.setString("replace_block", energized_map.get(i).get(pos).getRight().getBlock().getRegistryName().toString());
				nbt_blockstate.setInteger("replace_meta", energized_map.get(i).get(pos).getRight().getBlock().getMetaFromState(energized_map.get(i).get(pos).getRight()));
				
				nbt_world.setTag(String.valueOf(pos.getX()) + " " + String.valueOf(pos.getY()) + " " + String.valueOf(pos.getZ()), nbt_blockstate);
			}
			nbt_world.setIntArray("blockpos", blockPositions);
			nbt_worlds.setTag("dimension_" + String.valueOf(i), nbt_world);
		}
		
		nbt.setTag("worlds", nbt_worlds);
		nbt.setTag("info", nbt_info);
		File file = new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/" + SecretRooms5.MODID + "_data.dat");
		
		try 
		{
			CompressedStreamTools.safeWrite(nbt, file);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void readFromFile()
	{
		energized_map.clear();
		NBTTagCompound nbt;
		try 
		{
			nbt = CompressedStreamTools.read(new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/" + SecretRooms5.MODID + "_data.dat"));
			if(nbt == null)
				return;
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		
		NBTTagCompound world_info = nbt.getCompoundTag("info");
		for(int dimension : world_info.getIntArray("dimensions"))
		{
			NBTTagCompound nbt_world = nbt.getCompoundTag("worlds").getCompoundTag("dimension_" + String.valueOf(dimension));
			int[] blockpos = nbt_world.getIntArray("blockpos");
			for(int i = 0; i < blockpos.length; i+=3)
			{
				NBTTagCompound nbt_blockstate = nbt_world.getCompoundTag(String.valueOf(blockpos[i]) + " " + String.valueOf(blockpos[i + 1]) + " " + String.valueOf(blockpos[i + 2]));
				putState(dimension, new BlockPos(blockpos[i], blockpos[i + 1], blockpos[i + 2]),
						Block.REGISTRY.getObject(new ResourceLocation(nbt_blockstate.getString("block"))).getStateFromMeta(nbt_blockstate.getInteger("meta")),
						Block.REGISTRY.getObject(new ResourceLocation(nbt_blockstate.getString("replace_block"))).getStateFromMeta(nbt_blockstate.getInteger("replace_meta")));
			}
		}
	}
}
	