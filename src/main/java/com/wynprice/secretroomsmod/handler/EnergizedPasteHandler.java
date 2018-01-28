package com.wynprice.secretroomsmod.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.secretroomsmod.SecretConfig;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.items.CamouflagePaste;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketRemoveEnergizedPaste;
import com.wynprice.secretroomsmod.network.packets.MessagePacketSwingArm;
import com.wynprice.secretroomsmod.network.packets.MessagePacketSyncEnergizedPaste;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

public class EnergizedPasteHandler 
{
	private static HashMap<Integer, HashMap<BlockPos, Pair<IBlockState, IBlockState>>> energized_map = new HashMap<>();
	

	public static void putState(World world, BlockPos pos, IBlockState state)
	{
		putState(world.provider.getDimension(), pos, state, world.getBlockState(pos));
	}
	
	public static void putState(int dim, BlockPos pos, IBlockState state, IBlockState replaceState)
	{
		pos = new BlockPos(pos);
		HashMap<BlockPos, Pair<IBlockState, IBlockState>> innerMap = energized_map.get(dim) == null || energized_map.get(dim).keySet() == null || energized_map.get(dim).keySet().isEmpty() ? new HashMap<>() : energized_map.get(dim);
		innerMap.put(pos, Pair.of(state, replaceState));
		energized_map.put(dim, innerMap);
	}
	
	public static void removeReplacedState(int dim, BlockPos pos)
	{
		pos = new BlockPos(pos);
		HashMap<BlockPos, Pair<IBlockState, IBlockState>> innerMap = energized_map.get(dim) == null || energized_map.get(dim).keySet() == null || energized_map.get(dim).keySet().isEmpty() ? new HashMap<>() : energized_map.get(dim);
		try
		{
			if(innerMap.containsKey(pos))
				innerMap.remove(pos);

		}
		catch (ConcurrentModificationException e) 
		{
			e.printStackTrace();
			return;
		}
		energized_map.put(dim, innerMap);
	}
	
	public static boolean hasReplacedState(World world, BlockPos pos)
	{
		pos = new BlockPos(pos);
		if(energized_map.get(world.provider.getDimension()) == null)
			return false;
		HashMap<BlockPos, Pair<IBlockState, IBlockState>> innerMap = energized_map.get(world.provider.getDimension());
		if(innerMap.containsKey(pos))
			return canBlockBeMirrored(world.getBlockState(pos).getBlock(), world, world.getBlockState(pos), pos);//Extra precaution
		return false;
	}
	
	public static IBlockState getReplacedState(World world, BlockPos pos)
	{
		pos = new BlockPos(pos);
		if(!hasReplacedState(world, pos)) return Blocks.AIR.getDefaultState(); // should never happen
		try
		{
			for(BlockPos blockpos : energized_map.get(world.provider.getDimension()).keySet())
				if(blockpos.equals(pos))
					return energized_map.get(world.provider.getDimension()).get(blockpos).getLeft();
		}
		catch (NullPointerException e) //Can be thrown when /resetenergized is called. As it is a temp command, this is temp
		{
			;
		}
		return Blocks.AIR.getDefaultState();
	}
	
	public static IBlockState getSetBlockState(World world, BlockPos pos)
	{
		pos = new BlockPos(pos);
		if(!hasReplacedState(world, pos)) return null; // should never happen
		for(BlockPos blockpos : energized_map.get(world.provider.getDimension()).keySet())
			if(blockpos.equals(pos))
				return energized_map.get(world.provider.getDimension()).get(blockpos).getRight();
		return null;
	}
	
	public static boolean canBlockBeMirrored(Block block, World world, IBlockState state, BlockPos pos)
	{
		if(block instanceof ISecretBlock) return false;
		boolean directFromClass = false;
		try
		{
			directFromClass = (boolean) block.getClass().getMethod("canBlockBeMirrored", World.class, IBlockState.class, BlockPos.class).invoke(block, world, state,  pos);
		}
		catch (Throwable e) 
		{
			;
		}
		return directFromClass || !Arrays.asList(SecretConfig.energized_blacklist_mirror).contains(block.getRegistryName().toString());
	}
	
	public static boolean canBlockBeReplaced(Block block, World world, IBlockState state, BlockPos pos)
	{
		if(block instanceof ISecretBlock) return false;
		boolean directFromClass = false;
		try
		{
			directFromClass = (boolean) block.getClass().getMethod("canBlockBeReplaced", World.class, IBlockState.class, BlockPos.class).invoke(block, world, state, pos);
		}
		catch (Throwable e) 
		{
			;
		}
		return directFromClass || !Arrays.asList(SecretConfig.energized_non_replacable).contains(block.getRegistryName().toString());
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
	
	private ArrayList<Location> previousServerTickMap = new ArrayList<>();
	
	@SubscribeEvent
	public void onMouseRightClick(RightClickBlock event)
	{
		Location location = new Location(event.getPos(), event.getWorld());
		if((hasReplacedState(event.getWorld(), event.getPos()) && event.getEntityPlayer().getHeldItemMainhand().isEmpty() && event.getEntityPlayer().isSneaking()) || previousServerTickMap.contains(location))
		{	
			if(!event.getWorld().isRemote)
			{
				if(previousServerTickMap.contains(location))
					previousServerTickMap.remove(location);
				else
				{
					previousServerTickMap.add(location);
					IBlockState state = EnergizedPasteHandler.getReplacedState(event.getWorld(), event.getPos());
					EnergizedPasteHandler.removeReplacedState(event.getWorld().provider.getDimension(), event.getPos());
					SecretNetwork.sendToAll(new MessagePacketSyncEnergizedPaste(event.getWorld().provider.getDimension(), event.getPos(), null, false));
					if(((EntityPlayerMP)event.getEntityPlayer()).interactionManager.getGameType() == GameType.SURVIVAL)
					{
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setString("hit_block", state.getBlock().getRegistryName().toString());
						nbt.setInteger("hit_meta", state.getBlock().getMetaFromState(state));
						nbt.setInteger("hit_color", state.getMapColor(event.getWorld(), event.getPos()).colorValue);
						ItemStack stack = new ItemStack(SecretItems.CAMOUFLAGE_PASTE, 1, 1);
						stack.setTagCompound(nbt);
						boolean flag = event.getEntityPlayer().inventory.addItemStackToInventory(stack);
			            if (flag)
			            {
			            	event.getEntityPlayer().world.playSound((EntityPlayer)null, event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((event.getEntityPlayer().getRNG().nextFloat() - event.getEntityPlayer().getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			                event.getEntityPlayer().inventoryContainer.detectAndSendChanges();
			            }
			            else //Should not be possible, clicking with an empty hand, so there will always be a space. Will add code just in case
			            {
			                EntityItem entityitem = new EntityItem(event.getWorld(), event.getPos().getX() + 0.5d, event.getPos().getY() + 1d, event.getPos().getZ() + 0.5d, stack);
			                entityitem.setNoPickupDelay();
		                    entityitem.setOwner(event.getEntityPlayer().getName());
		                    event.getWorld().spawnEntity(entityitem);
			            }
					}
				}
			}
			event.setCanceled(true);
		}
		else if(event.getEntityPlayer().getHeldItemMainhand().getItem() == SecretItems.CAMOUFLAGE_PASTE && event.getEntityPlayer().getHeldItemMainhand().getMetadata() == 1)
		{
			if(event.getEntityPlayer().isSneaking() && EnergizedPasteHandler.canBlockBeMirrored(event.getWorld().getBlockState(event.getPos()).getBlock(), event.getWorld(), event.getWorld().getBlockState(event.getPos()), event.getPos()))
			{
				if(!event.getEntityPlayer().getHeldItemMainhand().hasTagCompound())
					event.getEntityPlayer().getHeldItemMainhand().setTagCompound(new NBTTagCompound());

				event.getEntityPlayer().getHeldItemMainhand().getTagCompound().setString("hit_block", event.getWorld().getBlockState(event.getPos()).getBlock().getRegistryName().toString());
				event.getEntityPlayer().getHeldItemMainhand().getTagCompound().setInteger("hit_meta", event.getWorld().getBlockState(event.getPos()).getBlock().getMetaFromState(event.getWorld().getBlockState(event.getPos())));
				event.getEntityPlayer().getHeldItemMainhand().getTagCompound().setInteger("hit_color", event.getWorld().getBlockState(event.getPos()).getMapColor(event.getWorld(), event.getPos()).colorValue);
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(SecretConfig.sound_set_name));
				if(soundEvent == null) soundEvent = SoundEvents.BLOCK_SAND_PLACE;
				event.getWorld().playSound(event.getEntityPlayer(), event.getPos(), soundEvent, SoundCategory.BLOCKS, (float) SecretConfig.sound_set_vol, (float) SecretConfig.sound_set_pitch);
			}
			else
			{
				event.setCanceled(true);
				ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
				if(stack.hasTagCompound() && stack.getTagCompound().hasKey("hit_block", 8) && stack.getTagCompound().hasKey("hit_meta", 99))
				{
					Block block = Block.REGISTRY.getObject(new ResourceLocation(stack.getTagCompound().getString("hit_block")));
					if(block != Blocks.AIR)
					{
						IBlockState state = block.getStateFromMeta(stack.getTagCompound().getInteger("hit_meta"));
						if(EnergizedPasteHandler.canBlockBeMirrored(block, event.getWorld(), state, event.getPos()) && EnergizedPasteHandler.canBlockBeReplaced(event.getWorld().getBlockState(event.getPos()).getBlock(), event.getWorld(), event.getWorld().getBlockState(event.getPos()), event.getPos()))
						{
							if(!event.getWorld().isRemote)
							{
								EnergizedPasteHandler.putState(event.getWorld(), event.getPos(), state);
								SecretNetwork.sendToAll(new MessagePacketSyncEnergizedPaste(event.getWorld().provider.getDimension(), event.getPos(), state, true));
								if(((EntityPlayerMP)event.getEntityPlayer()).interactionManager.getGameType() == GameType.SURVIVAL)
									if(!previousServerTickMap.contains(location))
									{
										event.getEntityPlayer().getHeldItemMainhand().shrink(1);
										previousServerTickMap.add(location);
									}
									else
										previousServerTickMap.remove(location);
								event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
								SecretNetwork.sendToPlayer(event.getEntityPlayer(), new MessagePacketSwingArm(EnumHand.MAIN_HAND));
							}
							else
							{
								SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(SecretConfig.sound_use_name));
								if(soundEvent == null) soundEvent = SoundEvents.BLOCK_SLIME_BREAK;
								event.getWorld().playSound(event.getEntityPlayer(), event.getPos(), soundEvent, SoundCategory.BLOCKS, (float) SecretConfig.sound_use_vol, (float) SecretConfig.sound_use_pitch);
							}
							
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldSaved(WorldEvent.Save event)
	{
		if(event.getWorld().isRemote) return;		
		try 
		{
			CompressedStreamTools.writeCompressed(saveToNBT(), new FileOutputStream(new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/" + SecretRooms5.MODID + "_data.dat")));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public void onEntityTick(WorldTickEvent event)
	{
		if(event.world.isRemote) return;
		for(Entity entity : new ArrayList<>(event.world.loadedEntityList))
			if(entity instanceof EntityItem)
			{
				EntityItem ei = (EntityItem)entity;
				if(ei.getItem().getItem() instanceof CamouflagePaste)
				{
					if(ei.isBurning() || ei.world.getBlockState(ei.getPosition()).getBlock() == Blocks.FIRE)
					{
						NBTTagCompound compound = new NBTTagCompound();
						ei.writeEntityToNBT(compound);
						compound.setShort("Health", (short) 100);
						ei.readEntityFromNBT(compound);
						if(ei.getItem().getMaxDamage() == 0 && !ei.isDead && ei.getItem().getCount() >= 5)
						{
							EntityItem newItem = new EntityItem(ei.world, ei.posX, ei.posY, ei.posZ, new ItemStack(SecretItems.CAMOUFLAGE_PASTE, Math.floorDiv(ei.getItem().getCount(), 5), 1));
							newItem.motionX = ei.motionX;
							newItem.motionY = ei.motionY;
							newItem.motionZ = ei.motionZ;
							newItem.hoverStart = ei.hoverStart;
							newItem.rotationYaw = ei.rotationYaw;
							if(ei.getItem().getCount() % 5 == 0)
								ei.setDead();
							else
								ei.getItem().setCount(ei.getItem().getCount() % 5);
							event.world.spawnEntity(newItem);
						}
					}
					else
					{
						NBTTagCompound compound = new NBTTagCompound();
						ei.writeEntityToNBT(compound);
						if(compound.getShort("Health") > 5)
						{
							compound.setShort("Health", (short) 5);
							ei.readEntityFromNBT(compound);
						}
					}
						
				}
			}
	}
	
	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load event)
	{
		if(event.getWorld().isRemote) return;
		try 
		{
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/" + SecretRooms5.MODID + "_data.dat")));
			if(nbt != null)
				readFromNBT(nbt);
		} 
		catch (FileNotFoundException e) {
			SecretRooms5.LOGGER.info("Secret Rooms Mod file cannot be found. Assuming world creation");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
	}
	
	public static NBTTagCompound saveToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound nbt_info = new NBTTagCompound();
		NBTTagCompound nbt_worlds = new NBTTagCompound();
		HashMap<Integer, HashMap<BlockPos, Pair<IBlockState, IBlockState>>> energized_map = EnergizedPasteHandler.energized_map;
		int[] dimensions = new int[energized_map.size()];
		for(int i = 0; i < dimensions.length; i++)
			dimensions[i] = (int) energized_map.keySet().toArray()[i];
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
		
		return nbt;
	}
	
	public static void readFromNBT(NBTTagCompound nbt)
	{
		energized_map.clear();
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
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event)
	{
		SecretNetwork.sendToPlayer(event.player, new MessagePacketSyncEnergizedPaste(saveToNBT(), null));
	}
	
	public static HashMap<Integer, HashMap<BlockPos, Pair<IBlockState, IBlockState>>> getEnergized_map() {
		return energized_map;
	}
	
	private class Location
	{
		private final BlockPos pos;
		private final World world;
		
		public Location(BlockPos pos, World world) 
		{
			this.pos = pos;
			this.world = world;
		}
		
		public BlockPos getPos() {
			return pos;
		}
		
		public World getWorld() {
			return world;
		}
		
		@Override
		public boolean equals(Object obj) 
		{
			return obj instanceof Location && ((Location)obj).pos.equals(pos) && ((Location)obj).world.provider.getDimension() == world.provider.getDimension();
		}
	}
}
	