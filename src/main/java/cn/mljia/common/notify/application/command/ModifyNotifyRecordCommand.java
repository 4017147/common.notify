package cn.mljia.common.notify.application.command;

import java.io.Serializable;
import java.util.Date;

import cn.mljia.ddd.common.AssertionConcern;

public class ModifyNotifyRecordCommand extends AssertionConcern implements Serializable
{
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = 1L;
    
    private String notifyId;
    
    private int notifyTimes;
    
    private String status;
    
    private Date editTime;
    
    public ModifyNotifyRecordCommand(String notifyId, int notifyTimes, String status, Date editTime)
    {
        super();
        this.notifyId = notifyId;
        this.notifyTimes = notifyTimes;
        this.status = status;
        this.editTime = editTime;
    }
    
    public String getNotifyId()
    {
        return notifyId;
    }
    
    public int getNotifyTimes()
    {
        return notifyTimes;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public Date getEditTime()
    {
        return editTime;
    }
    
    @Override
    public String toString()
    {
        return "ModifyNotifyRecordCommand [notifyId=" + notifyId + ", notifyTimes=" + notifyTimes + ", status="
            + status + ", editTime=" + editTime + "]";
    }
    
}
