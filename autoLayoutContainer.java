package graphGenerator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Vector;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.GenericDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;


public class autoLayoutContainer {
	
	public static autoLayout cont = new autoLayout();
	public static int ypositioner;
	public static int xpositioner;

	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		cont.populateGraphNodes();
		
		
		
		// Get a DOMImplementation
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();
        String svgNamespaceURI = "http://www.w3.org/2000/svg";

        // Create an instance of org.w3c.dom.Document
        Document document = 
            domImpl.createDocument(svgNamespaceURI, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        
       

        
        
		
		//graphFrame f = new graphFrame();
		//g2d = (Graphics2D) f.graphg;
        
        Rectangle ba = new Rectangle(11, 10, 1780, 1330);
        svgGenerator.setPaint(new Color(242,242,242,255));
        svgGenerator.fill(ba);
        svgGenerator.setPaint(Color.BLACK);
        svgGenerator.draw(ba);
        
		svgGenerator.setFont(new Font("Arial", Font.BOLD, cont.fontsize));
		ypositioner = cont.headingsize;
		xpositioner = 0;
		
		List <Rectangle> rectanglesToBeDrawn;
		
		//Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
		//           0.0f);
		//Composite c2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
		//           1.0f);
		//svgGenerator.setComposite(c2);
		
		rectanglesToBeDrawn = new Vector<Rectangle>();
		for(GraphNodes gn : cont.nodeList){
			List <String> dividedLine = cont.divideLine(gn.text, cont.sizex/3 -cont.leftsize-cont.rightsize);
			int cardDivision = dividedLine.size();
			int neededSpace = cardDivision*(cont.lineheight) + (cardDivision)*cont.interLine;
			if((ypositioner + neededSpace)>cont.sizey){
				ypositioner = cont.headingsize;
				drawDownArrow(svgGenerator, cont.sizey);
				xpositioner = xpositioner + cont.sizex/3;				
			} 
			
			int origypos = ypositioner;
				
			for(String tline : dividedLine){
				svgGenerator.drawString(tline, (xpositioner+cont.leftsize), ypositioner);
				ypositioner = ypositioner + cont.lineheight + cont.interLine;
			}
			
			 Rectangle s;
			 Color boxcolor = new Color(230,230,230,255);
			 int averagepos = (origypos - cont.lineheight) + (cardDivision-1)*cont.lineheight/2 - cont.recsize/2;
			 //svgGenerator.setComposite(c1);   
			 s = new Rectangle(xpositioner + cont.gposition, averagepos, cont.recsize, cont.recsize);
			 drawBs(svgGenerator, xpositioner + cont.gposition - cont.bextension - 10, averagepos + (cont.recsize-cont.bextension)/2, cont.bextension);
			 svgGenerator.setPaint(boxcolor);
			 svgGenerator.drawString("B",xpositioner + cont.gposition - cont.bextension - 5, averagepos + (cont.recsize-cont.bextension)/2 + cont.lineheight - 5 );
			 svgGenerator.setPaint(Color.BLACK);
			 //Color mywhite = new Color(1.0f, 1.0f, 1.0f, 1.0f);
			 //svgGenerator.setPaint(mywhite);
			 //svgGenerator.fill(s);
			 //svgGenerator.setPaint(Color.black);
			 //svgGenerator.setComposite(c2);
			 rectanglesToBeDrawn.add(s);
			 //svgGenerator.draw(s);
			

			ypositioner = ypositioner + cont.betweenSize;
			
			
			
			
		}
		drawDownArrow(svgGenerator, ypositioner);
		int stepNumber = 1;
		Color mywhite = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		Color boxcolor = new Color(02,107,85,255);
		for(Rectangle r : rectanglesToBeDrawn){
			//svgGenerator.setComposite(c1);   
			 
			 svgGenerator.setPaint(mywhite);
			 svgGenerator.fill(r);
			 svgGenerator.setPaint(boxcolor);
			// svgGenerator.setComposite(c2);
			svgGenerator.draw(r);
			if(stepNumber < 10){
				svgGenerator.drawString(Integer.toString(stepNumber), (int)r.getCenterX() - 8, (int)r.getCenterY() + 8);
			} else {
				svgGenerator.drawString(Integer.toString(stepNumber), (int)r.getCenterX() - 12, (int)r.getCenterY() + 8);
			}
			svgGenerator.setPaint(Color.BLACK);
			stepNumber++;
		}
		try{
			 boolean useCSS = true; // we want to use CSS style attribute
			 File generatedfile = new File("M://graphGenerator/generated.svg");
			 FileOutputStream os = new FileOutputStream(generatedfile);
		        Writer out = new OutputStreamWriter(os, "UTF-8");
		        svgGenerator.stream(out, useCSS);

			Thread.currentThread().wait();
		} catch(Exception e) {}
		
		// Finally, stream out SVG to the standard output using UTF-8
        // character to byte encoding
       
	}
	
	private static void drawDownArrow(SVGGraphics2D svgGenerator, int downReach){
		Line2D.Double arrowLine;
		Polygon arrshape;
		int[] polygonXs = { (xpositioner+cont.gposition+ cont.recsize/2)-20, (xpositioner+cont.gposition+ cont.recsize/2), (xpositioner+cont.gposition+ cont.recsize/2)+20, (xpositioner+cont.gposition+ cont.recsize/2)};
		int[] polygonYs = { downReach - 60, downReach - 40, downReach - 60, downReach-20};
		arrshape = new Polygon(polygonXs, polygonYs, polygonXs.length);
		svgGenerator.fill(arrshape);
		arrowLine = new Line2D.Double((double)(xpositioner+cont.gposition+ cont.recsize/2), (double)(cont.headingsize + 10), (double)(xpositioner+cont.gposition+ cont.recsize/2), (double)(downReach - 40));
		svgGenerator.draw(arrowLine);
		svgGenerator.draw(arrshape);
		
	}
	private static void drawBs(SVGGraphics2D svgGenerator, int x, int y, int extension){
		Color boxcolor = new Color(230,230,230,255);
		int rounding = extension/4;
		int[] xPoints = {x+rounding,x+extension-rounding,x+extension,x+extension,x+extension-rounding,x+rounding,x,x};
		int[] yPoints = {y,y,y+rounding,y+extension-rounding,y+extension,y+extension,y+extension-rounding,y+rounding};
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
                xPoints.length);
		path.moveTo(xPoints[0], yPoints[0]);
		path.lineTo(xPoints[1], yPoints[1]);
		Arc2D.Double arc1;
		arc1 = new Arc2D.Double(xPoints[1],yPoints[1],rounding,rounding,90.0, -90.0, Arc2D.OPEN);
		path.append(arc1, true);
		path.lineTo(xPoints[3], yPoints[3]);
		Arc2D.Double arc2;
		arc2 = new Arc2D.Double(xPoints[1],yPoints[3],rounding,rounding,0.0, -90.0, Arc2D.OPEN);
		path.append(arc2, true);
		path.lineTo(xPoints[5], yPoints[5]);
		Arc2D.Double arc3;
		arc3 = new Arc2D.Double(xPoints[6],yPoints[6],rounding,rounding,-90.0, -90.0, Arc2D.OPEN);
		path.append(arc3, true);
		path.lineTo(xPoints[7], yPoints[7]);
		Arc2D.Double arc4;
		arc4 = new Arc2D.Double(xPoints[7],yPoints[0],rounding,rounding,-180.0, -90.0, Arc2D.OPEN);
		path.append(arc4, true);
		path.closePath();
		svgGenerator.setPaint(Color.WHITE);
		svgGenerator.fill(path);
		svgGenerator.setPaint(boxcolor);
		svgGenerator.draw(path);
		svgGenerator.setPaint(Color.BLACK);
		
	}

}

