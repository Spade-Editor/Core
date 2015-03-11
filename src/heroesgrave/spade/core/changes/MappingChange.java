package heroesgrave.spade.core.changes;

import java.io.*;

import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.change.IChange;
import heroesgrave.spade.image.change.IImageChange;
import heroesgrave.spade.io.Serialised;

// TODO: serialization?
public class MappingChange implements IImageChange {
	
	int[][] mappings;
	
	// TODO: is this suitably generalized? color spaces? (HSV, etc.)
	public MappingChange(int[][] channelmappings) {
		mappings = channelmappings;
	}
	
	@Override
	public IChange decode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Serialised encode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public RawImage apply(RawImage image) {
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		
		int[] red = mappings[0], green = mappings[1], blue = mappings[2];
		
		for (int i = 0; i < buffer.length; i++) {
			if (mask == null || mask[i]) {
				int c = buffer[i];
				int r = red[(c >> 16) & 0xFF];
				int g = green[(c >> 8) & 0xFF];
				int b = blue[(c) & 0xFF];
				
				buffer[i] = (c & 0xFF000000) | r << 16 | g << 8 | b;
			}
		}
		
		return image;
	}
	
}
