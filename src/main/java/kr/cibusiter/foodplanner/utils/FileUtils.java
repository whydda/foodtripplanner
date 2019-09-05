package kr.cibusiter.foodplanner.utils;

import kr.cibusiter.foodplanner.utils.excel.ExcelReadOption;
import kr.cibusiter.foodplanner.utils.excel.ExcelUtils;
import kr.cibusiter.foodplanner.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by whydda on 2017-07-28.
 */
@Slf4j
public class FileUtils {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 파일 업로드
     * @param defaultParams
     * @throws Exception
     */
    public void fileUploadFiles(Map<String, Object> defaultParams) throws Exception{
        //업로드될 절대경로를 가지고 온다.
        String filePath = resourceLoader.getResource("/").getFile().getAbsolutePath() + File.separator +"resources" + File.separator + String.valueOf(defaultParams.get("filePath"));
        defaultParams.put("filePath", filePath);

        List<Map<String, Object>> uploadFiles = (List<Map<String, Object>>) defaultParams.get("uploadFileMap");

        //ATTACH 객체가 1나 라도 있으면 삭제파일이 있는지 확인하고 삭제한다.
        if(defaultParams.get("ATTACH") != null){
            if(((List<Map<String, Object>>) defaultParams.get("ATTACH")).size() > 0) {
                //삭제 메서드 호출
                deleteFile(defaultParams);
            }

            if (null != uploadFiles && 0 < uploadFiles.size()) {
                //업로드 메서드 호출
                uploadFile(defaultParams);
            }
        }
    }

    /**
     * 엑셀 파일 업로드
     * @param defaultParams
     * @throws Exception
     */
    public static List<Map<String, String>> excelFileUploadFile(Map<String, Object> defaultParams) throws Exception{
        //업로드될 절대경로를 가지고 온다.
        Map<String, Object> map = ((List<Map<String, Object>>) defaultParams.get("uploadFileMap")).get(0);
        MultipartFile multipartFile = ((List<MultipartFile>) map.get("files")).get(0);
        ExcelReadOption excelReadOption = new ExcelReadOption();
        //컬럼은 직접 입력한다. 이 부분이 MAP에 키가 된다.
        excelReadOption.setOutputColumns("A","B","C","D","E","F","G");
        excelReadOption.setStartRow(2); //시작 row 를 나타 낸다.
        Workbook wb = ExcelUtils.getWorkbook(multipartFile.getInputStream());
        return ExcelUtils.excelDataUpload(wb, excelReadOption);
    }

    /**
     * 파일 삭제
     * @param paramMap
     * @throws Exception
     */
    public void deleteFile(Map<String, Object> paramMap) throws Exception{
        for (Map<String, Object> map : (List<Map<String, Object>>) paramMap.get("ATTACH")) {
            if ("D".equals(map.get("CRUD"))) {
                //파일 삭제처리
                //fileService.deleteFile(downPath, String.valueOf(map.get("IMAGE_SEQ_NO")), Integer.parseInt(String.valueOf(map.get("IMAGE_SEQ_NO"))));
            }else{
                if(!String.valueOf(map.get("IMAGE_SEQ_NO")).equals("null")){
                    paramMap.put("IMAGE_SEQ_NO", String.valueOf(map.get("IMAGE_SEQ_NO")));
                }
            }
        }
    }

    /**
     * 사진 업로드
     * @param defaultParams
     * @throws Exception
     */
    public void uploadFile(Map<String, Object> defaultParams) throws Exception{
        for (Map<String, Object> map : (List<Map<String, Object>>) defaultParams.get("uploadFileMap")) {
            if (String.valueOf(map.get("key")).equals(String.valueOf(defaultParams.get("RN")))) {
                for (MultipartFile multipartFile : (List<MultipartFile>) map.get("files")) {
                    defaultParams.put("IMAGE_SEQ_NO", fileUploadPros(defaultParams, multipartFile));
                }
            }
        }
    }


    /**
     * 파일 저장 로직
     * @param defaultParams
     * @param multipartFile
     * @return
     */
    public static String fileUploadPros(Map<String, Object> defaultParams, MultipartFile multipartFile) {
        String subDir = String.valueOf(defaultParams.get("filePath"));

        createDirectory(Paths.get(subDir));

        List<FileVO> fileLists = new ArrayList<>();
        String imageSeqNo = "";

        try {
        } catch (Exception e) {
        }

        return imageSeqNo;
    }

    /**
     * 디렉터리 생성
     * @param path
     * @return
     */
    public static void createDirectory(Path path) {
        if (!Files.exists(path) || Files.isDirectory(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error(e.getMessage(), e, e.getStackTrace());
            }
        }
    }

    /**
     * 파일다운로드 처리
     * @param request
     * @param response
     * @param filePath
     * @param fileName
     * @param fileOrgName
     */
    public static void fileDownload(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName, String fileOrgName){

        File file = new File(filePath, fileName);
        try(FileInputStream fi = new FileInputStream(file)){
            request.setCharacterEncoding("UTF-8");

            if(file.isFile()) {
                int bytes = (int) file.length();
                String header = request.getHeader("User-Agent");

                if (header.contains("MSIE") || header.contains("Trident")) {
                    fileOrgName = URLEncoder.encode(fileOrgName, "UTF-8").replaceAll("\\+", "%20");
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileOrgName + ";");
                } else {
                    fileOrgName = new String(fileOrgName.getBytes("UTF-8"), "ISO-8859-1");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileOrgName + "\"");
                }

                response.setContentType("application/download; UTF-8");
                response.setContentLength(bytes);
                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Content-Transfer-Encoding", "binary;");
                response.setHeader("Pragma", "no-cache;");
                response.setHeader("Expires", "-1;");

                IOUtils.copy(fi, response.getOutputStream());
            }
        }catch(Exception e){
            log.error("파일다운로드 오류 발생!!!",e);
        }
    }

}
