package cn.mljia.common.notify.application;

import java.util.concurrent.DelayQueue;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cn.mljia.common.notify.exception.NegativeException;


/**
 * 
 * @ClassName: ApplicationProcess 
 * @Description: TODO 商户通知应用启动类.
 * @author: marker@mljia.cn
 * @date: 2017年4月13日 上午9:41:15
 */
public class ApplicationProcess
{
    private static final Log LOG = LogFactory.getLog(ApplicationProcess.class);
    
    /**
     * 通知任务延时队列，对象只能在其到期时才能从队列中取走。
     */
    public static DelayQueue<NotifyTask> tasks = new DelayQueue<NotifyTask>();
    
    @Resource
    private ThreadPoolTaskExecutor threadPool;
    
    @Resource
    private NotifyMessageApplication messageApplication;
    
    public void init() throws NegativeException
    {
        messageApplication.initNotifyDataFromDB();// 从数据库中取一次数据用来当系统启动时初始化（此处可优化）
        
        startThread(); // 启动任务处理线程
    }
    
    private void startThread()
    {
        LOG.info("==>startThread");
        
        threadPool.execute(new Runnable()
        {
            public void run()
            {
                try
                {
                    while (true)
                    {
                        LOG.info("==>threadPool.getActiveCount():" + threadPool.getActiveCount());
                        LOG.info("==>threadPool.getMaxPoolSize():" + threadPool.getMaxPoolSize());
                        // 如果当前活动线程等于最大线程，那么不执行
                        if (threadPool.getActiveCount() < threadPool.getMaxPoolSize())
                        {
                            LOG.info("==>tasks.size():" + tasks.size());
                            final NotifyTask task = tasks.take(); // 使用take方法获取过期任务,如果获取不到,就一直等待,知道获取到数据
                            if (task != null)
                            {
                                threadPool.execute(new Runnable()
                                {
                                    public void run()
                                    {
                                        tasks.remove(task);
                                        task.run(); // 执行通知处理
                                        LOG.info("==>tasks.size():" + tasks.size());
                                    }
                                });
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    LOG.error("系统异常;", e);
                }
            }
        });
    }
    
}
