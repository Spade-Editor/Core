package heroesgrave.paint.core;

import heroesgrave.paint.core.blend.AlphaTestReplace;
import heroesgrave.paint.core.blend.Replace;
import heroesgrave.paint.core.changes.*;
import heroesgrave.paint.core.effects.*;
import heroesgrave.paint.core.ops.ResizeCanvasOp;
import heroesgrave.paint.core.ops.ResizeImageOp;
import heroesgrave.paint.core.tools.Eraser;
import heroesgrave.paint.core.tools.FloodFill;
import heroesgrave.paint.core.tools.Line;
import heroesgrave.paint.core.tools.Move;
import heroesgrave.paint.core.tools.Rectangle;
import heroesgrave.paint.core.tools.Select;
import heroesgrave.paint.plugin.Plugin;
import heroesgrave.paint.plugin.Registrar;

public class CorePlugin extends Plugin
{
	public static void main(String[] args)
	{
		launchPaintWithPlugins(args, new CorePlugin());
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
		registrar.registerOperation(new FlipVertical("Flip Vertically"), 'V');
		
		registrar.registerEffect(new Invert("Invert Colour"), 'I');
		registrar.registerEffect(new Greyscale("Greyscale"), 'G');
		registrar.registerEffect(new Sepia("Sepia"), 'S');
		
		registrar.registerSerialiser(LineChange.class);
		registrar.registerSerialiser(FillRectChange.class);
		registrar.registerSerialiser(RectChange.class);
		registrar.registerSerialiser(FloodPathChange.Serial.class);
		registrar.registerSerialiser(PixelChange.class);
		registrar.registerSerialiser(MaskRectChange.class);
		registrar.registerSerialiser(MoveChange.class);
		
		registrar.registerSerialiser(FlipVertChange.class);
		registrar.registerSerialiser(ResizeImageChange.class);
		registrar.registerSerialiser(ResizeCanvasChange.class);
		
		registrar.registerSerialiser(InvertChange.class);
		registrar.registerSerialiser(GreyscaleChange.class);
		registrar.registerSerialiser(SepiaChange.class);
		
		registrar.registerBlendMode(new Replace());
		registrar.registerBlendMode(new AlphaTestReplace());
	}
}
