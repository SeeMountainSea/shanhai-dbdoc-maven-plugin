package com.wangshanhai.dbdoc.utils;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.IOException;
import java.math.BigInteger;

/**
 * The {@code WordUtil} class
 *
 * @author Fly.Sky
 * @since 2022/10/28 10:06
 */
public class WordUtil {
    /**
     * 创建默认的页脚(该页脚主要只居中显示页码)
     *
     * @param docx XWPFDocument文档对象
     * @return
     * @throws IOException IO异常
     */
    public static void createDefaultFooter(XWPFDocument docx) throws IOException {
        CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docx, sectPr);
        XWPFFooter footer = headerFooterPolicy.createFooter(STHdrFtr.DEFAULT);
        XWPFParagraph paragraph = footer.getParagraphArray(0);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setVerticalAlignment(TextAlignment.CENTER);
        CTTabStop tabStop = paragraph.getCTP().getPPr().addNewTabs().addNewTab();
        tabStop.setVal(STTabJc.CENTER);
        XWPFRun run = paragraph.createRun();
        run.addTab();
        run = paragraph.createRun();
        run.setText("第");
        run = paragraph.createRun();
        CTFldChar fldChar = run.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));
        run = paragraph.createRun();
        CTText ctText = run.getCTR().addNewInstrText();
        ctText.setStringValue("PAGE  \\* MERGEFORMAT");
        ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
        fldChar = run.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("end"));
        run = paragraph.createRun();
        run.setText("页/共");
        run = paragraph.createRun();
        fldChar = run.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));
        run = paragraph.createRun();
        ctText = run.getCTR().addNewInstrText();
        ctText.setStringValue("NUMPAGES  \\* MERGEFORMAT ");
        ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
        fldChar = run.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.Enum.forString("end"));
        run = paragraph.createRun();
        run.setText("页");
    }


    /**
     * 设置表头内容
     *
     * @param cell
     * @param text
     * @param width
     * @param fontFamily
     * @param fontSize
     * @param bold
     */
    public static void setCellText(XWPFTableCell cell, String text, int width, String fontFamily, int fontSize, boolean bold) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        cell.setColor("A6A6A6");
        XWPFRun run = paragraph.createRun();
        run.setFontFamily(fontFamily);
        run.setFontSize(fontSize);
        run.setBold(bold);
        run.setText(text);
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
    }

    /**
     * 设置页边距
     *
     * @param doc
     * @param left
     * @param right
     * @param top
     * @param bottom
     */
    public static void setMargin(XWPFDocument doc, int left, int right, int top, int bottom) {
        CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(left));
        pageMar.setRight(BigInteger.valueOf(right));
        pageMar.setTop(BigInteger.valueOf(top));
        pageMar.setBottom(BigInteger.valueOf(bottom));
    }

    /**
     * 设置段落文本
     *
     * @param doc
     * @param text
     * @param fontFamily
     * @param fontSize
     * @param textPosition
     * @param bold
     * @param paragraphAlignment
     */
    public static void setParagraph(XWPFDocument doc,
                                    String text,
                                    String fontFamily,
                                    int fontSize,
                                    int textPosition,
                                    boolean bold,
                                    ParagraphAlignment paragraphAlignment,int level) {
        XWPFParagraph xp = doc.createParagraph();
        xp.setSpacingBeforeLines(1);
        if(level>0){
            CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
            indentNumber.setVal(BigInteger.valueOf(level));
            xp.getCTP().getPPr().setOutlineLvl(indentNumber);
        }
        XWPFRun r1 = xp.createRun();
        r1.setText(text);
        r1.setFontFamily(fontFamily);
        r1.setFontSize(fontSize);
        r1.setTextPosition(textPosition);
        r1.setBold(bold);
        xp.setAlignment(paragraphAlignment);
    }

    /**
     * 设置表格样式
     *
     * @param cell
     * @param paragraphAlignment
     * @param fontFamily
     * @param fontSize
     * @param bold
     * @return
     */
    public static XWPFRun setTableCellStyle(XWPFTableCell cell, ParagraphAlignment paragraphAlignment, String fontFamily, int fontSize, boolean bold) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        paragraph.setAlignment(paragraphAlignment);
        XWPFRun run = paragraph.createRun();
        run.setFontFamily(fontFamily);
        run.setFontSize(fontSize);
        run.setBold(bold);
        return run;
    }

    /**
     * 设置边框
     *
     * @param xTable
     * @param bolderSize
     */
    public static void setTableBolder(XWPFTable xTable, int bolderSize, String color) {
        CTTblBorders borders = xTable.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder hBorder = borders.addNewInsideH();
        hBorder.setVal(STBorder.Enum.forString("single"));
        hBorder.setSz(new BigInteger(String.valueOf(bolderSize)));
        hBorder.setColor(color);

        CTBorder vBorder = borders.addNewInsideV();
        vBorder.setVal(STBorder.Enum.forString("single"));
        vBorder.setSz(new BigInteger(String.valueOf(bolderSize)));
        vBorder.setColor(color);

        CTBorder lBorder = borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString("single"));
        lBorder.setSz(new BigInteger(String.valueOf(bolderSize)));
        lBorder.setColor(color);

        CTBorder rBorder = borders.addNewRight();
        rBorder.setVal(STBorder.Enum.forString("single"));
        rBorder.setSz(new BigInteger(String.valueOf(bolderSize)));
        rBorder.setColor(color);

        CTBorder tBorder = borders.addNewTop();
        tBorder.setVal(STBorder.Enum.forString("single"));
        tBorder.setSz(new BigInteger(String.valueOf(bolderSize)));
        tBorder.setColor(color);

        CTBorder bBorder = borders.addNewBottom();
        bBorder.setVal(STBorder.Enum.forString("single"));
        bBorder.setSz(new BigInteger(String.valueOf(bolderSize)));
        bBorder.setColor(color);
    }

}
