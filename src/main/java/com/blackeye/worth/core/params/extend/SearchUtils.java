package com.blackeye.worth.core.params.extend;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.Column;
import javax.persistence.criteria.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

/**
 * Automatic assembling the query conditions. (org.springframework.data.jpa.domain.Specification)
 * @ https://blog.csdn.net/achi010/article/details/72793479
 */
public class SearchUtils {

    /**
     * Automatic assembling the query conditions.
     * 
     * @param root
     * @param query
     * @param cb
     * @param model : tableColumnName_SqlOperator_convertDataType
     * @return
     */
    public static CriteriaQuery<?> autoBuildQuery(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Map<String, Object> model) {
        if (model == null || model.size() == 0) {
            return null;
        }
        // query condition list
        List<Predicate> predicates = new ArrayList<Predicate>();
        List<Order> orderList = new ArrayList<Order>();
        List<Expression<?>> groupList = new ArrayList<Expression<?>>();
        // assemble the query conditions
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            String key = entry.getKey();
            String[] keys = key.split("_");
            Object value = entry.getValue();
            // value is null
            boolean valueIsNull = null == value || (value instanceof String && StringUtils.isBlank((String) value))
                    || (value instanceof Collection && CollectionUtils.isEmpty((Collection<?>) value));
            // field_oper_type : tableColumnName_SqlOperator_convertDataType
            String field = keys[0];
            String oper = keys.length > 1 ? keys[1] : "";
            String type = keys.length > 2 ? keys[2] : "";
            // To validate the field to prevent SQL injection.
            if (validateFieldKey(root.getJavaType(), field) == false) {
                // TODO : Increase the log output information.
                // throw new DataValidateException("查询字段[" + field + "]不存在");
                continue;
            }
            // reference entity
            Path<String> path = root.get(field);
            if (keys.length == 1) {
                if (valueIsNull == false) {
                    predicates.add(cb.equal(path, convertQueryParamsType(type, value)));
                }
                continue;
            }
            if (keys.length > 1) {
                if (valueIsNull == false) {
                    if (SearchType.EQ.equals(oper)) {
                        predicates.add(cb.equal(path, convertQueryParamsType(type, value)));
                        continue;
                    }
                    if (SearchType.NE.equals(oper)) {
                        predicates.add(cb.notEqual(path, convertQueryParamsType(type, value)));
                        continue;
                    }
                    if (SearchType.LT.equals(oper)) {
                        predicates.add(convertParamsTypeAndBuildQuery(oper, cb, path, type, value));
                        continue;
                    }
                    if (SearchType.LE.equals(oper)) {
                        predicates.add(convertParamsTypeAndBuildQuery(oper, cb, path, type, value));
                        continue;
                    }
                    if (SearchType.GT.equals(oper)) {
                        predicates.add(convertParamsTypeAndBuildQuery(oper, cb, path, type, value));
                        continue;
                    }
                    if (SearchType.GE.equals(oper)) {
                        predicates.add(convertParamsTypeAndBuildQuery(oper, cb, path, type, value));
                        continue;
                    }
                    if (SearchType.LIKE.equals(oper)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("%" + escapeSQLLike(convertQueryParamsType(type, value)) + "%");
                        predicates.add(cb.like(path, stringBuilder.toString()));
                        continue;
                    }
                    if (SearchType.NOTLIKE.equals(oper)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("%" + escapeSQLLike(convertQueryParamsType(type, value)) + "%");
                        predicates.add(cb.notLike(path, stringBuilder.toString()));
                        continue;
                    }
                    if (SearchType.START.equals(oper)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(escapeSQLLike(convertQueryParamsType(type, value)) + "%");
                        predicates.add(cb.like(path, stringBuilder.toString()));
                        continue;
                    }
                    if (SearchType.NOTSTART.equals(oper)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(escapeSQLLike(convertQueryParamsType(type, value)) + "%");
                        predicates.add(cb.notLike(path, stringBuilder.toString()));
                        continue;
                    }
                    if (SearchType.IN.equals(oper)) {
                        predicates.add(path.in(convertQueryParamsType(type, value)));
                        continue;
                    }
                    if (SearchType.NOTIN.equals(oper)) {
                        predicates.add(path.in(convertQueryParamsType(type, value)).not());
                        continue;
                    }
                    // ORDERBY's value may not be null
                    if (SearchType.ORDERBY.equals(oper)) {
                        if (StringUtils.equals(value.toString(), "desc")) {
                            orderList.add(cb.desc(path));
                        } else {
                            // if (StringUtils.equals(value.toString(), "asc")) {
                            orderList.add(cb.asc(path));
                        }
                        continue;
                    }
                } else {
                    if (SearchType.IS.equals(oper)) {
                        predicates.add(cb.isNull(path));
                        continue;
                    }
                    if (SearchType.ISNOT.equals(oper)) {
                        predicates.add(cb.isNotNull(path));
                        continue;
                    }
                    // ORDERBY's value may be null
                    if (SearchType.ORDERBY.equals(oper)) {
                        orderList.add(cb.asc(path));
                        continue;
                    }
                    // GROUPBY's value must be null.
                    if (SearchType.GROUPBY.equals(oper)) {
                        groupList.add(path);
                        continue;
                    }
                }
            }
        }
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        query.orderBy(orderList);
        query.groupBy(groupList);
        return query;
    }

    /**
     * Conversion BuildQuery data type of the parameter.
     * @param type
     * @param value
     * @return
     */
    private static Object convertQueryParamsType(String type, Object value) {
        if (StringUtils.isBlank(type)) {
            return value.toString();
        }
        if (SearchType.TOINT.equals(type)) {
            if (value instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection<?>) value)) {
                    value = CollectionUtils.collect((Collection<?>) value, new Transformer() {
                        @Override
                        public Integer transform(Object input) {
                            return Integer.parseInt((String) input);
                        }
                    });
                    return value;
                }
            } else if (!(value instanceof Integer)) {
                return Integer.parseInt((String) value);
            }
        } else if (SearchType.TODATE.equals(type)) {
            if (value instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection<?>) value)) {
                    value = CollectionUtils.collect((Collection<?>) value, new Transformer() {

                        @Override
                        public Date transform(Object input) {
                            try {
                                return DateUtils.parseDate(input.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                    });
                    return value;
                }
            } else if (!(value instanceof Date)) {
                try {
                    return DateUtils.parseDate(value.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        return value.toString();
    }

    /**
     * Conversion BuildQuery data type of the parameter, and assemble the query.
     * @param oper
     * @param cb
     * @param path
     * @param type
     * @param value
     * @return Predicate
     */
    private static Predicate convertParamsTypeAndBuildQuery(String oper, CriteriaBuilder cb, Path<String> path, String type, Object value) {
        // field_oper_type : type is null, don't need to transform
        if (StringUtils.isBlank(type)) {
            String valueStr = value.toString();
            if (SearchType.LT.equals(oper)) {
                return cb.lessThan(path, valueStr);
            }
            if (SearchType.LE.equals(oper)) {
                return cb.lessThanOrEqualTo(path, valueStr);
            }
            if (SearchType.GT.equals(oper)) {
                return cb.greaterThan(path, valueStr);
            }
            if (SearchType.GE.equals(oper)) {
                return cb.greaterThanOrEqualTo(path, valueStr);
            }
        }
        if (SearchType.TOINT.equals(type)) {
            boolean isTOINT = false;
            if (value instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection<?>) value)) {
                    value = CollectionUtils.collect((Collection<?>) value, new Transformer() {
                        @Override
                        public Integer transform(Object input) {
                            return Integer.parseInt((String) input);
                        }
                    });
                }
                isTOINT = true;
            }
            if (!(value instanceof Integer)) {
                isTOINT = true;
            }
            if (isTOINT) {
                int valueInt = Integer.parseInt((String) value);
                if (SearchType.LT.equals(oper)) {
                    return cb.lessThan(path.as(Integer.class), valueInt);
                }
                if (SearchType.LE.equals(oper)) {
                    return cb.lessThanOrEqualTo(path.as(Integer.class), valueInt);
                }
                if (SearchType.GT.equals(oper)) {
                    return cb.greaterThan(path.as(Integer.class), valueInt);
                }
                if (SearchType.GE.equals(oper)) {
                    return cb.greaterThanOrEqualTo(path.as(Integer.class), valueInt);
                }
            }
        }
        if (SearchType.TODATE.equals(type)) {
            boolean isTODATE = false;
            if (value instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection<?>) value)) {
                    value = CollectionUtils.collect((Collection<?>) value, new Transformer() {
                        @Override
                        public Date transform(Object input) {
                            try {
                                return DateUtils.parseDate(input.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    });
                    isTODATE = true;
                }
            }
            if (!(value instanceof Date)) {
                isTODATE = true;
            }
            if (isTODATE) {
                Date valueDate = null;
                try {
                    valueDate = DateUtils.parseDate(value.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (SearchType.LT.equals(oper)) {
                    return cb.lessThan(path.as(Date.class), valueDate);
                }
                if (SearchType.LE.equals(oper)) {
                    return cb.lessThanOrEqualTo(path.as(Date.class), valueDate);
                }
                if (SearchType.GT.equals(oper)) {
                    return cb.greaterThan(path.as(Date.class), valueDate);
                }
                if (SearchType.GE.equals(oper)) {
                    return cb.greaterThanOrEqualTo(path.as(Date.class), valueDate);
                }
            }
        }
        return null;
    }

    /**
     * Conversion BuildQuery data type of the parameter, and assemble the query.
     * @param entityClassMap
     * @param fieldName
     * @return
     */
    private static boolean validateFieldKey(Map<String, Class<?>> entityClassMap, String fieldName) {
        String alias = "";
        if (fieldName.contains(".")) {
            alias = fieldName.split("\\.")[0];
            fieldName = fieldName.split("\\.")[1];
        }

        Annotation annotation = null;
        if (entityClassMap.containsKey(alias)) {
            String getterMethodName = "get" + StringUtils.capitalize(fieldName);
            Method method = null;
            try {
                method = entityClassMap.get(alias).getMethod(getterMethodName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (method != null && method.isAnnotationPresent(Column.class)) {
                annotation = method.getAnnotation(Column.class);
            }
        }

        return annotation != null;
    }

    /**
     * Validation of the BuildQuery field name is legal.
     * @param entityClass
     * @param fieldName
     * @return
     */
    private static boolean validateFieldKey(Class<?> entityClass, String fieldName) {
        Map<String, Class<?>> entityClassMap = Maps.newHashMap();
        entityClassMap.put("", entityClass);
        return validateFieldKey(entityClassMap, fieldName);
    }

    /**
     * avoid SQL injection
     * @param likeStr
     * @return
     */
    private static String escapeSQLLike(Object likeStr) {
        String str = likeStr.toString();
        str = StringUtils.replace(str, "_", "/_");
        str = StringUtils.replace(str, "%", "/%");
        str = StringUtils.replace(str, "/", "//");
        return str;
    }

}

