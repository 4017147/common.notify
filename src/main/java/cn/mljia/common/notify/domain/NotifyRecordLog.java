package cn.mljia.common.notify.domain;

import java.io.Serializable;
import java.util.Date;

import cn.mljia.ddd.common.domain.model.ConcurrencySafeEntity;

/**
 * 
 * @ClassName: RpNotifyRecordLog
 * @Description: TODO 通知日志记录
 * @author: marker@mljia.cn
 * @date: 2017�?4�?11�? 下午2:09:52
 */
public class NotifyRecordLog extends ConcurrencySafeEntity implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 459406550725396000L;
    
    /** 通知记录ID **/
    private String notifyId;
    
    /** 请求信息 **/
    private String request;
    
    /** 返回信息 **/
    private String response;
    
    /** 通知消息�? **/
    private String notifyBody;
    
    /** HTTP状�?? **/
    private Integer httpStatus;
    
    private Date createTime;
    
    /** 修改时间 **/
    private Date editTime;
    
    public NotifyRecordLog()
    {
        super();
    }
    
    public NotifyRecordLog(String notifyId, String request, String response, String notifyBody, Integer httpStatus)
    {
        super();
        this.createTime = new Date();
        this.notifyId = notifyId;
        this.request = request;
        this.response = response;
        this.notifyBody = notifyBody;
        this.httpStatus = httpStatus;
        this.editTime = new Date();
    }
    
    @Override
    public String toString()
    {
        return "NotifyRecordLog [notifyId=" + notifyId + ", request=" + request + ", response=" + response
            + ", notifyBody=" + notifyBody + ", httpStatus=" + httpStatus + ", createTime=" + createTime
            + ", editTime=" + editTime + "]";
    }
    
}
