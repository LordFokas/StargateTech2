package lordfokas.naquadria.tileentity.facing;


public abstract class FaceColorFilter implements IFaceColorFilter{
	public static final FaceColorFilter INPUT = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return color.isInput();
		}
	};
	
	public static final FaceColorFilter OUTPUT = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return color.isOutput();
		}
	};
	
	public static final FaceColorFilter MAIN = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return color.isMain();
		}
	};
	
	public static final FaceColorFilter PRIMARY = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return color.isPrimary();
		}
	};
	
	public static final FaceColorFilter SECONDARY = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return color.isSecondary();
		}
	};
	
	public static final FaceColorFilter UNIVERSAL = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return color.isUniversal();
		}
	};
	
	public static final FaceColorFilter COLORED = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return color.isColored();
		}
	};
	
	public static final FaceColorFilter VOID = new FaceColorFilter(){
		@Override public boolean doesColorMatch(FaceColor color) {
			return !color.isColored();
		}
	};
	
	public static class MatchColors extends FaceColorFilter{
		private FaceColor[] colors;
		
		public MatchColors(FaceColor ... colors){
			this.colors = colors;
		}
		
		@Override
		public boolean doesColorMatch(FaceColor color) {
			for(FaceColor c : colors)
				if(c == color) return true;
			return false;
		}
	}
}
