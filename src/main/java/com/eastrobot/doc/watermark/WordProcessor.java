/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.watermark;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.ImagePngPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart.AddPartBehaviour;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.SectPr;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 上午11:04:42
 * @version 1.0
 */
public class WordProcessor extends AbstractProcessor {
	
	private WordprocessingMLPackage wordMLPackage;
	
	public WordProcessor(File file, File imageFile) {
		super(file, imageFile);
	}

	/**
	 * 传入一个 word 文件，和一个水印文件，给这个 word文件加水印
	 * @author eko.zhan at 2018年9月17日 上午11:09:56
	 * @param file
	 * @param imgFile
	 * @throws Docx4JException 
	 * @throws JAXBException 
	 * @throws IOException 
	 */
	public void process() throws WatermarkException {
		try {
			WordprocessingMLPackage wordMLPackage = Docx4J.load(file);
			this.wordMLPackage = wordMLPackage;
			SectPr sectPr = createSectPr();
			wordMLPackage.getMainDocumentPart().getContents().getBody().setSectPr(sectPr);
			Docx4J.save(wordMLPackage, file);
		} catch (Docx4JException e) {
			throw new WatermarkException("Docx4JException", e);
		} catch (JAXBException e) {
			throw new WatermarkException("JAXBException", e);
		} catch (IOException e) {
			throw new WatermarkException("IOException", e);
		}
	}
	
	private SectPr createSectPr() throws JAXBException, Docx4JException, IOException {
		String openXML = "<w:sectPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
				// Word adds the background image in each of 3 header parts
				+ "<w:headerReference r:id=\"" + createHeaderPart("even").getId() + "\" w:type=\"even\"/>"
				+ "<w:headerReference r:id=\"" + createHeaderPart("default").getId() + "\" w:type=\"default\"/>"
				+ "<w:headerReference r:id=\"" + createHeaderPart("first").getId() + "\" w:type=\"first\"/>"

	            + "<w:pgSz w:h=\"15840\" w:w=\"12240\"/>"
	            + "<w:pgMar w:bottom=\"1440\" w:footer=\"708\" w:gutter=\"0\" w:header=\"708\" w:left=\"1440\" w:right=\"1440\" w:top=\"1440\"/>"
	            + "<w:cols w:space=\"708\"/>"
	            + "<w:docGrid w:linePitch=\"360\"/>"
	            +"</w:sectPr>";
		return (SectPr)XmlUtils.unmarshalString(openXML);
	}
	
	private Relationship createHeaderPart(String nameSuffix) throws Docx4JException, IOException, JAXBException {
		HeaderPart headerPart = new HeaderPart(new PartName("/word/header-" + nameSuffix + ".xml"));
		Relationship rel =  wordMLPackage.getMainDocumentPart().addTargetPart(headerPart);
		setHdr(headerPart);
		return rel;
	}
	
	private void setHdr(HeaderPart headerPart) throws Docx4JException, IOException, JAXBException  {
		ImagePngPart imagePart = new ImagePngPart(new PartName("/media/background.png"));
		try (InputStream in = new FileInputStream(imageFile)){
			imagePart.setBinaryData(in);
		}
		Relationship rel = headerPart.addTargetPart(imagePart, AddPartBehaviour.REUSE_EXISTING); // the one image is shared by the 3 header parts

		String openXML = "<w:hdr mc:Ignorable=\"w14 wp14\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
				+ "<w:p>"
				+ "<w:pPr>"
				+ "<w:pStyle w:val=\"Header\"/>"
				+"</w:pPr>"
				+ "<w:r>"
				+ "<w:rPr>"
				+ "<w:noProof/>"
				+"</w:rPr>"
				+ "<w:pict>"
				+ "<v:shapetype coordsize=\"21600,21600\" filled=\"f\" id=\"_x0000_t75\" o:preferrelative=\"t\" o:spt=\"75\" path=\"m@4@5l@4@11@9@11@9@5xe\" stroked=\"f\">"
				+ "<v:stroke joinstyle=\"miter\"/>"
				+ "<v:formulas>"
				+ "<v:f eqn=\"if lineDrawn pixelLineWidth 0\"/>"
				+ "<v:f eqn=\"sum @0 1 0\"/>"
				+ "<v:f eqn=\"sum 0 0 @1\"/>"
				+ "<v:f eqn=\"prod @2 1 2\"/>"
				+ "<v:f eqn=\"prod @3 21600 pixelWidth\"/>"
				+ "<v:f eqn=\"prod @3 21600 pixelHeight\"/>"
				+ "<v:f eqn=\"sum @0 0 1\"/>"
				+ "<v:f eqn=\"prod @6 1 2\"/>"
				+ "<v:f eqn=\"prod @7 21600 pixelWidth\"/>"
				+ "<v:f eqn=\"sum @8 21600 0\"/>"
				+ "<v:f eqn=\"prod @7 21600 pixelHeight\"/>"
				+ "<v:f eqn=\"sum @10 21600 0\"/>"
				+"</v:formulas>"
				+ "<v:path gradientshapeok=\"t\" o:connecttype=\"rect\" o:extrusionok=\"f\"/>"
				+ "<o:lock aspectratio=\"t\" v:ext=\"edit\"/>"
				+"</v:shapetype>"
				+ "<v:shape id=\"WordPictureWatermark835936646\" o:allowincell=\"f\" o:spid=\"_x0000_s2050\" style=\"position:absolute;margin-left:0;margin-top:0;width:640px;height:400px;z-index:-251657216;mso-position-horizontal:center;mso-position-horizontal-relative:margin;mso-position-vertical:center;mso-position-vertical-relative:margin\" type=\"#_x0000_t75\">"
				+ "<v:imagedata blacklevel=\"22938f\" gain=\"19661f\" o:title=\"docx4j-logo\" r:id=\"" + rel.getId() + "\"/>"
				+"</v:shape>"
				+"</w:pict>"
				+"</w:r>"
				+"</w:p>"
				+"</w:hdr>";

		Hdr hdr = (Hdr)XmlUtils.unmarshalString(openXML);
		headerPart.setJaxbElement(hdr);
	}
}
