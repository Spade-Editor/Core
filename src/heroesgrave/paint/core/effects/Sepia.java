package heroesgrave.paint.core.effects;

import heroesgrave.paint.core.changes.SepiaChange;
import heroesgrave.paint.editing.Effect;
import heroesgrave.paint.image.Layer;

public class Sepia extends Effect 
{

	private SepiaChange instance = new SepiaChange();
	
	public Sepia(String name) {
		super(name);
	}

	@Override
	public void perform(Layer layer) {
		layer.addChange(instance);
	}
}
