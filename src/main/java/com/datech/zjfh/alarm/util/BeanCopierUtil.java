package com.datech.zjfh.alarm.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

public class BeanCopierUtil {

    /**
     * 复制单个bean
     */
    public static <S, T> T copyBean(S source, T target) {
        if(source == null || target == null) {
            return null;
        }
        BeanCopier bc = BeanCopier.create(source.getClass(), target.getClass(), false);
        bc.copy(source, target, null);
        return target;
    }

    /**
     * 复制单个bean
     */
    public static <S, T> T copyBean(S source, Class<T> targetClazz) {
        T target = null;
        if(source != null) {
            try {
                BeanCopier bc = BeanCopier.create(source.getClass(), targetClazz, false);
                target = targetClazz.newInstance();
                bc.copy(source, target, null);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return target;
    }

    /**
     * 复制bean列表
     */
    public static <S, T> List<T> copyBeanList(List<S> sourceList, Class<T> targetClazz) {
        List<T> targetList = new ArrayList<T>();
        if(sourceList != null && sourceList.size() > 0) {
            for(S source : sourceList) {
                targetList.add(copyBean(source, targetClazz));
            }
        }
        return targetList;
    }

}
