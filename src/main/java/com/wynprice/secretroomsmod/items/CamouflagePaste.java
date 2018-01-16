package com.wynprice.secretroomsmod.items;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;

public class CamouflagePaste extends Item
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
		return new String[]{"camouflage_paste", "energized_camouflage_paste"}[stack.getMetadata() < 2 ? stack.getMetadata() : 0];
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		if(playerIn.getHeldItem(handIn).getMetadata() == 0)
			return super.onItemRightClick(worldIn, playerIn, handIn);
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(player.getHeldItem(hand).getMetadata() == 0)
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		if(stack.getMetadata() == 0)
			super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if(GuiScreen.isShiftKeyDown())
			;
		else
			tooltip.add(new TextComponentTranslation("camofier.pressshift").getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
