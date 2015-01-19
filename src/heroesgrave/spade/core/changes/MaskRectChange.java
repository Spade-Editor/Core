// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.spade.core.changes;

import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.RawImage.MaskMode;
import heroesgrave.spade.image.change.IEditChange;
import heroesgrave.spade.image.change.IMaskChange;
import heroesgrave.spade.image.change.SerialisedChange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MaskRectChange extends SerialisedChange implements IEditChange, IMaskChange
{
	public short x1, y1, x2, y2;
	private MaskMode mode;
	
	public MaskRectChange()
	{
		
	}
	
	public MaskRectChange(short x1, short y1, short x2, short y2, MaskMode mode)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.mode = mode;
	}
	
	public boolean moveTo(short x, short y)
	{
		if(x2 == x && y2 == y)
			return false;
		this.x2 = x;
		this.y2 = y;
		return true;
	}
	
	@Override
	public void apply(RawImage image)
	{
		if(!image.isMaskEnabled() && (mode == MaskMode.AND || mode == MaskMode.SUB))
			return;
		image.setMaskEnabled(true);
		image.maskRect(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2), mode);
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeInt(mode.ordinal());
		out.writeShort(x1);
		out.writeShort(y1);
		out.writeShort(x2);
		out.writeShort(y2);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		int mode = in.readInt();
		for(MaskMode m : MaskMode.values())
		{
			if(m.ordinal() == mode)
			{
				this.mode = m;
				break;
			}
		}
		x1 = in.readShort();
		y1 = in.readShort();
		x2 = in.readShort();
		y2 = in.readShort();
	}
}
