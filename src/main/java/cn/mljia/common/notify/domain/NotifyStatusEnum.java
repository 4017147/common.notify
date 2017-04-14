
package cn.mljia.common.notify.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: NotifyStatusEnum 
 * @Description: TODO
 * @author: marker@mljia.cn
 * @date: 2017年4月13日 上午9:42:09
 */
public enum NotifyStatusEnum {

	CREATED("通知记录已创建"), SUCCESS("通知成功"), FAILED("通知失败"), HTTP_REQUEST_SUCCESS("http请求响应成功"), HTTP_REQUEST_FALIED(
			"http请求失败");

	/** 描述 */
	private String desc;

	private NotifyStatusEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static Map<String, Map<String, Object>> toMap() {
		NotifyStatusEnum[] ary = NotifyStatusEnum.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = ary[num].name();
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		NotifyStatusEnum[] ary = NotifyStatusEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}

	public static NotifyStatusEnum getEnum(String name) {
		NotifyStatusEnum[] arry = NotifyStatusEnum.values();
		for (int i = 0; i < arry.length; i++) {
			if (arry[i].name().equalsIgnoreCase(name)) {
				return arry[i];
			}
		}
		return null;
	}

	/**
	 * 取枚举的json字符串
	 *
	 * @return
	 */
	public static String getJsonStr() {
		NotifyStatusEnum[] enums = NotifyStatusEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (NotifyStatusEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
