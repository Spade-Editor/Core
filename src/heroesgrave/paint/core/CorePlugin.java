package heroesgrave.paint.core;

import heroesgrave.paint.core.blend.AlphaTestReplace;
import heroesgrave.paint.core.blend.Replace;
import heroesgrave.paint.core.changes.FillRectChange;
import heroesgrave.paint.core.changes.FlipVertChange;
import heroesgrave.paint.core.changes.FloodPathChange;
import heroesgrave.paint.core.changes.GreyscaleChange;
import heroesgrave.paint.core.changes.InvertChange;
import heroesgrave.paint.core.changes.LineChange;
import heroesgrave.paint.core.changes.MaskRectChange;
import heroesgrave.paint.core.changes.MoveChange;
import heroesgrave.paint.core.changes.PixelChange;
import heroesgrave.paint.core.changes.RectChange;
import heroesgrave.paint.core.effects.FlipVertical;
import heroesgrave.paint.core.effects.Greyscale;
import heroesgrave.paint.core.effects.Invert;
import heroesgrave.paint.core.tools.Eraser;
import heroesgrave.paint.core.tools.FloodFill;
import heroesgrave.paint.core.tools.Line;
import heroesgrave.paint.core.tools.Move;
import heroesgrave.paint.core.tools.Picker;
import heroesgrave.paint.core.tools.Rectangle;
import heroesgrave.paint.core.tools.Select;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.plugin.Plugin;
import heroesgrave.paint.plugin.Registrar;
import heroesgrave.utils.misc.Metadata;

public class CorePlugin extends Plugin
{
	public static void main(String[] args)
	{
		Plugin plugin = new CorePlugin();
		Metadata info = new Metadata();
		
		info.set("name", "Core");
		info.set("version", Paint.VERSION_STRING); // This will be inserted at compile time.
		info.set("authors", "Paint.JAVA Developers");
		info.set("updated", Paint.RELEASED); // This will be inserted at compile time.
		info.set("description", "Official core plugin containing basic editing tools for Paint.JAVA");
		info.set("min-paint-version", "0.14-Dev");
		
		plugin.setInfo(info);
		Paint.launchWithPlugin(args, plugin);
	}
	
	public void load()
	{
		
	}
	
	public void register(Registrar registrar)
	{
		registrar.registerTool(new Picker("Colour Picker"), 'K');
		registrar.registerTool(new Line("Line"), 'L');
		registrar.registerTool(new Rectangle("Rectangle"), 'R');
		registrar.registerTool(new Select("Select"), 'S');
		registrar.registerTool(new Move("Move"), 'M');
		registrar.registerTool(new Eraser("Eraser"), 'E');
		registrar.registerTool(new FloodFill("Paint Bucket"), 'F');
		
		registrar.registerEffect(new Invert("Invert Colour"), 'I');
		registrar.registerEffect(new FlipVertical("Flip Vertically"), 'V');
		registrar.registerEffect(new Greyscale("Greyscale"), 'G');
		
		registrar.registerSerialiser(LineChange.class);
		registrar.registerSerialiser(FillRectChange.class);
		registrar.registerSerialiser(RectChange.class);
		registrar.registerSerialiser(FloodPathChange.Serial.class);
		registrar.registerSerialiser(PixelChange.class);
		registrar.registerSerialiser(MaskRectChange.class);
		registrar.registerSerialiser(MoveChange.class);
		
		registrar.registerSerialiser(FlipVertChange.class);
		
		registrar.registerSerialiser(InvertChange.class);
		registrar.registerSerialiser(GreyscaleChange.class);
		
		registrar.registerBlendMode(new Replace());
		registrar.registerBlendMode(new AlphaTestReplace());
	}
}
