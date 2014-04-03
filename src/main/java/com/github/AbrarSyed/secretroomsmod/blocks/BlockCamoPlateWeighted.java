package com.github.AbrarSyed.secretroomsmod.blocks;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCamoPlateWeighted extends BlockCamoPlate
{
    private int maxWeight;

    public BlockCamoPlateWeighted(int weight)
    {
        super(false);
        maxWeight = weight;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        if (maxWeight > 100)
            blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_PLATE_IRON);
        else
            blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_PLATE_GOLD);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected int getCurrentWeight(World world, int x, int y, int z)
    {
        int l = 0;
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, getSensetiveAABB(x, y, z));

        if (list == null)
            return 0;

        Iterator<EntityItem> it = list.iterator();

        while (it.hasNext())
        {
            EntityItem entityitem = it.next();
            l += entityitem.getEntityItem().stackSize;

            if (l >= maxWeight)
            {
                break;
            }
        }

        if (l <= 0)
            return 0;

        float f = (float) Math.min(maxWeight, l) / (float) maxWeight;
        return MathHelper.ceiling_float_int(f * 15.0F);
    }

    @Override
    protected int getPowerFromMeta(int meta)
    {
        return meta;
    }

    @Override
    protected int getMetaFromWeight(int weight)
    {
        return weight;
    }

}
