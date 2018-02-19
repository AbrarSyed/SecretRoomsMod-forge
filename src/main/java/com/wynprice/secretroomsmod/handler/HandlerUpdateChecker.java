package com.wynprice.secretroomsmod.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.wynprice.secretroomsmod.SecretConfig;
import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.versioning.ComparableVersion;

public class HandlerUpdateChecker 
{
	private boolean hasPosted;
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(!hasPosted)
		{
			hasPosted = true;
			if(!SecretConfig.updateChecker)
				return;
			Status status = Status.PENDING;
	        ComparableVersion target = null;
			InputStream con = null;
			String data = null;
			try
			{
				URL url = new URL(SecretRooms5.UPDATE_URL);
				con = url.openStream();
				data = new String(ByteStreams.toByteArray(con), "UTF-8");
		        con.close();
			}
			catch (UnknownHostException h){
				SecretRooms5.LOGGER.warn("Host Cannot be found for update checker. Either the Site is offline or the checker has moved. Please report this");
				return;
			} catch (IOException e) {
				SecretRooms5.LOGGER.warn("The URL is in the wrong format. Please report this");
				return;
			}
	        
	        @SuppressWarnings("unchecked")
	        Map<String, Object> json = new Gson().fromJson(data, Map.class);
	        @SuppressWarnings("unchecked")
	        Map<String, String> promos = (Map<String, String>)json.get("promos");
	        String rec = promos.get(MinecraftForge.MC_VERSION + "-recommended");
	        String lat = promos.get(MinecraftForge.MC_VERSION + "-latest");
	        ComparableVersion current = new ComparableVersion(SecretRooms5.VERSION);
	        if (rec != null)
	        {
	            ComparableVersion recommended = new ComparableVersion(rec);
	            int diff = recommended.compareTo(current);
	            if (diff == 0)
	                status = Status.UP_TO_DATE;
	            else if (diff < 0)
	            {
	                status = Status.AHEAD;
	                if (lat != null)
	                {
	                    ComparableVersion latest = new ComparableVersion(lat);
	                    if (current.compareTo(latest) < 0)
	                    {
	                        status = Status.OUTDATED;
	                        target = latest;
	                    }
	                }
	            }
	            else
	            {
	                status = Status.OUTDATED;
	                target = recommended;
	            }
	        }
	        else if (lat != null)
	        {
	            ComparableVersion latest = new ComparableVersion(lat);
	            if (current.compareTo(latest) < 0)
	            {
	                status = Status.BETA_OUTDATED;
	                target = latest;
	            }
	            else
	                status = Status.BETA;
	        }
	        else
	            status = Status.BETA;
	        if(status == Status.OUTDATED || status == Status.BETA_OUTDATED)
	        {
	        	ITextComponent componant = new TextComponentTranslation("update.version", SecretRooms5.MODNAME, SecretRooms5.VERSION, target)
	        			.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, (String)json.get("homepage"))));
	        	componant.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("update.hover", (String)json.get("homepage"))));
	        	event.player.sendMessage(componant);

	        }
	        else
	        	SecretRooms5.LOGGER.info("Update checker returned: " + status);
			
		}
	}
}