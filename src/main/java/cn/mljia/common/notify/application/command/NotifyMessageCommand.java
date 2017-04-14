package cn.mljia.common.notify.application.command;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.mljia.ddd.common.AssertionConcern;

public class NotifyMessageCommand extends AssertionConcern implements Serializable
{
    
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = 1L;
    
    private Date createTime;
    
    /** 通知规则 */
    private String notifyRule;
    
    /** �?后一次�?�知时间 **/
    private transient Date lastNotifyTime;
    
    /** 通知次数 **/
    private Integer notifyTimes;
    
    /** 限制通知次数 **/
    private Integer limitNotifyTimes;
    
    /** 通知URL **/
    private String url;
    
    /** 通知类容 **/
    private String notifyBody;
    
    /** 通知类型 NotifyTypeEnum **/
    private String notifyType;
    
    /** 通知ID **/
    private String notifyId;
    
    /** 修改时间 **/
    private Date editTime;
    
    /** 通知状�?? **/
    private String status;
    
    public NotifyMessageCommand()
    {
        super();
    }
    
    public NotifyMessageCommand(Date createTime, String notifyRule, Date lastNotifyTime, Integer notifyTimes,
        Integer limitNotifyTimes, String url, String notifyBody, String notifyType, String notifyId, Date editTime,
        String status)
    {
        super();
        this.createTime = createTime;
        this.notifyRule = notifyRule;
        this.lastNotifyTime = lastNotifyTime;
        this.notifyTimes = notifyTimes;
        this.limitNotifyTimes = limitNotifyTimes;
        this.url = url;
        this.notifyBody = notifyBody;
        this.notifyType = notifyType;
        this.notifyId = notifyId;
        this.editTime = editTime;
        this.status = status;
    }
    
    public void setNotifyTimes(Integer notifyTimes)
    {
        this.notifyTimes = notifyTimes;
    }
    
    public void setEditTime(Date editTime)
    {
        this.editTime = editTime;
    }
    
    public void setLastNotifyTime(Date lastNotifyTime)
    {
        this.lastNotifyTime = lastNotifyTime;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public String getNotifyRule()
    {
        return notifyRule;
    }
    
    public Date getLastNotifyTime()
    {
        return lastNotifyTime;
    }
    
    public Integer getNotifyTimes()
    {
        return notifyTimes;
    }
    
    public Integer getLimitNotifyTimes()
    {
        return limitNotifyTimes;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public String getNotifyBody()
    {
        return notifyBody;
    }
    
    public String getNotifyType()
    {
        return notifyType;
    }
    
    public String getNotifyId()
    {
        return notifyId;
    }
    
    public Date getEditTime()
    {
        return editTime;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public Map<Integer, Integer> getNotifyRuleMap()
    {
        return parseData(this.notifyRule);
    }
    
    private Map<Integer, Integer> parseData(String data)
    {
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<Integer, Integer> map = g.fromJson(data, new TypeToken<Map<Integer, Integer>>()
        {
        }.getType());
        return map;
    }
    
    @Override
    public String toString()
    {
        return "NotifyMessageCommand [createTime=" + createTime + ", notifyRule=" + notifyRule + ", notifyTimes="
            + notifyTimes + ", limitNotifyTimes=" + limitNotifyTimes + ", url=" + url + ", notifyBody=" + notifyBody
            + ", notifyType=" + notifyType + ", notifyId=" + notifyId + ", editTime=" + editTime + ", status=" + status
            + "]";
    }
    
}
