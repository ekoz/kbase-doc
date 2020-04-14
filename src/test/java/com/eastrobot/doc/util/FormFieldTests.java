/*
 * Powered by http://www.xiaoi.com
 */
package com.eastrobot.doc.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.FieldsDocumentPart;
import org.apache.poi.hwpf.usermodel.Field;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import javax.xml.namespace.QName;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * poi 操作 word form field
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2019/8/19 18:23
 */
public class FormFieldTests {

    @Test
    public void testTextBox() {

    }


    @Test
    public void testDocx() throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream("D:\\Xiaoi\\Items\\2019-07-02 合同智能分析工具\\04_现场数据\\4.25国网北京信通公司110kV半壁店站等63个站点通信蓄电池改造勘察设计合同.docx"));

        List<XWPFParagraph> paragraphs = document.getParagraphs();
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables){
        }
        for (XWPFParagraph paragraph : paragraphs){
//            System.out.println(paragraph.getCTP().xmlText());
            printContentsOfTextBox(paragraph);
        }
    }

    private void printContentsOfTextBox(XWPFParagraph paragraph) {
//declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main'
// declare namespace wps='http://schemas.microsoft.com/office/word/2010/wordprocessingShape' .//*/wps:txbx/w:txbxContent
        String NS_W = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
        String queryExpression =
                "declare namespace w='" + NS_W + "';" +
                        "$this/w:permStart";
        XmlObject[] textBoxObjects =  paragraph.getCTP().selectPath(queryExpression);

        if (textBoxObjects.length>0){
            for (XmlObject xmlObject : textBoxObjects){
                XmlCursor xmlCursor = xmlObject.newCursor();

                // permStart 前面的 r 是主要内容

                String tmp = "";
                while (xmlCursor.toNextSibling()){
                    tmp += xmlCursor.getTextValue().trim();
                    // r / permStart
                    if (("permEnd").equals(xmlCursor.getName().getLocalPart())){
                        break;
                    }
                }
                System.out.println(tmp);
            }
        }

//        for (int i =0; i < textBoxObjects.length; i++) {
//            XWPFParagraph embeddedPara = null;
//            try {
//                XmlObject[] paraObjects = textBoxObjects[i].
//                        selectChildren(
//                                new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "p"));
//
//                for (int j=0; j<paraObjects.length; j++) {
//                    embeddedPara = new XWPFParagraph(
//                            CTP.Factory.parse(paraObjects[j].xmlText()), paragraph.getBody());
//                    //Here you have your paragraph;
//                    System.out.println(embeddedPara.getText());
//                }
//
//            } catch (XmlException e) {
//                //handle
//            }
//        }

    }

    @Test
    public void testDoc() throws IOException {
        HWPFDocument document = new HWPFDocument(new FileInputStream("D:\\Xiaoi\\Items\\2019-07-02 合同智能分析工具\\04_现场数据\\4.25国网北京信通公司110kV半壁店站等63个站点通信蓄电池改造勘察设计合同.doc"));
//        for ( FieldsDocumentPart part : FieldsDocumentPart.values() ) {
//            System.out.println( "=== Document part: " + part + " ===" );
//            for ( Field field : document.getFields().getFields( part ) ) {
//                System.out.println(field.firstSubrange(document.getRange()).getParagraph(0).text());
//            }
//        }

        for (int i=0;i<document.getRange().numParagraphs();i++){
            Paragraph paragraph = document.getRange().getParagraph(i);
            System.out.println(paragraph.text());
            System.out.println("===============================================");
        }

//        for (Field field : document.getFields().getFields(FieldsDocumentPart.MAIN)){
//            System.out.println(field);
//            System.out.println(field.firstSubrange(document.getRange()).getParagraph(0).text());
//        }
//        System.out.println(document.getRange().getParagraph(0).text());
//        System.out.println(document.getMainTextboxRange().getSection(0).text());

    }
}
