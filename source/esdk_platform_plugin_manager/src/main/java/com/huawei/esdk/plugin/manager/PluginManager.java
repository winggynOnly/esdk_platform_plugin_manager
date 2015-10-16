package com.huawei.esdk.plugin.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;

/**
 * 管理插件
 * <p>
 * 管理插件权限，验证插件文件真伪
 * <p>
 * @author wWX202775
 * @since eSDK Platform V100R500C00
 */
public class PluginManager
{
    private static final Logger LOGGER = Logger.getLogger(PluginManager.class);
    
    private Map<ClassLoader, String[]> pluginMap = new HashMap<ClassLoader, String[]>();
    
    private List<Plugin> pluginList = null;
    
    private PluginManager()
    {
    }
    
    public static final PluginManager getInstance()
    {
        return ApplicationContextUtil.getBean("PluginManager"); 
    }
    
    public void init()
    {
        try
        {
            JAXBContext jaxb = JAXBContext.newInstance(Plugins.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            Plugins plugins =
                (Plugins)unmarshaller.unmarshal(getClass().getClassLoader().getResourceAsStream("plugins.xml"));
            pluginList = plugins.getPlugins();
        }
        catch (JAXBException e)
        {
            LOGGER.error("JAXBException");
        }
        
        //Load Plugins
        LOGGER.info("Load Plugins begin");
        if (pluginList != null && !pluginList.isEmpty())
        {
            for (Plugin plugin : pluginList)
            {
                if (checkMD5(plugin))
                {
                    ClassLoader classLoader = plugin.load();
                    LOGGER.info("Plugin " + plugin.getName() + " is valid and loaded.");
                    String permission = plugin.getPermission();
                    pluginMap.put(classLoader, permission.split(";"));
                }
                
            }
        }
        LOGGER.info("Load Plugins end");
    }
    
    public Map<ClassLoader, String[]> getPluginPermission()
    {
        return pluginMap;
    }
    
    public List<Plugin> getPluginList()
    {
        return pluginList;
    }
    
    private boolean checkMD5(Plugin plugin)
    {
        String filePath = plugin.getPath().substring(10, plugin.getPath().length() - 2);
        String md5 = md5(new File(filePath));
        return md5.equalsIgnoreCase(plugin.getMd5());
    }
    
    
    /** 
     * 计算文件MD5
     *
     * @param file
     * @return
     * @since eSDK Platform V100R500C00
     */
    private String md5(File file)
    {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        
        MessageDigest messageDigest = null;
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error("md5 - NoSuchAlgorithmException");
        }
        
        FileInputStream in = null;
        FileChannel ch = null;
        try
        {
            in = new FileInputStream(file);
            ch = in.getChannel();
            ByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messageDigest.update(byteBuffer);
        }
        catch (IOException e)
        {
            LOGGER.error("md5 - IOException : read file");
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("md5 - IOException : close FileInputStream");
                }
            }
            if (ch != null)
            {
                try
                {
                    ch.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("md5 - IOException : close FileChannel");
                }
            }
        }
        
        byte[] md = messageDigest.digest();
        int len = md.length;
        char str[] = new char[len * 2];
        for (int i = 0, k = 0; i < len; i++)
        {
            byte b = md[i];
            str[k++] = hexDigits[b >>> 4 & 0xf];
            str[k++] = hexDigits[b & 0xf];
        }
        return new String(str);
    }
}
