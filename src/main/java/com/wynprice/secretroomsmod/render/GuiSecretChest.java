package com.wynprice.secretroomsmod.render;

import com.wynprice.secretroomsmod.blocks.SecretChest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.ItemStackHandler;

public class GuiSecretChest extends GuiContainer
{
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    
    private final BlockPos pos;

    public GuiSecretChest(BlockPos pos, ItemStackHandler handler, EntityPlayer player)
    {
        super(new ContainerSecretChest(pos, handler, player));
        this.pos = pos;
        this.allowUserInput = false;
        this.ySize = 138;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(new TextComponentTranslation("gui.secret_chest.name").getUnformattedText(), 8, 6, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, 3 * 18 + 17);
        this.drawTexturedModalRect(i, j + 3 * 18 + 17, 0, 126, this.xSize, 96);
    }
}
