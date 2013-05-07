package mods.secretroomsmod.common;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialFakeAir extends Material
{

	public MaterialFakeAir()
	{
		super(MapColor.airColor);
	}

	@Override
	public boolean isSolid()
	{
		return false;
	}

	/**
	 * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
	 */
	@Override
	public boolean getCanBlockGrass()
	{
		return false;
	}

	/**
	 * Returns if this material is considered solid or not
	 */
	@Override
	public boolean blocksMovement()
	{
		return false;
	}

	/**
	 * Indicate if the material is opaque
	 */
	@Override
	public boolean isOpaque()
	{
		return false;
	}

}
