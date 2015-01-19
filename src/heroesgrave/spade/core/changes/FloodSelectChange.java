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
import java.util.Arrays;

public class FloodSelectChange extends SerialisedChange implements IEditChange, IMaskChange
{
	private short x, y;
	private MaskMode mode;
	private boolean global;
	
	public FloodSelectChange(short x, short y, MaskMode mode, boolean global)
	{
		this.x = x;
		this.y = y;
		this.mode = mode;
		this.global = global;
	}
	
	public boolean moveTo(short x, short y)
	{
		if(this.x == x && this.y == y)
			return false;
		this.x = x;
		this.y = y;
		return true;
	}
	
	// TODO: move to RawImage instance method
	// scan line flood fill as described in Wikipedia
	public void apply(RawImage image)
	{
		final int iw = image.width;
		
		if(x < 0 | y < 0 || x >= iw || y >= image.height)
			return;
		
		int[] buffer = image.borrowBuffer();
		if(!image.isMaskEnabled() && (mode == MaskMode.AND || mode == MaskMode.SUB))
			return;
		if(mode == MaskMode.REP)
			image.fillMask(MaskMode.SUB);
		image.setMaskEnabled(true);
		boolean[] mask = image.borrowMask();
		int targetColor = buffer[x + y * iw];
		
		if(global)
		{
			for(int i = 0; i < buffer.length; i++)
			{
				if(buffer[i] == targetColor)
				{
					mask[i] = mode.transform(mask[i]);
				}
				else if(mode == MaskMode.AND)
				{
					mask[i] = false;
				}
			}
		}
		else
		{
			boolean[] visited = new boolean[mask.length];
			
			boolean[] newMask = null;
			switch(mode)
			{
				case ADD:
				case SUB:
				case XOR:
					newMask = Arrays.copyOf(mask, mask.length);
					break;
				case REP:
				case AND:
					newMask = new boolean[mask.length];
					break;
			}
			int[] stack = new int[1024];
			int head = -1;
			
			stack[++head] = x + y * iw;
			
			// XXX A bit of hacking was done here.
			// It may not be an optimal solution.
			// A different algorithm is probably required.
			while(head >= 0)
			{
				int n = stack[head--];
				if(buffer[n] == targetColor) // mask predicate is guaranteed here
				{
					final int ny = n / iw;
					final int nyo = ny * iw;
					final int nxl = nyo + iw;
					int w = n, e = n;
					// scan out on each side of current pixel
					while(w >= nyo && buffer[w] == targetColor && !visited[w])
						--w;
					while(e < nxl && buffer[e] == targetColor && !visited[e])
						++e;
					// fill in between
					w += 1;
					
					for(int i = w; i < e; ++i)
					{
						newMask[i] = mode.transform(mask[i]);
						visited[i] = true;
					}
					
					w -= iw;
					e -= iw;
					
					if(ny > 0)
						for(int i = w; i < e; ++i)
							if(buffer[i] == targetColor && !visited[i])
							{
								stack[++head] = i;
								if(head == stack.length - 1)
									stack = Arrays.copyOf(stack, stack.length * 2);
							}
					
					w += iw * 2;
					e += iw * 2;
					
					if(ny < image.height - 1)
						for(int i = w; i < e; ++i)
							if(buffer[i] == targetColor && !visited[i])
							{
								stack[++head] = i;
								if(head == stack.length - 1)
									stack = Arrays.copyOf(stack, stack.length * 2);
							}
				}
			}
			
			image.setMask(newMask);
		}
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeInt(mode.ordinal());
		out.writeShort(x);
		out.writeShort(y);
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
		x = in.readShort();
		y = in.readShort();
	}
}
