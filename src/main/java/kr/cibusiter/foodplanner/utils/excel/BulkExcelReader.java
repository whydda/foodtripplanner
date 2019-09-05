/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package kr.cibusiter.foodplanner.utils.excel;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.util.*;

/**
 * XSSF and SAX (Event API) basic example.
 * See {@link XLSX2CSV} for a fuller example of doing
 *  XSLX processing with the XSSF Event code.
 */
public class BulkExcelReader {

	//returnList
//	private List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
	private Set<String> setList = new HashSet<String>();

	public List<Map<String,Object>> getList(){
  		return returnList();
  	}

	private List<Map<String,Object>> returnList(){
		List list = new ArrayList<Map<String,Object>>();
		for (String str : setList) {
			if(str.equals("Id"))continue;
			Map tmp = new HashMap<String,Object>();
        	tmp.put("MEMBER_ID", str);
        	list.add(tmp);
		}
		return list;
	}

	 public void processFirstSheet(InputStream inputStream) throws Exception {

        try (OPCPackage pkg = OPCPackage.open(inputStream)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);
            // process the first sheet
            try (InputStream sheet = r.getSheetsData().next()) {
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
            }
        }
    }



    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        XMLReader parser = SAXHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private class SheetHandler extends DefaultHandler {
        private final SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

            // c => cell, A TYPE COLUMN only
            if(name.equals("c") && attributes.getValue("r").indexOf("A")>-1) {
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
            	Integer idx = Integer.valueOf(lastContents);
            	//getting inserted xml value by idx
            	lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
//            	Map tmp = new HashMap<String,Object>();
//            	tmp.put("MEMBER_ID", lastContents);
//            	list.add(tmp);
            	setList.add(lastContents);
            	//nextFalse
                nextIsString = false;
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException { // NOSONAR
            lastContents += new String(ch, start, length);
        }
    }




    /* 필요하면 사용하면됨
    public void processFirstSheet(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);

            // process the first sheet
            try (InputStream sheet = r.getSheetsData().next()) {
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
			}
        }
    }

    public void processAllSheets(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);

            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
//                System.out.println("Processing new sheet:\n");
                try (InputStream sheet = sheets.next()) {
					InputSource sheetSource = new InputSource(sheet);
					parser.parse(sheetSource);
				}
//                System.out.println("");
            }
        }
    }*/


}
