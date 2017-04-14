package cn.mljia.common.notify.application.command;

import java.io.Serializable;
import java.util.Map;

import cn.mljia.ddd.common.AssertionConcern;

public class CreateNotifyRecordCommand extends AssertionConcern implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	String notifyId;

	Map<Integer, Integer> notifyParams;

	Integer limitNotifyTimes;

	String url;

	String notifyBody;

	public CreateNotifyRecordCommand(String notifyId, Map<Integer, Integer> notifyParams, Integer limitNotifyTimes,
			String url, String notifyBody) {
		super();
		this.notifyId = notifyId;
		this.notifyParams = notifyParams;
		this.limitNotifyTimes = limitNotifyTimes;
		this.url = url;
		this.notifyBody = notifyBody;
	}

	public Map<Integer, Integer> getNotifyParams() {
		return notifyParams;
	}

	public Integer getLimitNotifyTimes() {
		return limitNotifyTimes;
	}

	public String getUrl() {
		return url;
	}

	public String getNotifyBody() {
		return notifyBody;
	}

	public String getNotifyId() {
		return notifyId;
	}

	@Override
	public String toString() {
		return "CreateNotifyRecordCommand [notifyId=" + notifyId + ", notifyParams=" + notifyParams
				+ ", limitNotifyTimes=" + limitNotifyTimes + ", url=" + url + ", notifyBody=" + notifyBody + "]";
	}

}
