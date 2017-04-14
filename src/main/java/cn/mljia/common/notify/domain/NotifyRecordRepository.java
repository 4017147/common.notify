package cn.mljia.common.notify.domain;

import java.util.List;

import cn.mljia.common.notify.exception.NegativeException;
 

/**
 * 
 * @ClassName: NotifyRecordRepository
 * @Description: TODO 通知记录仓库
 * @author: marker@mljia.cn
 * @date: 2017�?4�?10�? 下午6:16:35
 */
public interface NotifyRecordRepository
{
    
    public void add(NotifyRecord notifyRecord)  throws NegativeException;
    
    public void remove(NotifyRecord notifyRecord) throws NegativeException;
    
    public NotifyRecord notifyRecordOfId(String id) throws NegativeException;
    
    public List<NotifyRecord> notifyRecordOfByPage(Integer pageSize, Integer pageNumber,Integer maxNotifyTimes) throws NegativeException;
    
    public Integer notifyRecordOfByPageCount(Integer maxNotifyTimes) throws NegativeException;
}
