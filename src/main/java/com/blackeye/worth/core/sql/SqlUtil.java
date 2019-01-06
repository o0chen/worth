package com.blackeye.worth.core.sql;

import com.blackeye.worth.core.sql.expression.Expression;
import com.blackeye.worth.core.sql.expression.ExpressionContext;

import java.util.HashMap;

public class SqlUtil {

    public static void main(String[] args) {
        Expression Expression=eval("getCustomerBuyLogByDB",null);
        System.out.println(Expression.getText());
    }


    public static Expression eval(String nql, Object params) {//想要直接得到完整sql时,只能使用{#arg} {$arg}
        if (nql.indexOf(" ") == -1) {
            nql=SQLText.get(nql);
        }
        return evalBySql(nql,params);
    }
    public static Expression evalBySql(String sql, Object params) {//想要直接得到完整sql时,只能使用{#arg} {$arg}
        if(params==null)params=new HashMap<>();
        Expression exp = Expression.newExpression(sql);
        ExpressionContext ctx = new ExpressionContext(params);
        exp.eval(ctx);
        return exp;
    }

   /* public <T> List<T> find(String nql, Class<T> type, Object parameter,
                            int start, int size) {

        String bql = SQLText.get(nql);

        Expression exp = Expression.newExpression(bql);
        ExpressionContext ctx = new ExpressionContext(parameter);
        exp.eval(ctx);

        String sql = exp.getText();
        List values = exp.getValues();

        return SQLExecutor.finds(type, sql, start, size, values);

    }

    public <T> List<Map> find(String nql, Object parameter, int size) {
        String bql = SQLText.get(nql);
        Expression exp = Expression.newExpression(bql);
        ExpressionContext ctx = new ExpressionContext(parameter);
        exp.eval(ctx);
        String sql = exp.getText();
        List values = exp.getValues();
        try {
            return SQLExecutor.finds(Map.class, sql, 0, size, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public <T> List<Map> find(String nql, Object parameter) {
        String bql = SQLText.get(nql);
        Expression exp = Expression.newExpression(bql);
        ExpressionContext ctx = new ExpressionContext(parameter);
        exp.eval(ctx);
        String sql = exp.getText();
        List values = exp.getValues();
        try {
            return SQLExecutor.finds(Map.class, sql, 0, 0, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public Map single(String nql, Object parameter) {
        List<Map> l = find(nql, parameter);
        if(l.size()>0){
            return l.get(0);
        }
        return new HashMap();
    }

    public <T> void fetch(String nql, T type, Object parameter) {

        String bql = SQLText.get(nql);

        Expression exp = Expression.newExpression(bql);

        ExpressionContext ctx = new ExpressionContext(parameter);
        exp.eval(ctx);

        String sql = exp.getText();
        List values = exp.getValues();
        SQLExecutor.fetch(type, sql, values);

    }
    public int count(String nql, Object parameter) {

        Count count = new Count();
        fetch(nql, count, parameter);
        return count.count;

    }

    public static final String PAGE_PARAMETER = "p";

    public List<Map> webpage(String nql, Object parameter, int size)
            throws Exception {
        String bql = SQLText.get(nql);

        Expression exp = Expression.newExpression(bql);
        ExpressionContext ctx = new ExpressionContext(parameter);
        exp.eval(ctx);

        String sql = exp.getText();
        List values = exp.getValues();

        int p = multipart.getParameterInt(PAGE_PARAMETER, 1);
        if (p < 1) {
            p = 1;
        }

        int start = (p - 1) * size;

        List<Map> l = SQLExecutor.finds(Map.class, sql, start, size, values);

        if (this.document != null) {

            String count_sql = "select count(1) count from (" + sql + ")";

            Count c = new Count();
            SQLExecutor.fetch(c, count_sql, values);

            int total_page = c.count / size;

            String url = request.getRequestURI();
            String query = request.getQueryString();

            Element el = this.document.id("pager");

            webpage_format(el, p, total_page, url, query);
        }

        return l;
    }
    //o0chen-新增
    public List<Map> page(String nql, Object parameter, int size,int totalSize) {

        String bql = SQLText.get(nql);

        Expression exp = Expression.newExpression(bql);
        ExpressionContext ctx = new ExpressionContext(parameter);
        exp.eval(ctx);

        String sql = exp.getText();
        List values = exp.getValues();

        if (size == 0) {
            if(this.document!=null && this.document.id("pager")!=null ){
                this.document.id("pager").remove();
            }
            return SQLExecutor.finds(Map.class, sql, 0, 0, values);
        }

        if(totalSize==0)
        {
            return page(sql, values, size);
        }else{
            return page(sql, values, size,totalSize);
        }

    }

    //o0chen-新增
    public List<Map> page(String sql, List values, int size,int totalSize) {

        page = multipart.getParameterInt(PAGE_PARAMETER, 1);
        if (page < 1) {
            page = 1;
        }
        int start = (page - 1) * size;
        List<Map> l = SQLExecutor.finds(Map.class, sql, start, size, values);
        if(totalSize==0)
        {
            String count_sql = "select count(1) count from (" + sql + ") count";
            Count c = new Count();
            SQLExecutor.fetch(c, count_sql, values);
            this.count = c.count;
            this.pageCount = (int) Math.ceil((double) c.count / size);
        }
        else{
            this.count = totalSize;
            this.pageCount = (int) Math.ceil((double) totalSize / size);
        }

        if (this.document != null) {
            String url = request.getRequestURI();
            String query = request.getQueryString();

            Element el = this.document.id("pager");
            page_format(el, page, pageCount, url, query, size, this.count );
        }
        return l;
    }


    public List<Map> page(String nql, Object parameter, int size) {

        String bql = SQLText.get(nql);

        Expression exp = Expression.newExpression(bql);
        ExpressionContext ctx = new ExpressionContext(parameter);
        exp.eval(ctx);

        String sql = exp.getText();
        List values = exp.getValues();

        if (size == 0) {
            if(this.document!=null && this.document.id("pager")!=null ){
                this.document.id("pager").remove();
            }
            return SQLExecutor.finds(Map.class, sql, 0, 0, values);
        }

        return page(sql, values, size);

    }

    public Integer count;
    public Integer pageCount;

    public Integer page = 1;

    public List<Map> page(String sql, List values, int size) {

        page = multipart.getParameterInt(PAGE_PARAMETER, 1);
        if (page < 1) {
            page = 1;
        }

        int start = (page - 1) * size;

        List<Map> l = SQLExecutor.finds(Map.class, sql, start, size, values);

        String count_sql = "select count(1) count from (" + sql + ") count";

        Count c = new Count();
        SQLExecutor.fetch(c, count_sql, values);

        this.count = c.count;
        this.pageCount = (int) Math.ceil((double) c.count / size);

        if (this.document != null) {
            String url = request.getRequestURI();
            String query = request.getQueryString();

            Element el = this.document.id("pager");
            page_format(el, page, pageCount, url, query, size, c.count);
        }

        return l;

    }*/
}
