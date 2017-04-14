package cn.mljia.common.notify.application;

import java.util.Map;

/**
 * 
 * @ClassName: NotifyParam 
 * @Description: TODO 通知参数（通知规则），从XML中加载.
 * @author: marker@mljia.cn
 * @date: 2017年4月13日 上午9:41:36
 */
public class NotifyParam {

	/**
	 * 通知参数（通知规则Map）
	 */
	private Map<Integer, Integer> notifyParams;

	/**
	 * 通知后用于判断是否成功的返回值（成功标识）,由HttpResponse获取
	 */
	private String successValue;

	public Map<Integer, Integer> getNotifyParams() {
		return notifyParams;
	}

	public void setNotifyParams(Map<Integer, Integer> notifyParams) {
		this.notifyParams = notifyParams;
	}

	public String getSuccessValue() {
		return successValue;
	}

	public void setSuccessValue(String successValue) {
		this.successValue = successValue;
	}

	/**
	 * 最大通知次数限制.
	 * 
	 * @return
	 */
	public Integer getMaxNotifyTimes() {
		return notifyParams == null ? 0 : notifyParams.size();
	}

}
