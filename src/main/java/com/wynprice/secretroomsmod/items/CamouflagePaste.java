package com.wynprice.secretroomsmod.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;

public class CamouflagePaste extends Item
//Fix lighting issues.
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab))
        {
        	items.add(new ItemStack(this));
        	items.add(new ItemStack(this, 1, 1));
        }
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		if(stack.getMetadata() == 0 || !stack.hasTagCompound())
		{
			super.addInformation(stack, worldIn, tooltip, flagIn);
			return; 
		}
		
		if(GuiScreen.isShiftKeyDown())
			for(String string : new TextComponentTranslation("camopaste.info", stack.getTagCompound().getString("hit_block"), stack.getTagCompound().getInteger("hit_meta")).getFormattedText().split("<br>"))
					tooltip.add(string);
		else
			tooltip.add(new TextComponentTranslation("camopaste.pressshift").getFormattedText());
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
