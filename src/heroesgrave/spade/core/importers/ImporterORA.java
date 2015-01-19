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

package heroesgrave.spade.core.importers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.io.ImageImporter;
import heroesgrave.utils.misc.Metadata;

public class ImporterORA extends ImageImporter
{
	@Override
	public boolean load(File file, Document doc) throws IOException
	{
		//Reading is sipmle; Just load each layer png from the directory and set name from the XML.
		ZipFile zip = new ZipFile(file);
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document xml = db.parse(zip.getInputStream(zip.getEntry("stack.xml")));
			
			Element imageNode = (Element)xml.getElementsByTagName("image").item(0);
			doc.setDimensions(Integer.parseInt(imageNode.getAttribute("w")), Integer.parseInt(imageNode.getAttribute("h")));
			
			NodeList layers = xml.getElementsByTagName("layer"); //Hope it's in order, eh?
			List<Layer> layerList = new ArrayList<>();
			for (int i = layers.getLength()-1; i >= 0; i--) //Do it in reverse.
			{
				Element l = (Element)layers.item(i);
				//Create new layer
				Metadata info = new Metadata();
				if (l.getAttribute("name") != null)
					info.set("name", l.getAttribute("name"));
				
				//System.out.println(l.getAttribute("src"));
				BufferedImage buffer = ImageIO.read(zip.getInputStream(zip.getEntry(l.getAttribute("src"))));
				
				//Ignoring x and y right now
				RawImage ri = RawImage.fromBufferedImage(buffer);
				Layer newLayer = new Layer(doc, ri, info);
				if (!layerList.isEmpty()) layerList.get(layerList.size()-1).addLayer(newLayer);
				layerList.add(newLayer);
			}
			
			doc.setRoot(layerList.get(0));
			
		} catch (ParserConfigurationException | SAXException e)
		{
			e.printStackTrace();
			return false;
		} finally
		{
			zip.close();
		}
		
		return true;
	}

	@Override
	public String getFileExtension()
	{
		return "ora";
	}

	@Override
	public String getDescription()
	{
		return "ORA - Open Raster layered image";
	}

}
