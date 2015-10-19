package com.why.hoolai.sango.amf;

import flex.messaging.io.MapProxy;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ASObject;
import flex.messaging.util.ClassUtil;

@SuppressWarnings({"serial", "unchecked"})
public class HoolaiProxy extends MapProxy{

	public Object createInstance(String className, Class desiredClass)
    {
        Object instance;

        if (className == null || className.length() == 0)
        {
            instance = new ASObject();
        }
        else if (className.startsWith(">")) // Handle [RemoteClass] (no server alias)
        {
            instance = new ASObject();
            ((ASObject)instance).setType(className);
        }
        else
        {
            SerializationContext context = getSerializationContext();
            if ((context.instantiateTypes || className.startsWith("flex.")) && className.indexOf("activity") == -1)
            {
            	instance = ClassUtil.createDefaultInstance(desiredClass, null);
//            	instance = createInstanceFromClassName(className);
            }
            else
            {
                // Just return type info with an ASObject...
                instance = new ASObject();
                ((ASObject)instance).setType(className);
            }
        }
        return instance;
    }
	
}
