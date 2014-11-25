// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
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

package heroesgrave.paint.core;

import heroesgrave.paint.core.blend.AlphaTestReplace;
import heroesgrave.paint.core.blend.Replace;
import heroesgrave.paint.core.changes.FillRectChange;
import heroesgrave.paint.core.changes.FlipHorizChange;
import heroesgrave.paint.core.changes.FlipVertChange;
import heroesgrave.paint.core.changes.FloodPathChange;
import heroesgrave.paint.core.changes.GlobalFloodPathChange;
import heroesgrave.paint.core.changes.GreyscaleChange;
import heroesgrave.paint.core.changes.InvertChange;
import heroesgrave.paint.core.changes.LineChange;
import heroesgrave.paint.core.changes.MaskRectChange;
import heroesgrave.paint.core.changes.MoveChange;
import heroesgrave.paint.core.changes.PixelChange;
import heroesgrave.paint.core.changes.RectChange;
import heroesgrave.paint.core.changes.ResizeImageChange;
import heroesgrave.paint.core.changes.SepiaChange;
import heroesgrave.paint.core.changes.TrueGreyscaleChange;
import heroesgrave.paint.core.exporters.ExporterJPEG;
import heroesgrave.paint.core.exporters.ExporterTGA;
import heroesgrave.paint.core.ops.ResizeCanvasOp;
import heroesgrave.paint.core.ops.ResizeImageOp;
import heroesgrave.paint.core.tools.Eraser;
import heroesgrave.paint.core.tools.FloodFill;
import heroesgrave.paint.core.tools.Line;
import heroesgrave.paint.core.tools.Move;
import heroesgrave.paint.core.tools.Rectangle;
import heroesgrave.paint.core.tools.Select;
import heroesgrave.paint.editing.SimpleEffect;
import heroesgrave.paint.io.exporters.ExporterGenericImageIO;
import heroesgrave.paint.plugin.Plugin;
import heroesgrave.paint.plugin.Registrar;

public class CorePlugin extends Plugin
{
	public static void main(String[] args)
	{
		launchPaintWithPlugins(args, false, new CorePlugin());
	}
	
	@Override
	public void load()
	{
		
	}
	
	@Override
	public void register(Registrar registrar)
	{
		registrar.registerTool(new Line("Line"), 'L');
		registrar.registerTool(new Rectangle("Rectangle"), 'R');
		registrar.registerTool(new Select("Select"), 'S');
		registrar.registerTool(new Move("Move"), 'M');
		registrar.registerTool(new Eraser("Eraser"), 'E');
		registrar.registerTool(new FloodFill("Paint Bucket"), 'F');
		
		registrar.registerOperation(new ResizeImageOp("Resize Image"), 'R');
		registrar.registerOperation(new ResizeCanvasOp("Resize Canvas"), null);
		registrar.registerOperation(new SimpleEffect(CorePlugin.class, "Flip Vertically", FlipVertChange.instance), 'V');
		registrar.registerOperation(new SimpleEffect(CorePlugin.class, "Flip Horizontally", FlipHorizChange.instance), 'H');
		
		registrar.registerEffect(new SimpleEffect(CorePlugin.class, "Invert Colour", InvertChange.instance), 'I');
		registrar.registerEffect(new SimpleEffect(CorePlugin.class, "Greyscale", GreyscaleChange.instance), 'G');
		registrar.registerEffect(new SimpleEffect(CorePlugin.class, "True Greyscale", TrueGreyscaleChange.instance), null); // Most of the time linear greyscale is preferred.
		registrar.registerEffect(new SimpleEffect(CorePlugin.class, "Sepia", SepiaChange.instance), null);
		
		registrar.registerSerialiser(LineChange.class);
		registrar.registerSerialiser(RectChange.class);
		registrar.registerSerialiser(FillRectChange.class);
		registrar.registerSerialiser(FloodPathChange.class);
		registrar.registerSerialiser(GlobalFloodPathChange.class);
		registrar.registerSerialiser(PixelChange.class);
		registrar.registerSerialiser(MaskRectChange.class);
		registrar.registerSerialiser(MoveChange.class);
		
		registrar.registerSerialiser(FlipVertChange.class);
		registrar.registerSerialiser(FlipHorizChange.class);
		registrar.registerSerialiser(ResizeImageChange.class);
		
		registrar.registerSerialiser(InvertChange.class);
		registrar.registerSerialiser(TrueGreyscaleChange.class);
		registrar.registerSerialiser(SepiaChange.class);
		
		registrar.registerBlendMode(new Replace());
		registrar.registerBlendMode(new AlphaTestReplace());
		
		registrar.registerExporter(new ExporterGenericImageIO("bmp", "BMP - Microsoft Bitmap Image"));
		registrar.registerExporter(new ExporterGenericImageIO("gif", "GIF - Graphics Interchange Format"));
		registrar.registerExporter(new ExporterJPEG());
		registrar.registerExporter(new ExporterTGA());
	}
}
