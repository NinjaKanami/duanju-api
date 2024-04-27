package com.sqx.modules.utils.wx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * @author fang
 * @date 2020/12/10
 */
public class XmlUtil {

    // xml转java对象
    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        XStream xstream = new XStream() {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            try {
                                return this.realClass(fieldName) != null;
                            } catch (Exception e) {
                                return false;
                            }
                        } else {
                            return super.shouldSerializeMember(definedIn, fieldName);
                        }
                    }
                };
            }
        };

        XStream.setupDefaultSecurity(xstream);
        xstream.autodetectAnnotations(true);
        xstream.alias("xml", clazz);
        xstream.allowTypes(new Class[] { clazz });

        return (T) xstream.fromXML(xml);
    }

    // java对象转xml
    public static <T> String beanToXml(T t, Class<T> clazz) {
        XStream xstream = new XStream(new DomDriver("UTF-8",new XmlFriendlyReplacer("_-", "_")));
        XStream.setupDefaultSecurity(xstream);
        xstream.autodetectAnnotations(true);
        xstream.alias("xml", clazz);
        xstream.allowTypes(new Class[] { clazz });
        return xstream.toXML(t);
    }
}