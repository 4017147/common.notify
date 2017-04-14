package cn.mljia.common.notify.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.mljia.common.CodeType;
import cn.mljia.common.TResult;
import cn.mljia.common.notify.application.NotifyMessageApplication;
import cn.mljia.common.notify.application.NotifyParam;
import cn.mljia.common.notify.application.command.CreateNotifyRecordCommand;
import cn.mljia.common.notify.application.command.NotifyMessageCommand;
import cn.mljia.common.notify.domain.NotifyStatusEnum;
import cn.mljia.common.notify.domain.NotifyTypeEnum;
import cn.mljia.common.notify.exception.NegativeException;
import cn.mljia.ddd.common.serializer.ObjectSerializer;

/**
 * 
 * @ClassName: NotifyMessageAgent
 * @Description: TODO
 * @author: marker@mljia.cn
 * @date: 2017年4月13日 上午10:21:41
 */
@RestController
@RequestMapping(value = "/notify")
public class NotifyMessageAgent
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyMessageAgent.class);
    
    @Resource
    private NotifyParam notifyParam;
    
    @Resource
    private NotifyMessageApplication notifyMessageApplication;
    
    @RequestMapping(value = "/proxy", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<TResult> proxy(@RequestParam(value = "url", required = true) String url,
        @RequestParam(value = "notify_body", required = true) String notifyBody,
        @RequestParam(value = "timestamp") Long timestamp, @RequestParam(value = "sign") String sign)
        throws NegativeException
    {
        TResult result = new TResult(CodeType.V_1);
        List<String> parameters = new ArrayList<String>();
        parameters.add("url" + ":" + url);
        parameters.add("notify_body" + ":" + notifyBody);
        parameters.add("timestamp" + ":" + timestamp);
        String bksign = sign("www.mljia.cn", parameters);
        if (StringUtils.isNotBlank(sign) && StringUtils.isNotEmpty(sign) && bksign.equals(sign))
        {
            
            try
            {
                LOGGER.warn("notify proxy : URL=========>>>" + url + "NOTIFY_BODY=========>>>" + notifyBody);
                String notifyId = UUID.randomUUID().toString().replace("-", "");
                Integer maxNotifyTimes = notifyParam.getMaxNotifyTimes();
                Map<Integer, Integer> notifyParams = notifyParam.getNotifyParams();
                notifyMessageApplication.createNotifyRecord(new CreateNotifyRecordCommand(notifyId, notifyParams,
                    maxNotifyTimes, url, notifyBody));
                notifyMessageApplication.addToNotifyTaskDelayQueue(new NotifyMessageCommand(new Date(),
                    ObjectSerializer.instance().serialize(notifyParams), new Date(), 0, maxNotifyTimes, url,
                    notifyBody, NotifyTypeEnum.MERCHANT.name(), notifyId, new Date(), NotifyStatusEnum.CREATED.name()));
                result.setStatus(CodeType.V_200);
                result.setContent("success");
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LOGGER.error("notify proxy Exception e:" + e.getMessage(), e);
                result = new TResult(CodeType.V_500, "notify proxy error.");
            }
        }
        else
        {
            result = new TResult(CodeType.V_501, "请求签名错误,请确认后再试!");
        }
        return new ResponseEntity<TResult>(result, HttpStatus.OK);
    }
    
    // 签名
    public String sign(String token, List<String> list)
    {
        Collections.sort(list);
        StringBuffer sb = new StringBuffer();
        sb.append(token);
        for (String s : list)
        {
            sb.append(s);
            sb.append("|");
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        LOGGER.debug("request archetype sign:====>{}" + str);
        LOGGER.debug("request conver    sign:====>{}" + DigestUtils.sha1Hex(str));
        return DigestUtils.sha1Hex(str);
    }
}
