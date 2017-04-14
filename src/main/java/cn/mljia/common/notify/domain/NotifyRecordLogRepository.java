package cn.mljia.common.notify.domain;

import cn.mljia.common.notify.exception.NegativeException;

/**
 * 
 * @ClassName: NotifyRecordRepository
 * @Description: TODO 通知记录日志仓库
 * @author: marker@mljia.cn
 * @date: 2017�?4�?10�? 下午6:16:35
 */
public interface NotifyRecordLogRepository
{
    
    public void add(NotifyRecordLog notifyRecordLog)
        throws NegativeException;
    
    public void remove(NotifyRecordLog notifyRecordLog)
        throws NegativeException;
    
}
