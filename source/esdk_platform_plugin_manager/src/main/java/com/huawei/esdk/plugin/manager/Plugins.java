package com.huawei.esdk.plugin.manager;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 解析xml
 * <p>
 * <功能详细描述>
 * <p>
 * @author wWX202775
 * @since eSDK Platform V100R500C00
 */
@XmlRootElement(name = "plugins")
@XmlAccessorType(XmlAccessType.FIELD)
public class Plugins
{
    @XmlElement(name = "plugin", type = Plugin.class)
    private List<Plugin> plugins;
    
    public void setPlugins(List<Plugin> plugins)
    {
        this.plugins = plugins;
    }

    public List<Plugin> getPlugins()
    {
        return plugins;
    }
}
