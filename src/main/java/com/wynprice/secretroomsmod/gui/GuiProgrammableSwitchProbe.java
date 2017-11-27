package com.wynprice.secretroomsmod.gui;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.gui.slots.SlotItemStuck;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketUpdateProbe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

public class GuiProgrammableSwitchProbe extends GuiContainer
{
	
	private GuiButton buttonExit;
	private GuiTextField textInput;
	private ItemStack stack;	
	private HashMap<Point, IBlockState> nonItemBlockMap = new HashMap<>();
	
	private SelectedOutput output;
	
	public GuiProgrammableSwitchProbe(ItemStack itemStack) 
	{
		super(new ContainerProgrammableProbe());
		stack = itemStack;
	}

	@Override
	public void initGui() {
		this.xSize = this.width;
		this.ySize = this.height;
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
		ArrayList<IBlockState> nonItemBlocks = new ArrayList<>();
		ArrayList<ItemStack> renderStacks = new ArrayList<>();
		Block overblock = Block.getBlockFromName(textInput.getText());
		if(overblock != null)
		{
			NonNullList<ItemStack> items = NonNullList.create();
			overblock.getSubBlocks(CreativeTabs.SEARCH, items);
			for(ItemStack stack : items)
				if(stack.isEmpty())
				{
					nonItemBlocks.add(overblock.getStateFromMeta(stack.getMetadata()));
					renderStacks.add(ItemStack.EMPTY);
				}
				else
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
							renderStacks.add(stack1);
					}
					else
						renderStacks.add(stack);
				}
		}
		boolean flag = false;
		int amount = 0;
		if(renderStacks.size() == 1 && !renderStacks.get(0).isEmpty())
			output = new SelectedOutput(renderStacks.get(0));
		else if(nonItemBlocks.size() == 1)
			output = new SelectedOutput(nonItemBlocks.get(0));
		inventorySlots.inventorySlots.clear();
		nonItemBlockMap.clear();
		ListIterator<IBlockState> iterator = nonItemBlocks.listIterator();
		int total = renderStacks.size();
		while(!flag)
			flag = renderStacks(renderStacks, iterator, total, amount++);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Point point = new Point((this.width / 2) - 7, (this.height / 2) + 50);
		if(output != null)
		{
			if(output.isStack())
				this.inventorySlots.inventorySlots.add(new SlotItemStuck(output.getStack(), point.x, point.y));
			else
			{
				this.inventorySlots.inventorySlots.add(new SlotItemStuck(ItemStack.EMPTY, point.x, point.y));
        		nonItemBlockMap.put(point, output.getState());
        		this.zLevel = 1;
				this.drawTexturedModalRect(point.x, point.y, output.getSprite(), 16, 16);
			}
		}
		this.zLevel = 0;
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/container/generic_54.png"));
    	this.drawTexturedModalRect(point.x - 1, point.y - 1, 7, 17, 18, 18);
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		if(this.getSlotUnderMouse() != null && this.getSlotUnderMouse().getStack().isEmpty() && nonItemBlockMap.get(new Point(this.getSlotUnderMouse().xPos, this.getSlotUnderMouse().yPos)) != null)
		{
			IBlockState state = nonItemBlockMap.get(new Point(this.getSlotUnderMouse().xPos, this.getSlotUnderMouse().yPos));
			ArrayList<String> list = new ArrayList<>();
			list.add(state.getBlock().getLocalizedName());
			list.add(TextFormatting.DARK_GRAY + ((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock())).toString());
			this.drawHoveringText(list, mouseX, mouseY);
			String s = state.getBlock().getLocalizedName();
		}
		
		if(mouseX - ((this.width / 2) - 170) >= 318 && mouseX - ((this.width / 2) - 170) <= 332 && mouseY - ((this.height / 2) - 100) >= 3 && mouseY - ((this.height / 2) - 100) <= 19)
			this.drawHoveringText(Arrays.asList(new TextComponentTranslation("gui.programmable.info").getFormattedText().split("<br>")), mouseX, mouseY);
	}	
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
            if(this.isPointInRegion(this.inventorySlots.inventorySlots.get(i).xPos, this.inventorySlots.inventorySlots.get(i).yPos, 16, 16, mouseX, mouseY))
            	if(nonItemBlockMap.get(new Point(this.inventorySlots.inventorySlots.get(i).xPos, this.inventorySlots.inventorySlots.get(i).yPos)) != null)
            		this.output = new SelectedOutput(nonItemBlockMap.get(new Point(this.inventorySlots.inventorySlots.get(i).xPos, this.inventorySlots.inventorySlots.get(i).yPos)));
            	else	            	
	            	this.output = new SelectedOutput(this.inventorySlots.inventorySlots.get(i).getStack());
	}
	
	private boolean renderStacks(ArrayList<ItemStack> renderStacks, ListIterator<IBlockState> nonItemBlocks, int total, int amount)
	{
		boolean flag = renderStacks.size() > 16;
        int renderamount = flag ? 16 : renderStacks.size();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        for(int i = 0; i < renderamount; i ++)
        {
        	Point point = new Point((this.width / 2) - (flag || renderamount % 2== 0 ? -3 : 7) - (((renderamount / 2) - (i % 16)) * 20), this.height / 2 - 20 - (10 * (4 - MathHelper.clamp(Math.floorDiv(total, 16), 0, 4))) - (amount*25));
        	if(renderStacks.get(i).isEmpty() && nonItemBlocks.hasNext())
        	{
	        	inventorySlots.inventorySlots.add(new SlotItemStuck(ItemStack.EMPTY, point.x, point.y));
        		IBlockState state = nonItemBlocks.next();
        		this.zLevel = 1;
        		nonItemBlockMap.put(point, state);
        		this.drawTexturedModalRect(point.x, point.y, Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state), 16, 16);
        	}
        	else
	        	inventorySlots.inventorySlots.add(new SlotItemStuck(renderStacks.get(i), point.x, point.y));
        	this.zLevel = 0;
    		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/container/generic_54.png"));
        	this.drawTexturedModalRect(point.x - 1, point.y - 1, 7, 17, 18, 18);
        }
        ArrayList<ItemStack> newStackList = new ArrayList<>();
        if(flag)
        	for(int i = 16; i < renderStacks.size(); i++)
        		newStackList.add(renderStacks.get(i));
        renderStacks.clear();
        renderStacks.addAll(newStackList);
        if(amount == 3)
        	return true;
        return !flag;
	}
	
	@Override
	public void drawDefaultBackground() 
	{
		super.drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(SecretRooms5.MODID, "textures/gui/blank_slate.png"));
		this.drawTexturedModalRect((this.width / 2) - 170,(this.height / 2) - 100, 0, 0, 239, 197);
		this.drawTexturedModalRect((this.width / 2) + 69,( this.height / 2) - 100, 159, 0, 97, 197);
		this.drawTexturedModalRect((this.width / 2) - 170,( this.height / 2) + 97, 0, 253, 253, 3);
		this.drawTexturedModalRect((this.width / 2) + 83,( this.height / 2) + 97, 173, 253, 83, 3);

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
		IBlockState outputState = Blocks.AIR.getDefaultState();
		if(output != null)
			if(output.isStack())
				outputState = Block.getBlockFromItem(output.getStack().getItem()).getStateForPlacement(Minecraft.getMinecraft().world, BlockPos.ORIGIN, EnumFacing.UP, 0f, 0f, 0f, 
						output.getStack().getMetadata(), Minecraft.getMinecraft().player, EnumHand.MAIN_HAND);
			else
				outputState = output.getState();
		if(!this.stack.hasTagCompound())
			this.stack.setTagCompound(new NBTTagCompound());
		ItemStackHandler handler = new ItemStackHandler(1);
		if(output != null)
			handler.setStackInSlot(0, output.isStack() ? output.getStack() : ItemStack.EMPTY);
		stack.getTagCompound().setTag("hit_itemstack", handler.serializeNBT());
		stack.getTagCompound().setString("hit_block", outputState.getBlock().getRegistryName().toString());
		stack.getTagCompound().setInteger("hit_meta", outputState.getBlock().getMetaFromState(outputState));
		SecretNetwork.sendToServer(new MessagePacketUpdateProbe(stack.getTagCompound()));
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
	
	private class SelectedOutput
	{
		private IBlockState state;
		
		private ItemStack stack;
		
		public SelectedOutput(ItemStack stack) {
			this.stack = stack;
		}
		
		public SelectedOutput(IBlockState state) 
		{
			this.state = state;
		}
		
		public boolean isStack() {
			return stack != null;
		}
		
		public TextureAtlasSprite getSprite() {
			return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
		}
		
		public ItemStack getStack() {
			return stack;
		}
		
		public IBlockState getState() {
			return state;
		}
	}
}
