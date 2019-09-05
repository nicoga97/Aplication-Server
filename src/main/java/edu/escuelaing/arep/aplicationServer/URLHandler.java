package edu.escuelaing.arep.aplicationServer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class URLHandler {
    private static HashMap<String, Handler> URLHandlerList;

    public static void loadWebAplications() {
        List<Method> methods = new ArrayList<>();
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("edu.escuelaing.arep.aplicationServer.apps"))
                .setScanners(new SubTypesScanner(false))
        );
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        for (Class loadedClass : allClasses) {
            for (Method method : loadedClass.getMethods()) {
                if (method.isAnnotationPresent(Web.class)) {
                    methods.add(method);
                }
            }
        }

    }
}
