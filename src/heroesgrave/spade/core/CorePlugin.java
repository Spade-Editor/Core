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

package heroesgrave.spade.core;

import heroesgrave.spade.core.blend.AlphaTestReplace;
import heroesgrave.spade.core.blend.Replace;
import heroesgrave.spade.core.changes.FillRectChange;
import heroesgrave.spade.core.changes.FlipHorizChange;
import heroesgrave.spade.core.changes.FlipVertChange;
import heroesgrave.spade.core.changes.FloodPathChange;
import heroesgrave.spade.core.changes.FloodSelectChange;
import heroesgrave.spade.core.changes.GlobalFloodPathChange;
import heroesgrave.spade.core.changes.GreyscaleChange;
import heroesgrave.spade.core.changes.InvertChange;
import heroesgrave.spade.core.changes.LineChange;
import heroesgrave.spade.core.changes.MaskRectChange;
import heroesgrave.spade.core.changes.MoveChange;
import heroesgrave.spade.core.changes.PixelChange;
import heroesgrave.spade.core.changes.RectChange;
import heroesgrave.spade.core.changes.ResizeImageChange;
import heroesgrave.spade.core.changes.SepiaChange;
import heroesgrave.spade.core.changes.TrueGreyscaleChange;
import heroesgrave.spade.core.effects.GreyscaleEffect;
import heroesgrave.spade.core.exporters.ExporterJPEG;
import heroesgrave.spade.core.exporters.ExporterORA;
import heroesgrave.spade.core.exporters.ExporterSPD;
import heroesgrave.spade.core.exporters.ExporterTGA;
import heroesgrave.spade.core.importers.ImporterORA;
import heroesgrave.spade.core.importers.ImporterSPD;
import heroesgrave.spade.core.ops.ResizeCanvasOp;
import heroesgrave.spade.core.ops.ResizeImageOp;
import heroesgrave.spade.core.tools.Eraser;
import heroesgrave.spade.core.tools.FloodFill;
import heroesgrave.spade.core.tools.FloodSelect;
import heroesgrave.spade.core.tools.Line;
import heroesgrave.spade.core.tools.Move;
import heroesgrave.spade.core.tools.Rectangle;
import heroesgrave.spade.core.tools.Select;
import heroesgrave.spade.editing.SimpleEffect;
import heroesgrave.spade.io.exporters.ExporterGenericImageIO;
import heroesgrave.spade.plugin.Plugin;
import heroesgrave.spade.plugin.Registrar;

public class CorePlugin extends Plugin
{
	public static void main(String[] args)
	{
		launchSpadeWithPlugins(args, new CorePlugin());
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
		registrar.registerTool(new Eraser("Eraser"), 'E');
		registrar.registerTool(new Select("Select"), 'S');
		registrar.registerTool(new FloodSelect("Magic Wand"), 'Z');
		registrar.registerTool(new FloodFill("Paint Bucket"), 'F');
		registrar.registerTool(new Move("Move"), 'M');
		
		registrar.registerOperation(new ResizeImageOp("Resize Image"), 'R');
		registrar.registerOperation(new ResizeCanvasOp("Resize Canvas"), null);
		registrar.registerOperation(new SimpleEffect(CorePlugin.class, "Flip Vertically", FlipVertChange.instance), 'V');
		registrar.registerOperation(new SimpleEffect(CorePlugin.class, "Flip Horizontally", FlipHorizChange.instance), 'H');
		
		registrar.registerEffect(new GreyscaleEffect("Greyscale"), 'G');
		registrar.registerEffect(new SimpleEffect(CorePlugin.class, "Invert Colour", InvertChange.instance), 'I');
		registrar.registerEffect(new SimpleEffect(CorePlugin.class, "Sepia", SepiaChange.instance), null);
		
		registrar.registerSerialiser(LineChange.class);
		registrar.registerSerialiser(RectChange.class);
		registrar.registerSerialiser(FillRectChange.class);
		registrar.registerSerialiser(FloodPathChange.class);
		registrar.registerSerialiser(GlobalFloodPathChange.class);
		registrar.registerSerialiser(FloodSelectChange.class);
		registrar.registerSerialiser(PixelChange.class);
		registrar.registerSerialiser(MaskRectChange.class);
		registrar.registerSerialiser(MoveChange.class);
		
		registrar.registerSerialiser(FlipVertChange.class);
		registrar.registerSerialiser(FlipHorizChange.class);
		registrar.registerSerialiser(ResizeImageChange.class);
		
		registrar.registerSerialiser(InvertChange.class);
		registrar.registerSerialiser(GreyscaleChange.class);
		registrar.registerSerialiser(TrueGreyscaleChange.class);
		registrar.registerSerialiser(SepiaChange.class);
		
		registrar.registerBlendMode(new Replace());
		registrar.registerBlendMode(new AlphaTestReplace());
		
		registrar.registerExporter(new ExporterGenericImageIO("bmp", "BMP - Microsoft Bitmap Image"));
		registrar.registerExporter(new ExporterGenericImageIO("gif", "GIF - Graphics Interchange Format"));
		registrar.registerExporter(new ExporterJPEG());
		registrar.registerExporter(new ExporterTGA());
		registrar.registerExporter(new ExporterSPD());
		registrar.registerExporter(new ExporterORA());
		
		registrar.registerImporter(new ImporterSPD());
		registrar.registerImporter(new ImporterORA());
	}
}
