// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
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

package heroesgrave.spade.core.exporters;

import heroesgrave.spade.gui.dialogs.ProgressDialog;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.io.ImageExporter;
import heroesgrave.utils.misc.Metadata;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

public class ExporterSPD extends ImageExporter
{
	@Override
	public String getFileExtension()
	{
		return "spd";
	}
	
	@Override
	public String getDescription()
	{
		return "SPD - Spade experimental file format (supports layers)";
	}
	
	@Override
	public void save(Document doc, File destination) throws IOException
	{
		GZIPOutputStream zip = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
		DataOutputStream out = new DataOutputStream(zip);
		
		out.writeInt(doc.getWidth());
		out.writeInt(doc.getHeight());
		out.writeInt(doc.getFlatMap().size());
		
		writeMetadata(out, doc.getMetadata());
		
		int c = doc.getWidth() * doc.getHeight() * doc.getFlatMap().size();
		ProgressDialog dialog = new ProgressDialog("Saving...", "Saving Image...", c + 1);
		
		writeLayer(out, doc.getRoot(), dialog);
		out.flush();
		dialog.setMessage("Compressing...");
		zip.finish();
		zip.flush();
		out.close();
		
		dialog.close();
	}
	
	public void writeLayer(DataOutputStream out, Layer layer, ProgressDialog dialog) throws IOException
	{
		writeMetadata(out, layer.getMetadata());
		
		RawImage image = layer.getImage();
		int[] buffer = image.borrowBuffer();
		
		int start = dialog.getValue();
		
		// Why does java not have a nice way to write an int[]?
		// Or at least a way to convert an int[] to a byte[] for I/O?
		for(int i = 0; i < buffer.length; i++)
		{
			out.writeInt(buffer[i]);
			
			if(i % 128 == 0)
			{
				dialog.setValue(start + i);
			}
		}
		dialog.setValue(start + buffer.length);
		
		int count = layer.getChildCount();
		
		out.writeInt(count);
		
		for(int i = 0; i < count; i++)
		{
			writeLayer(out, (Layer) layer.getChildAt(i), dialog);
		}
	}
	
	public void writeMetadata(DataOutputStream out, Metadata data) throws IOException
	{
		out.writeInt(data.size());
		for(Entry<String, String> e : data.getEntries())
		{
			out.writeUTF(e.getKey());
			out.writeUTF(e.getValue());
		}
	}
}
