package com.huawei.esdk.plugin.manager;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;

import com.huawei.esdk.plugin.inf.IPlugin;
import com.huawei.esdk.plugin.loader.PluginClassLoader;

/**
 * 解析Plugin
 * <p>
 * 加载指定3rd jar
 * <p>
 * @author wWX202775
 * @since eSDK Platform V100R500C00
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Plugin
{
    private static final Logger LOGGER = Logger.getLogger(PluginManager.class);
    
    @XmlElement(name = "name", required = true)
    private String name;
    
    @XmlElement(name = "vserion", required = true)
    private String vserion;
    
    @XmlElement(name = "corporation", required = true)
    private String corporation;
    
    @XmlElement(name = "date", required = true)
    private String date;
    
    @XmlElement(name = "remark", required = true)
    private String remark;
    
    @XmlElement(name = "load", required = true)
    private String load;
    
    @XmlElement(name = "extension", required = true)
    private String extension;
    
    @XmlElement(name = "loadClass", required = true)
    private String loadClass;
    
    @XmlElement(name = "path", required = true)
    private String path;
    
    @XmlElement(name = "md5", required = true)
    private String md5;
    
    @XmlElement(name = "permission", required = true)
    private String permission;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getVserion()
    {
        return vserion;
    }
    
    public void setVserion(String vserion)
    {
        this.vserion = vserion;
    }
    
    public String getCorporation()
    {
        return corporation;
    }
    
    public void setCorporation(String corporation)
    {
        this.corporation = corporation;
    }
    
    public String getDate()
    {
        return date;
    }
    
    public void setDate(String date)
    {
        this.date = date;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getLoad()
    {
        return load;
    }
    
    public void setLoad(String load)
    {
        this.load = load;
    }
    
    public String getExtension()
    {
        return extension;
    }
    
    public void setExtension(String extension)
    {
        this.extension = extension;
    }
    
    public String getLoadClass()
    {
        return loadClass;
    }
    
    public void setLoadClass(String loadClass)
    {
        this.loadClass = loadClass;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public void setPath(String path)
    {
        this.path = path;
    }
    
    public String getMd5()
    {
        return md5;
    }
    
    public void setMd5(String md5)
    {
        this.md5 = md5;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    public void setPermission(String permission)
    {
        this.permission = permission;
    }
    
    /** 
     * 加载指定jar
     *
     * @return ClassLoader
     * @since eSDK Platform V100R500C00
     */
    public ClassLoader load()
    {
        if (name == null || loadClass == null || path == null)
        {
            LOGGER.error("required paramater is null");
            return null;
        }
        try
        {
            ClassLoader classLoader =
                new PluginClassLoader(new URL[] {new URL(path)}, this.getClass().getClassLoader());
            Class<?> clazz = classLoader.loadClass(loadClass);
            if (clazz != null)
            {
                IPlugin plugin = (IPlugin)clazz.newInstance();
                //发布CXF服务
                plugin.init();
            }
            return classLoader;
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.error("ClassNotFoundException");
        }
        catch (InstantiationException e)
        {
            LOGGER.error("InstantiationException");
        }
        catch (IllegalAccessException e)
        {
            LOGGER.error("IllegalAccessException");
        }
        catch (MalformedURLException e)
        {
            LOGGER.error("MalformedURLException");
        }
        return null;
    }
}
