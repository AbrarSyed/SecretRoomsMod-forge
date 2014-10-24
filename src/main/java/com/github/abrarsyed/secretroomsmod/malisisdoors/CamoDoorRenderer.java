/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.abrarsyed.secretroomsmod.malisisdoors;

import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.preset.FacePreset;
import net.malisis.core.renderer.preset.ShapePreset;
import net.malisis.doors.door.Door;
import net.malisis.doors.door.renderer.DoorRenderer;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author Ordinastie
 * 
 */
public class CamoDoorRenderer extends DoorRenderer
{
	private Face[] presets;

	@Override
	protected void initShapes()
	{
		shape = ShapePreset.Cube();
		shape.setSize(1, 1, Door.DOOR_WIDTH);
		//shape.scale(1, 1, 0.995F);
		shape.storeState();

		presets = new Face[6];
		presets[Door.DIR_NORTH] = FacePreset.North();
		presets[Door.DIR_SOUTH] = FacePreset.South();
		presets[Door.DIR_EAST] = FacePreset.East();
		presets[Door.DIR_WEST] = FacePreset.West();
	}

	@Override
	protected void setup(boolean topBlock)
	{
		super.setup(topBlock);

		Face f = presets[direction];
		shape.getFace(ForgeDirection.NORTH).setParameters(f.getParameters());
		shape.getFace(ForgeDirection.NORTH).getParameters().useBlockBrightness.set(true);

		switch (direction)
		{
			case Door.DIR_NORTH:
				f = presets[Door.DIR_SOUTH];
				break;
			case Door.DIR_SOUTH:
				f = presets[Door.DIR_NORTH];
				break;
			case Door.DIR_EAST:
				f = presets[Door.DIR_WEST];
				break;
			case Door.DIR_WEST:
				f = presets[Door.DIR_EAST];
				break;
		}
		shape.getFace(ForgeDirection.SOUTH).setParameters(f.getParameters());
		shape.getFace(ForgeDirection.SOUTH).getParameters().useBlockBrightness.set(true);

		rp.calculateAOColor.set(true);
		rp.useBlockBrightness.set(false);
		rp.calculateBrightness.set(false);
	}

	@Override
	protected void renderTileEntity()
	{
		super.renderTileEntity();
	}

}
