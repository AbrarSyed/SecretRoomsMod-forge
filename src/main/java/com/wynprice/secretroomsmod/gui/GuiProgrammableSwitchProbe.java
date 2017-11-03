package com.wynprice.secretroomsmod.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.wynprice.secretroomsmod.gui.slots.SlotItemStuck;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

public class GuiProgrammableSwitchProbe extends GuiContainer
{
	
	private GuiButton buttonExit;
	private GuiTextField textInput;
	private ItemStack stack;
	private IBlockState outputState;
	
	public GuiProgrammableSwitchProbe(ItemStack itemStack) 
	{
		super(new ContainerProgrammableProbe());
		stack = itemStack;
	}

	@Override
	public void initGui() {
        Keyboard.enableRepeatEvents(true);
		this.buttonExit = addButton(new GuiButton(0, this.width / 2 - 100, (int) this.height - 30, 200, 20, I18n.format("gui.done")));
		this.textInput =  new GuiTextField(1, this.fontRenderer, this.width / 2 - 152, this.height / 2, 300, 20);
		this.textInput.setMaxStringLength(60);
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();
		this.textInput.drawTextBox();
		this.textInput.setFocused(true);
		ArrayList<Block> usedBlocks = new ArrayList<>();
		ArrayList<ItemStack> renderStacks = new ArrayList<>();
		Block overblock = Block.getBlockFromName(textInput.getText());
		if(overblock != null)
		{
			NonNullList<ItemStack> items = NonNullList.create();
			overblock.getSubBlocks(CreativeTabs.SEARCH, items);
			for(ItemStack stack : items)
				if(!stack.isEmpty())
						renderStacks.add(stack);
		}
		else
		{
			for(ItemStack stack : OreDictionary.getOres(textInput.getText(), false))
				if(stack.getItem() instanceof ItemBlock)
				{
					if(stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
					{
						NonNullList<ItemStack> items = NonNullList.create();
						stack.getItem().getSubItems(CreativeTabs.SEARCH, items);
						for(ItemStack stack1 : items)
							if(!stack1.isEmpty())
									renderStacks.add(stack1);
					}
					else
						renderStacks.add(stack);
				}
		}
		boolean flag = false;
		int amount = 0;
		inventorySlots.inventorySlots.clear();
		while(!flag)
			flag = renderStacks(renderStacks, amount++);
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}	
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
            if(this.isPointInRegion(this.inventorySlots.inventorySlots.get(i).xPos, this.inventorySlots.inventorySlots.get(i).yPos, 16, 16, mouseX, mouseY))
            	this.outputState = Block.getBlockFromItem(this.inventorySlots.inventorySlots.get(i).getStack().getItem())
            	.getStateForPlacement(Minecraft.getMinecraft().world, BlockPos.ORIGIN, EnumFacing.UP, 0f, 0f, 0f, this.inventorySlots.inventorySlots.get(i).getStack().getMetadata(), 
            			Minecraft.getMinecraft().player, EnumHand.MAIN_HAND);
	}
	
	private boolean renderStacks(ArrayList<ItemStack> renderStacks, int amount)
	{
		boolean flag = renderStacks.size() > 16;
        int renderamount = flag ? 16 : renderStacks.size();
        for(int i = 0; i < renderamount; i ++)
        	inventorySlots.inventorySlots.add(new SlotItemStuck(renderStacks.get(i), (this.width / 2) - (flag || renderamount % 2== 0 ? -3 : 7) - (((renderamount / 2) - (i % 16)) * 20), this.height / 2 - 50 - (amount*25)));
        ArrayList<ItemStack> newStackList = new ArrayList<>();
        if(flag)
        	for(int i = 16; i < renderStacks.size(); i++)
        		newStackList.add(renderStacks.get(i));
        renderStacks.clear();
        renderStacks.addAll(newStackList);
        return !flag;
	}
	
	@Override
	public void drawDefaultBackground() {
		super.drawDefaultBackground();
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
			closeGui();
	}
	
	private void closeGui()
	{
		this.mc.displayGuiScreen((GuiScreen)null);
		System.out.println(outputState);
	}
	
	int timeOver = 0;
	List<String> dictonaryList = new ArrayList<>();
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		if(keyCode == Keyboard.KEY_TAB)
		{					
			ArrayList<String> stringList = new ArrayList<>();
			if(dictonaryList.isEmpty())
			{
				stringList.addAll(CommandBase.getListOfStringsMatchingLastWord(Arrays.asList(this.textInput.getText()).toArray(new String[1]), Block.REGISTRY.getKeys()));
				for(String s : getAllOreDictionaryList())
					for(ItemStack stack : OreDictionary.getOres(s))
						if(Block.getBlockFromItem(stack.getItem()) != Blocks.AIR)
							stringList.add(s);
				dictonaryList = CommandBase.getListOfStringsMatchingLastWord(Arrays.asList(this.textInput.getText()).toArray(new String[1]), stringList);
			}
			if(!dictonaryList.isEmpty())
				this.textInput.setText(dictonaryList.get(timeOver++%dictonaryList.size()));
		}
		else
		{
			timeOver = 0;
			dictonaryList.clear();
		}
		
		if(keyCode == Keyboard.KEY_RETURN)
			closeGui();
		this.textInput.textboxKeyTyped(typedChar, keyCode);
		if(!this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
			super.keyTyped(typedChar, keyCode);
	}
	
	public static List<String> getAllOreDictionaryList()
    {
    	try {
        	Field f = OreDictionary.class.getDeclaredField("idToName");
        	f.setAccessible(true);
			List<String> stringList = (List<String>) f.get(new OreDictionary());
	    	f.setAccessible(false);
	    	ArrayList<String> finalList = new ArrayList<>();
	    	for(String s : stringList)
	    		if(!OreDictionary.getOres(s).isEmpty())
	    			finalList.add(s);
	    	return finalList;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
    	return new ArrayList<>();
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
	}
}
