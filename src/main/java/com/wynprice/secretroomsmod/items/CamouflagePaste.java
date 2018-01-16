package com.wynprice.secretroomsmod.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;

public class CamouflagePaste extends Item
//TODO make so when block is used, hands swing and server notifies all clients. Remove changing the HashMap on message sent
{
	public CamouflagePaste() 
	{
		this.setRegistryName("camouflage_paste");
		this.setUnlocalizedName("camouflage_paste");
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return "item." + new String[]{"camouflage_paste", "energized_camouflage_paste"}[stack.getMetadata() < 2 ? stack.getMetadata() : 0];
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		if(playerIn.getHeldItem(handIn).getMetadata() == 0 || !playerIn.getHeldItem(handIn).hasTagCompound())
			return super.onItemRightClick(worldIn, playerIn, handIn);
		playerIn.getHeldItem(handIn).getTagCompound().removeTag("hit_block");
		playerIn.getHeldItem(handIn).getTagCompound().removeTag("hit_meta");
		playerIn.getHeldItem(handIn).getTagCompound().removeTag("hit_color");
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{

		if(player.getHeldItem(hand).getMetadata() == 0)
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		IBlockState state = worldIn.getBlockState(pos);
		if(state.getBlock() == Blocks.AIR)
			return EnumActionResult.PASS;
		
		if(!player.getHeldItem(hand).hasTagCompound())
			player.getHeldItem(hand).setTagCompound(new NBTTagCompound());

		player.getHeldItem(hand).getTagCompound().setString("hit_block", worldIn.getBlockState(pos).getBlock().getRegistryName().toString());
		player.getHeldItem(hand).getTagCompound().setInteger("hit_meta", worldIn.getBlockState(pos).getBlock().getMetaFromState(worldIn.getBlockState(pos)));
		player.getHeldItem(hand).getTagCompound().setInteger("hit_color", worldIn.getBlockState(pos).getMapColor(worldIn, pos).colorValue);
		
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		if(stack.getMetadata() == 0 || !stack.hasTagCompound())
			super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if(GuiScreen.isShiftKeyDown())
			for(String string : new TextComponentTranslation("camopaste.info", stack.getTagCompound().getString("hit_block"), stack.getTagCompound().getInteger("hit_meta")).getFormattedText().split("<br>"))
					tooltip.add(string);
		else
			tooltip.add(new TextComponentTranslation("camopaste.pressshift").getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
