package com.github.abrarsyed.secretroomsmod.common;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.abrarsyed.secretroomsmod.blocks.TileEntityCamo;
import com.github.abrarsyed.secretroomsmod.network.PacketChangeOwnership;
import com.github.abrarsyed.secretroomsmod.network.PacketManager;
import com.github.abrarsyed.secretroomsmod.network.PacketSyncOwnership;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class OwnershipManager
{
    private static OwnershipManager                 INSTANCE;
    private TIntObjectMap<Map<BlockLocation, UUID>> ownership;
    private static final String                     FILE_NAME = "SecretRooms-ownership.dat";
    private static Logger                           LOG       = LogManager.getLogger();

    protected static void init()
    {
        INSTANCE = new OwnershipManager();
        INSTANCE.ownership = new TIntObjectHashMap<Map<BlockLocation, UUID>>();
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public static void setOwnership(UUID id, BlockLocation loc)
    {
        INSTANCE.ownership.get(loc.dimId).put(loc, id);

        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            PacketManager.sendToDimension(new PacketChangeOwnership(true, loc), loc.dimId);
        }
    }

    public static Map<BlockLocation, UUID> getOwnershipMap(int world)
    {
        return INSTANCE.ownership.get(world);
    }

    public static void clearDimension(int world)
    {
        INSTANCE.ownership.remove(world);
    }

    public static void removeBlock(BlockLocation loc)
    {
        INSTANCE.ownership.get(loc.dimId).remove(loc);

        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            PacketManager.sendToDimension(new PacketChangeOwnership(false, loc), loc.dimId);
        }
    }

    public static boolean isOwner(UUID player, BlockLocation loc)
    {
        UUID gotten = INSTANCE.ownership.get(loc.dimId).get(loc);
        if (gotten == null)
        {
            // not in the map? check if its our kind of Block...
            TileEntity entity = loc.getWorld().getTileEntity(loc.x, loc.y, loc.z);
            if (entity instanceof TileEntityCamo)
            {
                gotten = ((TileEntityCamo) entity).owner;
            }
        }
        
        return equalsUUID(player, gotten);
    }

    private Collection<BlockLocation> getAllForPlayer(UUID player, int dimension)
    {
        LinkedList<BlockLocation> locs = new LinkedList<BlockLocation>();

        for (Entry<BlockLocation, UUID> e : ownership.get(dimension).entrySet())
        {
            if (equalsUUID(player, e.getValue()))
            {
                locs.add(e.getKey());
            }
        }

        return locs;
    }

    @SubscribeEvent
    public void onWorldChange(PlayerChangedDimensionEvent e)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            return;

        PacketManager.sendToPlayer(new PacketSyncOwnership(e.fromDim), e.player);
        PacketManager.sendToPlayer(new PacketSyncOwnership(e.toDim, getAllForPlayer(e.player.getUniqueID(), e.toDim)), e.player);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            return;

        File save = getSaveFile(e.world);
        int dimid = e.world.provider.dimensionId;

        // create data.
        Map<BlockLocation, UUID> map = Maps.newHashMap();
        ownership.put(dimid, map);

        if (!save.exists())
        {
            LOG.info("Ownership data for dimension {} not found ({})", dimid, save);
            return;
        }

        LOG.info("reading ownership data for dimension {} from {}", dimid, save);

        // reading.
        try
        {
            DataInputStream in = getDataIn(save);
            int readDim = in.readInt();

            if (readDim != dimid)
            {
                // had better never happen.
                in.close();
                throw new IllegalArgumentException("WRONG DIMENSION ID! "+readDim +"instead of "+dimid);
            }

            int keys = in.readInt(); // num of keys
            for (int i = 0; i < keys; i++)
            {
                map.put(BlockLocation.readFromData(in, false, dimid), new UUID(in.readLong(), in.readLong()));
            }

            in.close();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        // dont waste bandwidth if its empty....
        if (!map.isEmpty())
        {
            PacketManager.sendToAll(new PacketSyncOwnership(e.world.provider.dimensionId));
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save e)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            return;

        saveWorld(e.world);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload e)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            return;

        saveWorld(e.world);
        ownership.remove(e.world.provider.dimensionId);
        PacketManager.sendToAll(new PacketSyncOwnership(e.world.provider.dimensionId));
    }

    private void saveWorld(World world)
    {
        File save = getSaveFile(world);
        int dimid = world.provider.dimensionId;
        LOG.info("Saving ownership data for dimension {} to {}", dimid, save);

        Map<BlockLocation, UUID> map = ownership.get(dimid);

        try
        {
            DataOutputStream out = getDataOut(save);
            
            // write dimension
            out.writeInt(dimid);
            
            out.writeInt(map.keySet().size()); // num of keys
            for (Entry<BlockLocation, UUID> e : map.entrySet())
            {
                e.getKey().writeToData(out, false); // write locaion
                out.writeLong(e.getValue().getMostSignificantBits());
                out.writeLong(e.getValue().getLeastSignificantBits());
            }

            out.close();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    private File getSaveFile(World world)
    {
        String worldSaveFolder = world.provider.getSaveFolder();
        if (worldSaveFolder == null)
        {
            worldSaveFolder = "";
        }
        else
        {
            worldSaveFolder = "/" + worldSaveFolder;
        }

        return new File(world.getSaveHandler().getWorldDirectory() + worldSaveFolder, FILE_NAME);
    }

    private DataOutputStream getDataOut(File file) throws FileNotFoundException, IOException
    {
        return new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
    }

    private DataInputStream getDataIn(File file) throws FileNotFoundException, IOException
    {
        return new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
    }

    private static final boolean equalsUUID(UUID one, UUID two)
    {
        if (one == null && one == two)
            return true;
        else if (one != null)
            return one.equals(two);
        else if (two != null)
            return two.equals(one);
        else
            return false;
    }
}
