package kr.cibusiter.foodplanner.enumeration;

import kr.cibusiter.foodplanner.params.CommonMap;

import java.util.ArrayList;
import java.util.List;

public enum UserColorCdEnum {
	GUEST("00","게스트",""),
	USER("01","사용자","#dedede"),
	ADMIN("02","관리자","#b3a2c7");

    	private String code;
		private String value;
		private String color;

		UserColorCdEnum(String tv1, String tv2, String tv3) {
			this.code = tv1;
			this.value = tv2;
			this.color = tv3;
		}

	private static List<CommonMap> colorList = new ArrayList<>();

	public static List<CommonMap> colorCd(){
		if(colorList.isEmpty()) {
			for (UserColorCdEnum e : UserColorCdEnum.values()) {
				CommonMap map = new CommonMap();
				map.put("code_color", e.color); map.put("code_name", e.value); map.put("code_value", e.code);
				colorList.add(map);
			}
		}
		return colorList;
	}
}
