package cn.mljia.common.notify.domain;

import java.util.Date;

import cn.mljia.ddd.common.domain.model.Entity;

public class NotifyMessage extends Entity
{
    
    public NotifyMessage()
    {
        super();
    }
    
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = 1L;
    
    private String messageId; // 事件ID
    
    private String message;// 事件类容
    
    private String type;// 事件类型
    
    private Date date;// 创建时间
    
    public NotifyMessage(String messageId, String message, String type, Date date)
    {
        super();
        this.messageId = messageId;
        this.message = message;
        this.type = type;
        this.date = date;
    }
    
    @Override
    public String toString()
    {
        return "NotifyMessage [messageId=" + messageId + ", message=" + message + ", type=" + type + ", date=" + date
            + "]";
    }
    
}
