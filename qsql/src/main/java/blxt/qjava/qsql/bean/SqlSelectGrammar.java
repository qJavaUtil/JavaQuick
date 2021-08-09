package blxt.qjava.qsql.bean;

/**
 * sql 查询 语法 辅助工具
 * @author OpenJialei
 * @date 2021年08月06日 17:43
 */
public class SqlSelectGrammar {

   // https://www.runoob.com/w3cnote/sql-syntax-manual.html

   /** 动作 */
   final String action = "SELECT";

   StringBuilder sql = new StringBuilder(128);

   /** 表 */
   String table;

   String column;

   /** 条件 */
   String where;

   String and;

   String or;

   String between;

   String distinct;

   String orderby;

   String groupby;

   String having;

   String join;

   public SqlSelectGrammar table(String table){
      sql.append("from ").append(table);
      return this;
   }




}
