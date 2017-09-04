package com.dreamgear.majiangserver.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dreamgear.majiangserver.net.BaseServer;

public class ServiceInterpreter {
	  private static ServiceInterpreter instance = null;
	  private Class<?> clazz;

	  public static ServiceInterpreter getInstance()
	  {
	    if (instance == null)
	    {
	      instance = new ServiceInterpreter();
	    }
	    return instance;
	  }

	  public Class<?> loadMessageService(BaseServer service)
	  {
	    Class tempClazz = service.getClass();
	    Class annotationClass = DGMessageService.class;

	    Annotation annotation = tempClazz.getAnnotation(annotationClass);
	    if (annotation == null)
	    {
	      return null;
	    }

	    this.clazz = tempClazz;
	    return this.clazz;
	  }

	  public List<Method> loadMessageHandlers()
	  {
	    if (this.clazz == null)
	    {
	      throw new NullPointerException("clazz");
	    }

	    Class annotationClass = DGMessageHandler.class;

	    List results = new ArrayList();

	    Method[] methods = this.clazz.getDeclaredMethods();
	    for (Method method : methods)
	    {
	      Annotation annotation = method.getAnnotation(annotationClass);
	      if (annotation != null)
	      {
	        results.add(method);
	      }

	    }

	    return results;
	  }
}
