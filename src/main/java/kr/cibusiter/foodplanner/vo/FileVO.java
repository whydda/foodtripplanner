package kr.cibusiter.foodplanner.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by whydda on 2017-08-01.
 */
@Getter
@Setter
public class FileVO {
    private int imageNo;
    private String realName;
    private String fileName;
    private long fileSize;
    private String filePath;
}
