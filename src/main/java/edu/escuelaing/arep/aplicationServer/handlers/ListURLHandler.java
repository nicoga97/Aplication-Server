package edu.escuelaing.arep.aplicationServer.handlers;

import edu.escuelaing.arep.aplicationServer.service.Web;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

public class ListURLHandler {
    private static final String aplicationServerRoot = "/apps";
    private static HashMap<String, Method> URLHandlerList = new HashMap<>();

    public void loadWebAplications() throws InvocationTargetException, IllegalAccessException {
        System.out.println("Loading web aplication handlers...");
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("edu.escuelaing.arep.aplicationServer.apps"))
                .setScanners(new SubTypesScanner(false))
        );
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        for (Class loadedClass : allClasses) {
            for (Method method : loadedClass.getMethods()) {
                if (method.isAnnotationPresent(Web.class)) {
                    URLHandlerList.put(aplicationServerRoot + "/" + method.getAnnotation(Web.class).value()
                            , method);

                    System.out.println("Handler loaded for: " + aplicationServerRoot + "/" + method.getAnnotation(Web.class).value());

                }
            }
        }
    }

    public HashMap<String, Method> getURLHandlerList() {
        return URLHandlerList;
    }
}
