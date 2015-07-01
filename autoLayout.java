package graphGenerator;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class autoLayout {
	public List <GraphNodes> nodeList;
	int sizex;
	int sizey;
	int betweenSize;
	int interLine;
	int lineheight;
	int headingsize;
	int leftsize;
	int rightsize;
	int recsize;
	int bextension;
	int gposition;
	int fontsize;
	AffineTransform affinetransform;
	FontRenderContext frc;
	Font font;
	
	public autoLayout(){
		affinetransform = new AffineTransform();
		frc = new FontRenderContext(affinetransform, true,
				true);

		font = new Font("Arial", Font.BOLD, 21);
		lineheight = (int) (font.getStringBounds("testsample", frc).getHeight());
	}
	
	
	public void populateGraphNodes() {
		nodeList = new Vector<GraphNodes>();

		try {
			final InputStream file = new FileInputStream("M://graphGenerator/graphdef.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			nodeList = parseXMLDescription(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private List<GraphNodes> parseXMLDescription(Node doc) {
		NamedNodeMap setOfattr;
		Node attr;
		List<GraphNodes> ret = new Vector<GraphNodes>();
		
		if (doc.getNodeType() == Node.ELEMENT_NODE) {

				Element el = (Element) doc;
				String BuildingBlockType = doc.getNodeName();

				if (BuildingBlockType.equals("graph")) {
					setOfattr = el.getAttributes();
					attr = setOfattr.getNamedItem("xsize");
					if (attr != null) {
						String lxsize = attr.getNodeValue();

						sizex = Integer.parseInt(lxsize);
					} else {
						sizex = 0;
					}
					
					attr = setOfattr.getNamedItem("ysize");
					if (attr != null) {
						String lysize = attr.getNodeValue();

						sizey = Integer.parseInt(lysize);
					} else {
						sizey = 0;
					}
					
					attr = setOfattr.getNamedItem("headingsize");
					if (attr != null) {
						String attval = attr.getNodeValue();

						headingsize = Integer.parseInt(attval);
					} else {
						headingsize = 0;
					}
					
					attr = setOfattr.getNamedItem("leftsize");
					if (attr != null) {
						String attval = attr.getNodeValue();

						leftsize = Integer.parseInt(attval);
					} else {
						leftsize = 0;
					}
					
					attr = setOfattr.getNamedItem("rightsize");
					if (attr != null) {
						String attval = attr.getNodeValue();

						rightsize = Integer.parseInt(attval);
					} else {
						rightsize = 0;
					}
					
					attr = setOfattr.getNamedItem("recsize");
					if (attr != null) {
						String attval = attr.getNodeValue();

						recsize = Integer.parseInt(attval);
					} else {
						recsize = 0;
					}
					
					attr = setOfattr.getNamedItem("bextension");
					if (attr != null) {
						String attval = attr.getNodeValue();

						bextension = Integer.parseInt(attval);
					} else {
						bextension = 0;
					}
					
					attr = setOfattr.getNamedItem("gposition");
					if (attr != null) {
						String attval = attr.getNodeValue();

						gposition = Integer.parseInt(attval);
					} else {
						gposition = 0;
					}
					
					attr = setOfattr.getNamedItem("fontsize");
					if (attr != null) {
						String attval = attr.getNodeValue();

						fontsize = Integer.parseInt(attval);
					} else {
						fontsize = 0;
					}
				} else if (BuildingBlockType.equals("steps")){
					setOfattr = el.getAttributes();
					attr = setOfattr.getNamedItem("betweenSpaces");
					if (attr != null) {
						String bs = attr.getNodeValue();

						betweenSize = Integer.parseInt(bs);
					} else {
						betweenSize = 0;
					}
					attr = setOfattr.getNamedItem("interLine");
					if (attr != null) {
						String il = attr.getNodeValue();

						interLine = Integer.parseInt(il);
					} else {
						interLine = 0;
					}
					NodeList nodeLst = doc.getChildNodes();
					
					for (int s = 0; s < nodeLst.getLength(); s++) {

						Node fstNode = nodeLst.item(s);

						if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

							Element fstElmnt = (Element) fstNode;

							if (fstElmnt != null) {
								
								String fstElmntName = fstNode.getNodeName();
								if(fstElmntName.equals("step")){
									ret.add(new GraphNodes(fstNode.getTextContent()));
								}

								
							}
						}
					}
				}
				else if (BuildingBlockType.equals("foldoutPage")){
						
				}
		}
		NodeList nodeLst = doc.getChildNodes();
		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				if(fstElmnt != null){	

					List<GraphNodes> accuret = parseXMLDescription((Node)fstElmnt);
					for(int i=0;i < accuret.size();i++){

							ret.add((GraphNodes)accuret.get(i));
					}
				}
			}
		}
		return ret;
		}
	
	public List<String> divideLine(String entireLine, int xext){
		List<String> ret = new Vector<String>();
		
		List <String> divisionSet = splitText(entireLine, xext);
		String remainder = divisionSet.get(1);
		String nextLine = divisionSet.get(0);
		while(!remainder.equals("") && !nextLine.equals("")){
			if(!nextLine.equalsIgnoreCase(" "))ret.add(nextLine);
			divisionSet = splitText(remainder, xext);
			remainder = divisionSet.get(1);
			nextLine = divisionSet.get(0);
		}
		ret.add(nextLine);
		ret.add(remainder);
		return ret;
		
	}

	private List<String> splitText(String tx, int lext) {
		List<String> ret = new Vector<String>();
		String remainder = tx;

		boolean splitfound = false;
		String[] splited = remainder.split("\\s+");
		String acc1 = "";
		String acc2 = "";
		for (String curstr : splited) {
			if (!curstr.equals("")) {
				acc2 = acc2 + curstr + " ";
				int tw = (int) (font.getStringBounds(acc2, frc).getWidth());
				if (tw > lext && !splitfound) {
					splitfound = true;
					acc2 = curstr;
				} else if (!splitfound) {
					acc1 = acc1 + curstr + " ";
				}
			}
		}
		ret.add(acc1);
		if (splitfound) {
			ret.add(acc2);
		} else {
			ret.add("");
		}
		return ret;

	}
}

					
			