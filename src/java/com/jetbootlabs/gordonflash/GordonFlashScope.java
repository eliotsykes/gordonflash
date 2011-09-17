package com.jetbootlabs.gordonflash;

import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import org.codehaus.groovy.grails.web.servlet.FlashScope;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Majority of code copied from GrailsFlashScope.
 */
public class GordonFlashScope implements FlashScope, Serializable {

    private static final long serialVersionUID = -3639251340647089542L;
    private Map current = new ConcurrentHashMap();
    private Map next = new ConcurrentHashMap();
    public static final String ERRORS_PREFIX = "org.codehaus.groovy.grails.ERRORS_";
    private static final String ERRORS_PROPERTY = "errors";

    public void next() {
        current.clear();
        current = new ConcurrentHashMap(next);
        next.clear();
        reassociateObjectsWithErrors(current);
    }

    public Map getNow() {
        return current;
    }

    private void reassociateObjectsWithErrors(Map scope) {
        for (Object key : scope.keySet()) {
            Object value = scope.get(key);
            if (value instanceof Map) {
                reassociateObjectsWithErrors((Map) value);
            }
            reassociateObjectWithErrors(scope, value);
        }
    }

    private void reassociateObjectWithErrors(Map scope, Object value) {
        if (value instanceof Collection) {
            Collection values = (Collection)value;
            for (Object val : values) {
                reassociateObjectWithErrors(scope, val);
            }
        }
        else {
            String errorsKey = ERRORS_PREFIX + System.identityHashCode(value);
            Object errors = scope.get(errorsKey);
            if (value!=null && errors != null) {
                MetaClass mc = GroovySystem.getMetaClassRegistry().getMetaClass(value.getClass());
                if (mc.hasProperty(value, ERRORS_PROPERTY)!=null) {
                    mc.setProperty(value, ERRORS_PROPERTY, errors);
                }
            }
        }
    }

    public int size() {
        return current.size() + next.size();
    }

    public void clear() {
        current.clear();
        next.clear();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(Object key) {
        return (current.containsKey(key) || next.containsKey(key));
    }

    public boolean containsValue(Object value) {
        return (current.containsValue(value) || next.containsValue(value));
    }

    public Collection values() {
        Collection c = new ArrayList();
        c.addAll(current.values());
        c.addAll(next.values());
        return c;
    }

    public void putAll(Map t) {
        for (Map.Entry<Object, Object> entry : ((Map<Object,Object>)t).entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public Set entrySet() {
        Set keySet = new HashSet();
        keySet.addAll(current.entrySet());
        keySet.addAll(next.entrySet());
        return keySet;
    }

    public Set keySet() {
        Set keySet = new HashSet();
        keySet.addAll(current.keySet());
        keySet.addAll(next.keySet());
        return keySet;
    }

    public Object get(Object key) {
        if (next.containsKey(key)) {
            return next.get(key);
        }
        return current.get(key);
    }

    public Object remove(Object key) {
        if (current.containsKey(key)) {
            return current.remove(key);
        }

        return next.remove(key);
    }

    public Object put(Object key, Object value) {
        // create the session if it doesn't exist
        registerWithSessionIfNecessary();
        if (current.containsKey(key)) {
            current.remove(key);
        }
        storeErrorsIfPossible(next,value);

        if (value == null) {
            return next.remove(key);
        }

        return next.put(key,value);
    }

    private void storeErrorsIfPossible(Map scope,Object value) {
        if (value == null) {
            return;
        }

        if (value instanceof Collection) {
            Collection values = (Collection)value;
            for (Object val : values) {
                storeErrorsIfPossible(scope, val);
            }
        }
        else if (value instanceof Map) {
            Map map = (Map)value;
            Collection keys = new LinkedList(map.keySet());
            for (Object key : keys) {
                Object val = map.get(key);
                storeErrorsIfPossible(map, val);
            }
        }
        else {
            MetaClass mc = GroovySystem.getMetaClassRegistry().getMetaClass(value.getClass());
            if (mc.hasProperty(value, ERRORS_PROPERTY)!=null) {
                Object errors = mc.getProperty(value, ERRORS_PROPERTY);
                if (errors != null) {
                    scope.put(ERRORS_PREFIX + System.identityHashCode(value), errors);
                }
            }
        }
    }

    private void registerWithSessionIfNecessary() {
        GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.currentRequestAttributes();
        HttpSession session = webRequest.getCurrentRequest().getSession(true);
        if (session.getAttribute(GrailsApplicationAttributes.FLASH_SCOPE) == null) {
            session.setAttribute(GrailsApplicationAttributes.FLASH_SCOPE, this);
        }
    }
}
