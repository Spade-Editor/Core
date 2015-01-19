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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.io.ImageExporter;

public class ExporterORA extends ImageExporter
{
	private ZipOutputStream zipOut;
	
	@Override
	public String getFileExtension()
	{
		return "ora";
	}

	@Override
	public void save(Document document, File destination) throws IOException
	{
		zipOut = new ZipOutputStream(new FileOutputStream(destination));
		
		String mimetype = "image/openraster";
		
		//Must write mimetype string with STORED level compression!
		zipOut.setLevel(ZipOutputStream.STORED);
        //zipOut.setMethod(ZipOutputStream.STORED);
		zipOut.putNextEntry(new ZipEntry("mimetype"));
		zipOut.write(mimetype.getBytes());
		zipOut.closeEntry();
		
		//Compress the rest of the entries
		zipOut.setLevel(Deflater.DEFAULT_COMPRESSION);
		//zipOut.setMethod(ZipOutputStream.DEFLATED);
		zipOut.putNextEntry(new ZipEntry("data/"));
		zipOut.closeEntry();
		zipOut.putNextEntry(new ZipEntry("Thumbnails/"));
		zipOut.closeEntry();
		
		//Write all layers to the data/ folder.
		for (Layer l : document.getFlatMap())
		{
			writeLayer(l);
		}
		
		//Write the "merged image" (Just a composite of all the layers.)
		zipOut.putNextEntry(new ZipEntry("mergedimage.png"));
		writePNG(document.getRenderedImage());
		zipOut.closeEntry();
		
		//Write the thumbnail (It's mandatory for some reason.)
		zipOut.putNextEntry(new ZipEntry("Thumbnails/thumbnail.png"));
		writePNG(resize(document.getRenderedImage(), 256, 256));
		zipOut.closeEntry();
		
		//Write the XML descriptor. This could be refactored into multiple files.
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder xmlBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document dom = xmlBuilder.newDocument();
			
			Element root = dom.createElement("image");
			root.setAttribute("version", "0.0.3");
			root.setAttribute("w", Integer.toString(document.getWidth()));
			root.setAttribute("h", Integer.toString(document.getHeight()));
			
			Element baseStack = dom.createElement("stack");
			
			List<Layer> flatMap = document.getFlatMap();
			for (int i = flatMap.size()-1; i >= 0; i--)
			{
				Layer l = flatMap.get(i);
				Element newLayer = dom.createElement("layer");
				newLayer.setAttribute("name", l.getMetadata().get("name"));
				newLayer.setAttribute("src", "data/layer" + l.getLevel() + ".png");
				baseStack.appendChild(newLayer);
			}
			root.appendChild(baseStack);
			dom.appendChild(root);
			
			//Write it to the zip
			Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            ByteArrayOutputStream memStream = new ByteArrayOutputStream();
            tr.transform(new DOMSource(dom), new StreamResult(memStream));
			
            zipOut.putNextEntry(new ZipEntry("stack.xml"));
            zipOut.write(memStream.toByteArray());
            zipOut.closeEntry();
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e)
		{
			e.printStackTrace();
		}
		
		//Finalize the zip file.
		zipOut.flush();
		zipOut.close();
	}
	
	private void writeLayer(Layer l) throws IOException
	{
		zipOut.putNextEntry(new ZipEntry("data/layer" + l.getLevel() + ".png"));
		writePNG(l.getBufferedImage());
		zipOut.closeEntry();
	}
	
	private void writePNG(BufferedImage image) throws IOException
	{
		ByteArrayOutputStream memStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", memStream);
		zipOut.write(memStream.toByteArray());
	}
	
	private static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  

	@Override
	public String getDescription()
	{
		return "ORA - Open Raster layered image";
	}

}
