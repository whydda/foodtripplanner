package kr.cibusiter.foodplanner.enumeration;

import kr.cibusiter.foodplanner.params.CommonMap;

import java.util.ArrayList;
import java.util.List;

public enum ColorCdEnum {
    	ONE("00","전체",""),
		TWO("01","요청","#dedede"),
    	THREE("02","승인","#b3a2c7"),
    	FOUR("03","발송예정","#8eb4e3"),
    	FIVE("04","발송중","#0070c0"),
    	SIX("05","발송완료","#92d050"),
    	SEVEN("06","발송중지","#df0000"),
    	EIGHT("07","발송오류","#e46c0a");

    	private String code;
		private String value;
		private String color;

		ColorCdEnum(String tv1, String tv2, String tv3) {
			this.code = tv1;
			this.value = tv2;
			this.color = tv3;
		}

	private static List<CommonMap> colorList = new ArrayList<>();

	public static List<CommonMap> colorCd(){
		if(colorList.isEmpty()) {
			for (ColorCdEnum e : ColorCdEnum.values()) {
				CommonMap map = new CommonMap();
				map.put("code_color", e.color); map.put("code_name", e.value); map.put("code_value", e.code);
				colorList.add(map);
			}
		}
		return colorList;
	}
}
