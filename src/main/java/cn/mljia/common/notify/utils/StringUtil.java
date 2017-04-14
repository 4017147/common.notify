package cn.mljia.common.notify.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @ClassName: StringUtil
 * @Description: TODO String字符串工具类.
 * @author: Administrator
 * @date: 2017年4月13日 上午10:02:58
 */
public final class StringUtil
{
    
    private static final Log LOG = LogFactory.getLog(StringUtil.class);
    
    /**
     * 私有构造方法,将该工具类设为单例模式.
     */
    private StringUtil()
    {
    }
    
    /**
     * 函数功能说明 ： 判断字符串是否为空 . 修改者名字： 修改日期： 修改内容：
     * 
     * @参数： @param str
     * @参数： @return
     * @return boolean
     * @throws
     */
    public static boolean isEmpty(String str)
    {
        return null == str || "".equals(str);
    }
    
    /**
     * 函数功能说明 ： 判断对象数组是否为空. 修改者名字： 修改日期： 修改内容：
     * 
     * @参数： @param obj
     * @参数： @return
     * @return boolean
     * @throws
     */
    public static boolean isEmpty(Object[] obj)
    {
        return null == obj || 0 == obj.length;
    }
    
    /**
     * 函数功能说明 ： 判断对象是否为空. 修改者名字： 修改日期： 修改内容：
     * 
     * @参数： @param obj
     * @参数： @return
     * @return boolean
     * @throws
     */
    public static boolean isEmpty(Object obj)
    {
        if (null == obj)
        {
            return true;
        }
        if (obj instanceof String)
        {
            return ((String)obj).trim().isEmpty();
        }
        return !(obj instanceof Number) ? false : false;
    }
    
    /**
     * 函数功能说明 ： 判断集合是否为空. 修改者名字： 修改日期： 修改内容：
     * 
     * @参数： @param obj
     * @参数： @return
     * @return boolean
     * @throws
     */
    public static boolean isEmpty(List<?> obj)
    {
        return null == obj || obj.isEmpty();
    }
    
    /**
     * 函数功能说明 ： 判断Map集合是否为空. 修改者名字： 修改日期： 修改内容：
     * 
     * @参数： @param obj
     * @参数： @return
     * @return boolean
     * @throws
     */
    public static boolean isEmpty(Map<?, ?> obj)
    {
        return null == obj || obj.isEmpty();
    }
    
    /**
     * 函数功能说明 ： 获得文件名的后缀名. 修改者名字： 修改日期： 修改内容：
     * 
     * @参数： @param fileName
     * @参数： @return
     * @return String
     * @throws
     */
    public static String getExt(String fileName)
    {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    /**
     * 获取去掉横线的长度为32的UUID串.
     * 
     * @author WuShuicheng.
     * @return uuid.
     */
    public static String get32UUID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 获取带横线的长度为36的UUID串.
     * 
     * @author WuShuicheng.
     * @return uuid.
     */
    public static String get36UUID()
    {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 验证一个字符串是否完全由纯数字组成的字符串，当字符串为空时也返回false.
     * 
     * @author WuShuicheng .
     * @param str 要判断的字符串 .
     * @return true or false .
     */
    public static boolean isNumeric(String str)
    {
        if (StringUtils.isBlank(str))
        {
            return false;
        }
        else
        {
            return str.matches("\\d*");
        }
    }
    
    /**
     * 计算采用utf-8编码方式时字符串所占字节数
     * 
     * @param content
     * @return
     */
    public static int getByteSize(String content)
    {
        int size = 0;
        if (null != content)
        {
            try
            {
                // 汉字采用utf-8编码时占3个字节
                size = content.getBytes("utf-8").length;
            }
            catch (UnsupportedEncodingException e)
            {
                LOG.error(e);
            }
        }
        return size;
    }
    
    /**
     * 函数功能说明 ： 截取字符串拼接in查询参数. 修改者名字： 修改日期： 修改内容：
     * 
     * @参数： @param ids
     * @参数： @return
     * @return String
     * @throws
     */
    public static List<String> getInParam(String param)
    {
        boolean flag = param.contains(",");
        List<String> list = new ArrayList<String>();
        if (flag)
        {
            list = Arrays.asList(param.split(","));
        }
        else
        {
            list.add(param);
        }
        return list;
    }
    
}
