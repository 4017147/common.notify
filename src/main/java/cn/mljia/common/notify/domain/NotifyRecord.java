package cn.mljia.common.notify.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import cn.mljia.ddd.common.domain.model.ConcurrencySafeEntity;
import cn.mljia.ddd.common.serializer.ObjectSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: RpNotifyRecord
 * @Description: TODO 通知记录
 * @author: marker@mljia.cn
 * @date: 2017�?4�?10�? 下午6:07:54
 */
public class NotifyRecord extends ConcurrencySafeEntity implements Serializable
{
    
    private static final long serialVersionUID = -6104194914044220447L;
    
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
    
    public NotifyRecord()
    {
        super();
    }
    
    public NotifyRecord(String notifyId, Map<Integer, Integer> notifyRule, Integer limitNotifyTimes, String url,
        String notifyBody, NotifyStatusEnum status, NotifyTypeEnum type)
    {
        super();
        this.setNotifyId(notifyId);
        this.setCreateTime(new Date());
        this.setEditTime(new Date());
        this.setLastNotifyTime(new Date());
        this.setNotifyRule(ObjectSerializer.instance().serialize(notifyRule));
        this.setNotifyTimes(0);
        this.setLimitNotifyTimes(notifyRule.size());
        this.setUrl(url);
        this.setNotifyBody(notifyBody);
        this.setNotifyType(type.name());
        this.setStatus(status.name());
        
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
    
    public void modifyNotifyRecord(int notifyTimes, String status, Date editTime)
    {
        this.setNotifyTimes(notifyTimes);
        this.setStatus(status);
        this.setEditTime(editTime);
        this.setLastNotifyTime(editTime);
    }
    
    public void modifyLastNotifyTime()
    {
        if (this.notifyTimes.intValue() == 0)
        {
            this.setLastNotifyTime(new Date()); // 第一次发�?(取当前时�?)
        }
        else
        {
            this.setLastNotifyTime(this.editTime); // 非第�?次发送（取上�?次修改时间，也是上一次发送时间）
        }
    }
    
    private void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    private void setNotifyRule(String notifyRule)
    {
        this.notifyRule = notifyRule;
    }
    
    private void setLastNotifyTime(Date lastNotifyTime)
    {
        this.lastNotifyTime = lastNotifyTime;
    }
    
    private void setNotifyTimes(Integer notifyTimes)
    {
        this.notifyTimes = notifyTimes;
    }
    
    private void setLimitNotifyTimes(Integer limitNotifyTimes)
    {
        this.limitNotifyTimes = limitNotifyTimes;
    }
    
    private void setUrl(String url)
    {
        this.url = url;
    }
    
    private void setNotifyBody(String notifyBody)
    {
        this.notifyBody = notifyBody;
    }
    
    private void setNotifyType(String notifyType)
    {
        this.notifyType = notifyType;
    }
    
    private void setNotifyId(String notifyId)
    {
        this.notifyId = notifyId;
    }
    
    private void setEditTime(Date editTime)
    {
        this.editTime = editTime;
    }
    
    private void setStatus(String status)
    {
        this.status = status;
    }
    
    public String notifyId()
    {
        return this.notifyId;
    }
    
    public Integer notifyTimes()
    {
        return this.notifyTimes;
    }
    
    public Integer limitNotifyTimes()
    {
        return this.limitNotifyTimes;
    }
    
    public Date lastNotifyTime()
    {
        return this.lastNotifyTime;
    }
    
    public Map<Integer, Integer> notifyRuleMap()
    {
        return this.parseData(this.notifyRule);
    }
    
    public String notifyRule()
    {
        return this.notifyRule;
    }
    
    public String url()
    {
        return this.url;
    }
    
    public String notifyBody()
    {
        return this.notifyBody;
    }
    
    public Date createTime()
    {
        return this.createTime;
    }
    
    public String notifyType()
    {
        return this.notifyType;
    }
    
    public Date editTime()
    {
        return this.editTime;
    }
    
    public String status()
    {
        return this.status;
    }
    
    @Override
    public String toString()
    {
        return "NotifyRecord [createTime=" + createTime + ", notifyRule=" + notifyRule + ", notifyTimes=" + notifyTimes
            + ", limitNotifyTimes=" + limitNotifyTimes + ", url=" + url + ", notifyBody=" + notifyBody
            + ", notifyType=" + notifyType + ", notifyId=" + notifyId + ", editTime=" + editTime + ", status=" + status
            + "]";
    }
    
}
