package cn.mljia.common.notify.application;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import cn.mljia.common.notify.application.command.CreateNotifyRecordCommand;
import cn.mljia.common.notify.application.command.ModifyNotifyRecordCommand;
import cn.mljia.common.notify.application.command.NotifyMessageCommand;
import cn.mljia.common.notify.domain.NotifyRecord;
import cn.mljia.common.notify.domain.NotifyRecordLog;
import cn.mljia.common.notify.domain.NotifyRecordLogRepository;
import cn.mljia.common.notify.domain.NotifyRecordRepository;
import cn.mljia.common.notify.domain.NotifyStatusEnum;
import cn.mljia.common.notify.domain.NotifyTypeEnum;
import cn.mljia.common.notify.exception.NegativeException;
import cn.mljia.common.notify.utils.DateUtils;
import cn.mljia.ddd.common.event.annotation.EventListener;

/**
 * 
 * @ClassName: NotifyMessageApplication
 * @Description: TODO 通知记录持久应用服务
 * @author: marker@mljia.cn
 * @date: 2017�?4�?10�? 下午6:19:37
 */
@Service
public class NotifyMessageApplication
{
    
    private static final Log LOGGER = LogFactory.getLog(NotifyMessageApplication.class);
    
    @Resource
    private NotifyRecordRepository notifyRecordRepository;
    
    @Resource
    private NotifyRecordLogRepository notifyRecordLogRepository;
    
    @Resource
    private NotifyParam notifyParam;
    
    @Resource
    private HibernateTransactionManager hibernateTransactionManager;
    
    private static final Integer PAGE_SIZE = 10;
    
    /**
     * 
     * @Title: saveNotifyRecord
     * @Description: TODO 创建通知记录.
     * @param command
     * @return: void
     * @throws NegativeException
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, NegativeException.class})
    @EventListener(isListening = false)
    public void createNotifyRecord(CreateNotifyRecordCommand command)
        throws NegativeException
    {
        NotifyRecord notifyRecord =
            new NotifyRecord(command.getNotifyId(), command.getNotifyParams(), command.getLimitNotifyTimes(),
                command.getUrl(), command.getNotifyBody(), NotifyStatusEnum.CREATED, NotifyTypeEnum.MERCHANT);
        notifyRecordRepository.add(notifyRecord);
    }
    
    /**
     * 更新商户通知记录.<br/>
     *
     * @param id
     * @param notifyTimes 通知次数.<br/>
     * @param status 通知状�??.<br/>
     * @return 更新结果
     * @throws NegativeException
     */
    public void updateNotifyRord(ModifyNotifyRecordCommand command)
        throws NegativeException
    {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = this.hibernateTransactionManager.getTransaction(transactionDefinition);
        try
        {
            NotifyRecord notifyRecord = notifyRecordRepository.notifyRecordOfId(command.getNotifyId());
            notifyRecord.modifyNotifyRecord(command.getNotifyTimes(), command.getStatus(), command.getEditTime());
            notifyRecordRepository.add(notifyRecord);
            this.hibernateTransactionManager.commit(status);
        }
        catch (Exception e)
        {
            this.hibernateTransactionManager.rollback(status);
            throw e;
        }
    }
    
    /**
     * 
     * @Title: saveNotifyRecordLogs
     * @Description: TODO 创建商户通知日志记录.
     * @param notifyId
     * @param notifyBody
     * @param request
     * @param response
     * @param httpStatus
     * @throws NegativeException
     * @return: void
     */
    public void saveNotifyRecordLogs(String notifyId, String notifyBody, String request, String response, int httpStatus)
        throws NegativeException
    {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = this.hibernateTransactionManager.getTransaction(transactionDefinition);
        try
        {
            NotifyRecordLog notifyRecordLog = new NotifyRecordLog(notifyId, request, response, notifyBody, httpStatus);
            notifyRecordLogRepository.add(notifyRecordLog);
            this.hibernateTransactionManager.commit(status);
        }
        catch (Exception e)
        {
            this.hibernateTransactionManager.rollback(status);
            throw e;
        }
    }
    
    /**
     * 
     * @Title: addToNotifyTaskDelayQueue
     * @Description: TODO 将传过来的对象进行通知次数判断，决定是否放在任务队列中.
     * @param notifyId 通知ID
     * @param notifyTimes 通知次数
     * @param maxNotifyTimes 最大通知次数
     * @return: void
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, NegativeException.class})
    @EventListener(isListening = false)
    public void addToNotifyTaskDelayQueue(NotifyMessageCommand command)
    {
        if (command == null)
        {
            return;
        }
        
        LOGGER.info("===>addToNotifyTaskDelayQueue notify id:" + command.getNotifyId());
        
        Integer notifyTimes = command.getNotifyTimes(); // 通知次数
        
        Integer maxNotifyTimes = command.getLimitNotifyTimes(); // 最大通知次数
        
        if (command.getNotifyTimes().intValue() == 0)
        {
            command.setLastNotifyTime(new Date()); // 第一次发送(取当前时间)
        }
        else
        {
            command.setLastNotifyTime(command.getEditTime()); // 非第一次发送（取上一次修改时间，也是上一次发送时间）
        }
        
        if (notifyTimes < maxNotifyTimes)
        {
            // 未超过最大通知次数，继续下一次通知
            LOGGER.info("===>notify id:" + command.getNotifyId() + ", 上次通知时间lastNotifyTime:"
                + DateUtils.formatDate(command.getLastNotifyTime(), "yyyy-MM-dd HH:mm:ss SSS"));
            ApplicationProcess.tasks.put(new NotifyTask(command, this, notifyParam));
        }
        
    }
    
    /**
     * 从数据库中取一次数据用来当系统启动时初始化
     * 
     * @throws NegativeException
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, NegativeException.class})
    @EventListener(isListening = false)
    public void initNotifyDataFromDB()
        throws NegativeException
    {
        LOGGER.info("===>init get notify data from database");
        
        try
        {
            Integer totalCount = notifyRecordRepository.notifyRecordOfByPageCount(notifyParam.getMaxNotifyTimes());
            
            int totalPage; // 总页数
            if (totalCount % PAGE_SIZE == 0)
            {
                totalPage = totalCount / PAGE_SIZE;
            }
            else
            {
                totalPage = totalCount / PAGE_SIZE + 1;
            }
            
            for (int i = 1; i <= totalPage; i++)
            {
                List<NotifyRecord> list =
                    notifyRecordRepository.notifyRecordOfByPage(PAGE_SIZE, i, notifyParam.getMaxNotifyTimes());
                
                for (NotifyRecord notifyRecord : list)
                {
                    addToNotifyTaskDelayQueue(new NotifyMessageCommand(notifyRecord.createTime(),
                        notifyRecord.notifyRule(), notifyRecord.lastNotifyTime(), notifyRecord.notifyTimes(),
                        notifyRecord.limitNotifyTimes(), notifyRecord.url(), notifyRecord.notifyBody(),
                        notifyRecord.notifyType(), notifyRecord.notifyId(), notifyRecord.editTime(),
                        notifyRecord.status()));
                }
            }
        }
        catch (NegativeException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOGGER.error("initNotifyDataFromDB process NegativeException e:" + e.getMessage(), e);
            throw e;
        }
    }
    
}
