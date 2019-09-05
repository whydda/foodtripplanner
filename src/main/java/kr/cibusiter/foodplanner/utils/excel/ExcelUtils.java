package kr.cibusiter.foodplanner.utils.excel;

import kr.cibusiter.foodplanner.params.CommonMap;
import kr.cibusiter.foodplanner.utils.CommonUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by whydda on 2017-07-28.
 */
public class ExcelUtils {

    /**
     * xls파일로 다운로드 처리
     * @param excelList
     * @param title
     * @param file_name
     * @return
     * @throws Exception
     */
    public static HSSFWorkbook excelDataDownloadXls(List<CommonMap> excelList, String title, String file_name) throws Exception {

        // 문자열 형식의 제목을 excelTtitle변수에 담는다(후에 split을 통해서 배열로 만들것임)
        String excelTitle = title;

        HSSFWorkbook wb = new HSSFWorkbook();
        // 시트 생성
        HSSFSheet sheet = wb.createSheet(file_name);

        String titleArr[] = excelTitle.split(","); // 시트의 제목이 되는 부분

        // 첫번째 행에 각각의 컬럼 제목작성
        HSSFCell cell = null;
        HSSFRow row = sheet.createRow(0);

        //제목에 스타일을 적용한다.
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle styleContent = wb.createCellStyle();

        //글자에 대한 스타일 적용
        HSSFFont font = wb.createFont();
        HSSFFont fontContent = wb.createFont();

        setExcelStyle(font, style, styleContent, fontContent);

        for (int titleNo = 0; titleNo < titleArr.length; titleNo++) {
            cell = row.createCell(titleNo);
            cell.setCellValue(titleArr[titleNo]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(titleNo, true);
        }

        addRowExcel(excelList, cell, row, titleArr, style, sheet, styleContent);

        return wb;
    }


    /**
     * xlsx파일로 다운로드 처리
     * @param excelList
     * @param title
     * @param file_name
     * @return
     * @throws Exception
     */
    public static XSSFWorkbook excelDataDownloadXlsx(List<Map<String, Object>> excelList, String title, String file_name) throws Exception {

        // 문자열 형식의 제목을 excelTtitle변수에 담는다(후에 split을 통해서 배열로 만들것임)
        String excelTitle = title;

        XSSFWorkbook wb = new XSSFWorkbook();
        // 시트 생성
        XSSFSheet sheet = wb.createSheet(file_name);

        String titleArr[] = excelTitle.split(","); // 시트의 제목이 되는 부분

        // 첫번째 행에 각각의 컬럼 제목작성
        XSSFCell cell = null;
        XSSFRow row = sheet.createRow(0);

        //제목에 스타일을 적용한다.
        XSSFCellStyle style = wb.createCellStyle();
        XSSFCellStyle styleContent = wb.createCellStyle();

        //글자에 대한 스타일 적용
        XSSFFont font = wb.createFont();
        XSSFFont fontContent = wb.createFont();

        setExcelStyle(font, style, styleContent, fontContent);

        return wb;
    }





    /**
     * 엑셀파일을 읽어서 Workbook 객체에 리턴한다.
     * XLS와 XLSX 확장자를 비교한다.
     * @param fis
     * @return
     */
    public static Workbook getWorkbook(InputStream fis) throws Exception {
        Workbook wb = null;

        try {
            wb = new XSSFWorkbook(fis);
        } catch (Exception e) {
            try {
                wb = new HSSFWorkbook(fis);
            }catch (IOException ioe){
                throw new Exception(ioe.getMessage(), ioe);
            }
        }
        return wb;
    }

    /**
     * 엑셀데이타 업로드
     * @param workbook
     * @param excelReadOption
     * @return
     * @throws Exception
     */
    public static List<Map<String,String>> excelDataUpload(Workbook workbook, ExcelReadOption excelReadOption) throws Exception {
        //엑셀 파일 자체
        //엑셀파일을 읽어 들인다.
        //FileType.getWorkbook() <-- 파일의 확장자에 따라서 적절하게 가져온다.
        Workbook wb = workbook;
        /**
         * 엑셀 파일에서 첫번째 시트를 가지고 온다.
         */
        Sheet sheet = wb.getSheetAt(0);

        /**
         * sheet에서 유효한(데이터가 있는) 행의 개수를 가져온다.
         */
        int numOfRows = sheet.getPhysicalNumberOfRows();
        int numOfCells = 0;

        Row row = null;
        Cell cell = null;

        String cellName = "";
        /**
         * 각 row마다의 값을 저장할 맵 객체
         * 저장되는 형식은 다음과 같다.
         * put("A", "이름");
         * put("B", "게임명");
         */
        Map<String, String> map = null;
        /*
         * 각 Row를 리스트에 담는다.
         * 하나의 Row를 하나의 Map으로 표현되며
         * List에는 모든 Row가 포함될 것이다.
         */
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        /**
         * 각 Row만큼 반복을 한다.
         */
        for(int rowIndex = excelReadOption.getStartRow() - 1; rowIndex < numOfRows; rowIndex++) {
            /*
             * 워크북에서 가져온 시트에서 rowIndex에 해당하는 Row를 가져온다.
             * 하나의 Row는 여러개의 Cell을 가진다.
             */
            row = sheet.getRow(rowIndex);

            if(row != null) {
                /*
                 * 가져온 Row의 Cell의 개수를 구한다.
                 */
                numOfCells = row.getPhysicalNumberOfCells();
                /*
                 * 데이터를 담을 맵 객체 초기화
                 */
                map = new HashMap<String, String>();
                /*
                 * cell의 수 만큼 반복한다.
                 */
                for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
                    /*
                     * Row에서 CellIndex에 해당하는 Cell을 가져온다.
                     */
                    cell = row.getCell(cellIndex);
                    /*
                     * 현재 Cell의 이름을 가져온다
                     * 이름의 예 : A,B,C,D,......
                     */
                    cellName = ExcelCellRef.getName(cell, cellIndex);
                    /*
                     * 추출 대상 컬럼인지 확인한다
                     * 추출 대상 컬럼이 아니라면,
                     * for로 다시 올라간다
                     */
                    if( !excelReadOption.getOutputColumns().contains(cellName) ) {
                        continue;
                    }
                    /*
                     * map객체의 Cell의 이름을 키(Key)로 데이터를 담는다.
                     */
                    map.put(cellName, ExcelCellRef.getValue(cell));
                }
                /*
                 * 만들어진 Map객체를 List로 넣는다.
                 */
                result.add(map);
            }

        }
        //값이 없는 객체는 삭제
        result.removeIf(CommonUtils::removeObjectNull);

        return result;
    }

    public static boolean write(HttpServletResponse res, Workbook workbook, String filename) throws IOException {

        res.reset();
        res.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
        res.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20") + "\";");
        res.setContentType("application/x-msexcel");
        return write(res.getOutputStream(), workbook);
    }

    public static boolean write(OutputStream os, Workbook workbook) throws IOException {

        workbook.write(os);
        os.flush();
        os.close();
        return true;
    }

    public static void write(HttpServletResponse res, Workbook workbook, String filename, boolean setCookie) throws IOException {

        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        res.setHeader("Set-Cookie","fileDownload="+String.valueOf(setCookie)+"; path=/");
        OutputStream fop = res.getOutputStream();
        if(ExcelUtils.write(fop, workbook)) {
            return;
        }
    }

    private static void setExcelStyle(Font font, CellStyle style, CellStyle styleContent, Font fontContent){
        font.setBoldweight(font.BOLDWEIGHT_BOLD);
        font.setFontName("굴림체");

        style.setFont(font);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        fontContent.setFontName("굴림체");
        styleContent.setFont(fontContent);
        styleContent.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleContent.setBottomBorderColor(HSSFColor.BLACK.index);
        styleContent.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleContent.setLeftBorderColor(HSSFColor.BLACK.index);
        styleContent.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleContent.setRightBorderColor(HSSFColor.BLACK.index);
        styleContent.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleContent.setTopBorderColor(HSSFColor.BLACK.index);
        styleContent.setWrapText(true);
        styleContent.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
    }

    private static void setExcelList(List<CommonMap> excelList, Cell cell, Row row, String[] titleArr, CellStyle style, Sheet sheet, CellStyle styleContent){

        for (int titleNo = 0; titleNo < titleArr.length; titleNo++) {
            cell = row.createCell(titleNo);
            cell.setCellValue(titleArr[titleNo]);
            cell.setCellStyle(style);
        }

        addRowExcel(excelList, cell, row, titleArr, style, sheet, styleContent);

        for (int titleNo = 0; titleNo < titleArr.length; titleNo++) {
            sheet.autoSizeColumn(titleNo, true);
            sheet.setColumnWidth(titleNo, (sheet.getColumnWidth(titleNo))+512 );
        }
    }

    /**
     * 엑셀 add row
     * @param excelList
     * @param cell
     * @param row
     * @param titleArr
     * @param style
     * @param sheet
     * @param styleContent
     */
    private static void addRowExcel(List<CommonMap> excelList, Cell cell, Row row, String[] titleArr, CellStyle style, Sheet sheet, CellStyle styleContent){
        //엑셀 만들기
        int i = 1;
        for (CommonMap mapv : excelList) {
            row = sheet.createRow(i);
            Iterator<String> keys = mapv.keySet().iterator();
            int j = 0;
            while(keys.hasNext()) {
                String key = keys.next();
                if (mapv.get(key) == null) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                } else {
                    cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(mapv.get(key)));
                }
                cell.setCellStyle(styleContent);
                j++;
            }
            i++;
        }
    }
}
