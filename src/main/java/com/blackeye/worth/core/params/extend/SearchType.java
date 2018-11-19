package com.blackeye.worth.core.params.extend;

/**
 * 查询条件
 *
 */
public class SearchType {

    // 查询条件
    /**
     * 条件连接符 and
     */
    public static final String AND = "and";
    /**
     * 条件匹配符 =
     */
    public static final String EQ = "eq";
    /**
     * 条件匹配符 !=
     */
    public static final String NE = "ne";
    /**
     * 条件匹配符 <
     */
    public static final String LT = "lt";
    /**
     * 条件匹配符 <=
     */
    public static final String LE = "le";
    /**
     * 条件匹配符 >
     */
    public static final String GT = "gt";
    /**
     * 条件匹配符 >=
     */
    public static final String GE = "ge";

    /**
     * 条件匹配符 like %---%
     */
    public static final String LIKE = "like";
    /**
     * 条件匹配符 not like %%
     */
    public static final String NOTLIKE = "notlike";
    /**
     * 条件匹配符 like ---%
     */
    public static final String START = "start";
    /**
     * 条件匹配符 not like ---%
     */
    public static final String NOTSTART = "notstart";
    /**
     * 条件匹配符 in (--,--,--)
     */
    public static final String IN = "in";
    /**
     * 条件匹配符 not in (--,--,--)
     */
    public static final String NOTIN = "notin";
    /**
     * 条件匹配符 is null
     */
    public static final String IS = "is";
    /**
     * 条件匹配符 is not null
     */
    public static final String ISNOT = "isnot";
    /**
     * 条件匹配符 order by --
     */
    public static final String ORDERBY = "orderby";
    /**
     * 条件匹配符 group by --
     */
    public static final String GROUPBY = "groupby";

    // 转化数据类型
    /**
     * 转化int类型
     */
    public static final String TOINT = "int";
    /**
     * 转化date类型
     */
    public static final String TODATE = "date";

}

