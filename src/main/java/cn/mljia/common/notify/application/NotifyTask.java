package cn.mljia.common.notify.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.message.BasicNameValuePair;

import cn.mljia.common.notify.application.command.ModifyNotifyRecordCommand;
import cn.mljia.common.notify.application.command.NotifyMessageCommand;
import cn.mljia.common.notify.domain.NotifyStatusEnum;
import cn.mljia.common.notify.exception.NegativeException;
import cn.mljia.common.notify.utils.DateUtils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * 
 * @ClassName: NotifyTask
 * @Description: TODO 通知任务类.
 * @author: marker@mljia.cn
 * @date: 2017年4月12日 下午6:15:17
 */
public class NotifyTask implements Runnable, Delayed
{
    
    private static final Log LOG = LogFactory.getLog(NotifyTask.class);
    
    private long executeTime;
    
    private NotifyMessageCommand command;
    
    private NotifyMessageApplication messageApplication;
    
    private NotifyParam notifyParam;
    
    public NotifyTask()
    {
    }
    
    public NotifyTask(NotifyMessageCommand command, NotifyMessageApplication messageApplication, NotifyParam notifyParam)
    {
        super();
        this.command = command;
        this.messageApplication = messageApplication;
        this.notifyParam = notifyParam;
        this.executeTime = getExecuteTime(command);
    }
    
    /**
     * 计算任务允许执行的开始时间(executeTime).<br/>
     * 
     * @param record
     * @return
     */
    private long getExecuteTime(NotifyMessageCommand command)
    {
        long lastNotifyTime = command.getLastNotifyTime().getTime(); // 最后通知时间（上次通知时间）
        Integer notifyTimes = command.getNotifyTimes(); // 已通知次数
        LOG.info("===>notifyTimes:" + notifyTimes);
        // Integer nextNotifyTimeInterval = notifyParam.getNotifyParams().get(notifyTimes + 1); // 当前发送次数对应的时间间隔数（分钟数）
        Map<Integer, Integer> nofityRule = command.getNotifyRuleMap();
        Integer nextNotifyTimeInterval = nofityRule.get(notifyTimes + 1); // 当前发送次数对应的时间间隔数（分钟数）
        long nextNotifyTime =
            (nextNotifyTimeInterval == null ? 0 : nextNotifyTimeInterval * 60 * 1000) + lastNotifyTime;
        LOG.info("===>notify id:" + command.getNotifyId() + ", nextNotifyTime:"
            + DateUtils.formatDate(new Date(nextNotifyTime), "yyyy-MM-dd HH:mm:ss SSS"));
        return nextNotifyTime;
    }
    
    /**
     * 比较当前时间(task.executeTime)与任务允许执行的开始时间(executeTime).<br/>
     * 如果当前时间到了或超过任务允许执行的开始时间，那么就返回-1，可以执行。
     */
    public int compareTo(Delayed o)
    {
        NotifyTask task = (NotifyTask)o;
        return executeTime > task.executeTime ? 1 : (executeTime < task.executeTime ? -1 : 0);
    }
    
    public long getDelay(TimeUnit unit)
    {
        return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    /**
     * 执行通知处理.
     */
    public void run()
    {
        
        Integer notifyTimes = command.getNotifyTimes(); // 得到当前通知对象的通知次数
        Integer maxNotifyTimes = command.getLimitNotifyTimes(); // 最大通知次数
        Date notifyTime = new Date(); // 本次通知的时间
        
        // 去通知
        try
        {
            LOG.info("===>notify url " + command.getUrl() + ", notify id:" + command.getNotifyId() + ", notifyTimes:"
                + notifyTimes);
            
            // 执行HTTP通知请求
            OkHttpClient client = new OkHttpClient();
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("notify_body", command.getNotifyBody()));
            Request request =
                new Request.Builder().url(OkHttpUtil.attachHttpGetParams(command.getUrl(), params)).build();
            Response response = client.newCall(request).execute();
            
            command.setEditTime(notifyTime); // 取本次通知时间作为最后修改时间
            command.setNotifyTimes(notifyTimes + 1); // 通知次数+1
            
            String successValue = notifyParam.getSuccessValue(); // 通知成功标识
            String responseMsg = "";
            Integer responseStatus = response.code();
            
            // 写通知日志表
            messageApplication.saveNotifyRecordLogs(command.getNotifyId(),
                command.getNotifyBody(),
                command.getUrl(),
                responseMsg,
                responseStatus);
            LOG.info("===>insert NotifyRecordLog, notifybody:" + command.getNotifyBody());
            
            // 得到返回状态，如果是20X，也就是通知成功
            if (responseStatus == 200 || responseStatus == 201 || responseStatus == 202 || responseStatus == 203
                || responseStatus == 204 || responseStatus == 205 || responseStatus == 206)
            {
                
                responseMsg = response.body().string();
                responseMsg = responseMsg.length() >= 600 ? responseMsg.substring(0, 600) : responseMsg; // 避免异常日志过长
                
                LOG.info("===>notify url " + command.getUrl() + ", notify id:" + command.getNotifyId()
                    + " HTTP_STATUS:" + responseStatus + ",请求返回信息：" + responseMsg);
                
                // 通知成功,更新通知记录为已通知成功（以后不再通知）
                if (responseMsg.trim().equals(successValue))
                {
                    messageApplication.updateNotifyRord(new ModifyNotifyRecordCommand(command.getNotifyId(),
                        command.getNotifyTimes(), NotifyStatusEnum.SUCCESS.name(), notifyTime));
                    return;
                }
                
                // 通知不成功（返回的结果不是success）
                if (command.getNotifyTimes() < maxNotifyTimes)
                {
                    // 判断是否超过重发次数，未超重发次数的，再次进入延迟发送队列
                    messageApplication.addToNotifyTaskDelayQueue(command);
                    messageApplication.updateNotifyRord(new ModifyNotifyRecordCommand(command.getNotifyId(),
                        command.getNotifyTimes(), NotifyStatusEnum.HTTP_REQUEST_SUCCESS.name(), notifyTime));
                    LOG.info("===>update NotifyRecord status to HTTP_REQUEST_SUCCESS, notifyId:"
                        + command.getNotifyId());
                }
                else
                {
                    // 到达最大通知次数限制，标记为通知失败
                    messageApplication.updateNotifyRord(new ModifyNotifyRecordCommand(command.getNotifyId(),
                        command.getNotifyTimes(), NotifyStatusEnum.FAILED.name(), notifyTime));
                    LOG.info("===>update NotifyRecord status to failed, notifyId:" + command.getNotifyId());
                }
                
            }
            else
            {
                
                // 其它HTTP响应状态码情况下
                if (command.getNotifyTimes() < maxNotifyTimes)
                {
                    // 判断是否超过重发次数，未超重发次数的，再次进入延迟发送队列
                    messageApplication.addToNotifyTaskDelayQueue(command);
                    messageApplication.updateNotifyRord(new ModifyNotifyRecordCommand(command.getNotifyId(),
                        command.getNotifyTimes(), NotifyStatusEnum.HTTP_REQUEST_FALIED.name(), notifyTime));
                    
                    LOG.info("===>update NotifyRecord status to HTTP_REQUEST_FALIED, notifyId:" + command.getNotifyId());
                }
                else
                {
                    // 到达最大通知次数限制，标记为通知失败
                    messageApplication.updateNotifyRord(new ModifyNotifyRecordCommand(command.getNotifyId(),
                        command.getNotifyTimes(), NotifyStatusEnum.FAILED.name(), notifyTime));
                    LOG.info("===>update NotifyRecord status to failed, notifyId:" + command.getNotifyId());
                }
            }
            
        }
        catch (NegativeException e)
        {
            LOG.error("===>NotifyTask", e);
        }
        catch (Exception e)
        {
            // 异常
            LOG.error("===>NotifyTask", e);
            try
            {
                messageApplication.addToNotifyTaskDelayQueue(command); // 判断是否超过重发次数，未超重发次数的，再次进入延迟发送队列
                messageApplication.updateNotifyRord(new ModifyNotifyRecordCommand(command.getNotifyId(),
                    command.getNotifyTimes(), NotifyStatusEnum.HTTP_REQUEST_FALIED.name(), notifyTime));
                messageApplication.saveNotifyRecordLogs(command.getNotifyId(),
                    command.getNotifyBody(),
                    command.getUrl(),
                    "",
                    0);
            }
            catch (NegativeException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        
    }
    
}
