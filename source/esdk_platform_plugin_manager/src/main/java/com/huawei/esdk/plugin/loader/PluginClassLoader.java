package com.huawei.esdk.plugin.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 插件类加载器
 * <p>
 * 加载第三方插件
 * <p>
 * @author wWX202775
 * @since eSDK Platform V100R500C00
 */
public class PluginClassLoader extends URLClassLoader
{
    public PluginClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
    }
}
