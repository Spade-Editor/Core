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

package heroesgrave.spade.core.exporters;

import heroesgrave.spade.image.Document;
import heroesgrave.spade.io.ImageExporter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * JPEG Exporter.<br>
 * This exporter was made, because there was some kind of 'corruption' effect going on,
 * if the image had transparency. This special exporter fixes the corruption issue.
 * 
 * @author Longor1996
 **/
public class ExporterJPEG extends ImageExporter
{
	@Override
	public String getFileExtension()
	{
		return "jpeg";
	}
	
	public BufferedImage clean(BufferedImage imageIn)
	{
		// 'Color Corruption' Fix
		BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = imageOut.getGraphics();
		g.drawImage(imageIn, 0, 0, null);
		
		return imageOut;
	}
	
	@Override
	public String getDescription()
	{
		return "JPEG - JPEG File Interchange Format";
	}
	
	@Override
	public void save(Document doc, File destination) throws IOException
	{
		writeImage(clean(doc.getRenderedImage()), getFileExtension().toUpperCase(), destination.getAbsolutePath());
	}
}
