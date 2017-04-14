
package cn.mljia.common.notify.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: NotifyTypeEnum 
 * @Description: TODO 
 * @author: marker@mljia.cn
 * @date: 2017年4月13日 上午9:42:26
 */
public enum NotifyTypeEnum {

	/**
	 * 商户通知
	 */
	MERCHANT("商户通知"),

	/**
	 * 微信刷卡支付轮询
	 */
	WEPAY_SEARCH("微信刷卡支付轮询");

	/** 描述 */
	private String desc;

	private NotifyTypeEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static Map<String, Map<String, Object>> toMap() {
		NotifyTypeEnum[] ary = NotifyTypeEnum.values();
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
		NotifyTypeEnum[] ary = NotifyTypeEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}

	public static NotifyTypeEnum getEnum(String name) {
		NotifyTypeEnum[] arry = NotifyTypeEnum.values();
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
		NotifyTypeEnum[] enums = NotifyTypeEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (NotifyTypeEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}

}
