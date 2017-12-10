import java.awt.Rectangle;

public class Ratiolizer {
	double x,
		   y;
	int    maxX,
		   maxY;
	
	
	public Ratiolizer(int xTotal,int yTotal, int xGrid,int yGrid){
		
		x=((double)xTotal)/((double)xGrid);
		y=((double)yTotal)/((double)yGrid);
		
	}
	
	public Rectangle locate(int xStart,int yStart,int xLength,int yLength){
		
		
		return new Rectangle(( (int)Math.round(xStart*x) ),
							 ( (int)Math.round(yStart*y) ),
							 ( (int)Math.round(xLength*x) ),
							 ( (int)Math.round(yLength*y) )
							);
		
	}
	
public Rectangle locate(int xStart,	int yStart, int xLength, int yLength,
						int xStartModifier,
						int yStartModifier,
						int xLengthModifier,
						int yLengthModifier){
		
		
		return new Rectangle(( (int)Math.round(xStart*x) )+xStartModifier,
							 ( (int)Math.round(yStart*y) )+yStartModifier,
							 ( (int)Math.round(xLength*x) )+xLengthModifier,
							 ( (int)Math.round(yLength*y) )+yLengthModifier
							);
		
	}
	
	
	
	
	
	
	
	
	
	
}
