package com.blackeye.worth.core.params.extend;


import com.blackeye.worth.core.annotations.DateTimeRangeSearch;
import com.blackeye.worth.core.annotations.KeyWordSearch;
import com.blackeye.worth.core.annotations.NumberRangeSearch;
import com.blackeye.worth.model.QBaseDojo;
import com.blackeye.worth.utils.AnnotationUtil;
import com.blackeye.worth.utils.DateX;
import com.blackeye.worth.utils.ReflectUtil;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryDslUtils {

    public static Predicate mergePredicatesByAnd(Predicate predicate, Predicate... predicates) {
        final Predicate[] resultPredicate = new Predicate[]{predicate};
        if (predicate != null) {
            Arrays.asList(predicates).stream()
                    .filter(p -> p != null)
                    .forEach(andP -> resultPredicate[0] = ExpressionUtils.and(resultPredicate[0], andP));
        }
        return resultPredicate[0];
    }

    public static Predicate inQueryBuild(String entitySimpleName, MultiValueMap<String, String> paramsMap) {
        Predicate inPredicate = null;
        for (Map.Entry<String, List<String>> entry : paramsMap.entrySet()) {
            if (entry.getKey().endsWith("_in")) {
                System.out.println(entry.getValue());
                entitySimpleName = toUpperCaseFirstOne(entitySimpleName);
                Class entityClazz = getClassByModelName(entitySimpleName);
                Class qEntityClazz = getClassByModelName("Q" + entitySimpleName);
                if (entityClazz == null || qEntityClazz == null) return inPredicate;
                String value = entry.getValue().toString();
                String key = entry.getKey().split("_in")[0];
                if (!StringUtils.isEmpty(value)) {
                    if (value.startsWith("[") && value.endsWith("]")) {
                        int length = value.length();
                        value = value.substring(1, length - 1);
                    }
                    String[] arr = value.split(",");
                    // 获得字段
                    try {
                        Object qobj = qEntityClazz.getField(toLowerCaseFirstOne(entitySimpleName)).get(qEntityClazz);
                        StringPath stringPath = (StringPath) ReflectUtil.get(qobj, qEntityClazz, key);
                        if (inPredicate == null) {
                            inPredicate = stringPath.in(arr);
                        } else {
                            inPredicate = stringPath.in(arr).and(inPredicate);
                        }
                        return inPredicate;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }

            }
        }
        return inPredicate;
    }


    public static Predicate keywordQueryBuild(String entitySimpleName, MultiValueMap<String, String> paramsMap, String keywordOfKey) {
        Predicate keywordPredicate = null;
        entitySimpleName = toUpperCaseFirstOne(entitySimpleName);
        Class entityClazz = getClassByModelName(entitySimpleName);
        Class qEntityClazz = getClassByModelName("Q" + entitySimpleName);
        if (entityClazz == null || qEntityClazz == null) return keywordPredicate;
        //需要特殊处理的字段
        String keyword = null;
        if (paramsMap.get(keywordOfKey) != null)
            keyword = paramsMap.get(keywordOfKey).get(0);
        if (!StringUtils.isEmpty(keyword)) {
            keyword = "%" + keyword + "%";
            // 获得字段注解
            List<Field> feilds = AnnotationUtil.getSpecifyAnnotationFileds(entityClazz, KeyWordSearch.class);
            try {
                Object qobj = qEntityClazz.getField(toLowerCaseFirstOne(entitySimpleName)).get(qEntityClazz);
                for (Field f : feilds) {
                    StringPath stringPath = (StringPath) ReflectUtil.get(qobj, qEntityClazz, f.getName());
                    if (keywordPredicate == null) {
                        keywordPredicate = stringPath.like(keyword);
                    } else {
                        keywordPredicate = stringPath.like(keyword).or(keywordPredicate);
                    }
                }
                return keywordPredicate;
//                predicate=ExpressionUtils.and(predicate,keyWordPredicate);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } finally {
            }
        }
        return keywordPredicate;
    }

    public static Predicate dateRangeQueryBuild(String entitySimpleName, MultiValueMap<String, String> paramsMap) {
        Predicate dateRangePredicate = null;
        entitySimpleName = toUpperCaseFirstOne(entitySimpleName);
        Class entityClazz = getClassByModelName(entitySimpleName);
        Class qEntityClazz = getClassByModelName("Q" + entitySimpleName);
        if (entityClazz == null || qEntityClazz == null) return dateRangePredicate;
        // 获得字段注解
        List<Field> feilds = AnnotationUtil.getSpecifyAnnotationFileds(entityClazz, DateTimeRangeSearch.class);
        try {
            Object qobj = qEntityClazz.getField(toLowerCaseFirstOne(entitySimpleName)).get(qEntityClazz);
            for (Field f : feilds) {
                DateTimePath dateTimePath = (DateTimePath) ReflectUtil.get(qobj, qEntityClazz, f.getName());
                if (dateRangePredicate == null) {
                    dateRangePredicate = dealTimeRangeBinding(dateTimePath, paramsMap);
                } else {
                    dateRangePredicate = ExpressionUtils.and(dateRangePredicate, dealTimeRangeBinding(dateTimePath, paramsMap));
                }
            }
            return dateRangePredicate;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
        }
        return dateRangePredicate;
    }


    public static Predicate numberRangeQueryBuild(String entitySimpleName, MultiValueMap<String, String> paramsMap) {
        Predicate numberRangePredicate = null;
        entitySimpleName = toUpperCaseFirstOne(entitySimpleName);
        Class entityClazz = getClassByModelName(entitySimpleName);
        Class qEntityClazz = getClassByModelName("Q" + entitySimpleName);
        if (entityClazz == null || qEntityClazz == null) return numberRangePredicate;
        // 获得字段注解
        List<Field> feilds = AnnotationUtil.getSpecifyAnnotationFileds(entityClazz, NumberRangeSearch.class);
        try {
            Object qobj = qEntityClazz.getField(toLowerCaseFirstOne(entitySimpleName)).get(qEntityClazz);
            for (Field f : feilds) {
                NumberPath numberPath = (NumberPath) ReflectUtil.get(qobj, qEntityClazz, f.getName());
                if (numberRangePredicate == null) {
                    numberRangePredicate = dealNumberBinding(numberPath, paramsMap);
                } else {
                    numberRangePredicate = ExpressionUtils.and(numberRangePredicate, dealNumberBinding(numberPath, paramsMap));
                }
            }
            return numberRangePredicate;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
        }
        return numberRangePredicate;
    }


    public static Predicate dealTimeRangeBinding(DateTimePath dateTimePath, MultiValueMap<String, String> paramsMap) {
        String name = dateTimePath.getMetadata().getName();
        Predicate predicate = null;
        if (paramsMap.get(name + "_begin") != null && paramsMap.get(name + "_end") != null) {
            String beginTime = paramsMap.get(name + "_begin").get(0);
            String endTime = paramsMap.get(name + "_end").get(0);
            predicate = dateTimePath.between(DateX.parseDateTime(beginTime), DateX.parseDateTime(endTime));
            ;
        } else if (paramsMap.get(name + "_begin") != null && paramsMap.get(name + "_end") == null) {
            String beginTime = paramsMap.get(name + "_begin").get(0);
            predicate = dateTimePath.after(DateX.parseDateTime(beginTime));
        } else if (paramsMap.get(name + "_begin") == null && paramsMap.get(name + "_end") != null) {
            String endTime = paramsMap.get(name + "_end").get(0);
            predicate = dateTimePath.before(DateX.parseDateTime(endTime));
        }
        return predicate;
    }

    public static Number stringToNumber(String string, Class clazz) {
        Number result = null;
        if (Integer.class.equals(clazz)) {
            return Integer.parseInt(string);
        } else if (Double.class.equals(clazz)) {
            return Double.parseDouble(string);
        } else if (Float.class.equals(clazz)) {
            return Float.parseFloat(string);
        }
        return result;
    }

    public static Predicate dealNumberBinding(NumberPath numberPath, MultiValueMap<String, String> paramsMap) {
        String name = numberPath.getMetadata().getName();
        Class classType = numberPath.getType();
        Predicate predicate = null;
        if (paramsMap.get(name + "_min") != null && paramsMap.get(name + "_max") != null) {
            Number min = stringToNumber(paramsMap.get(name + "_min").get(0), classType);
            Number max = stringToNumber(paramsMap.get(name + "_max").get(0), classType);
            predicate = numberPath.between(min, max);
        } else if (paramsMap.get(name + "_min") != null && paramsMap.get(name + "_max") == null) {
            Number min = stringToNumber(paramsMap.get(name + "_min").get(0), classType);
            predicate = numberPath.gt(min);
        } else if (paramsMap.get(name + "_min") == null && paramsMap.get(name + "_max") != null) {
            Number _max = stringToNumber(paramsMap.get(name + "_max").get(0), classType);
            predicate = numberPath.lt(_max);
        }
        return predicate;
    }


    public static void main(String[] args) {
        System.out.println(QBaseDojo.baseDojo.delFlag.getRoot());
    }


    public static Class getClassByModelName(String entitySimpleName) {
        String entity = toUpperCaseFirstOne(entitySimpleName);
        Class clazz = null;
        try {
            clazz = Class.forName("com.blackeye.worth.model." + entity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }


    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}


