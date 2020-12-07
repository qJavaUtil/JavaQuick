package blxt.qjava.frame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 17:03
 */
public class TablePanle extends JPanel {

    /**
     * 表格名称的对其方式,居中
     */
    public int TitleConstants = SwingConstants.CENTER;
    public int SubTitleConstants = SwingConstants.RIGHT;
    public int ButtomTitleConstants = SwingConstants.LEFT;


    /** 标题面板 */
    JPanel jPanelTitle = new JPanel(new BorderLayout());
    /** 表格面板 */
    JPanel jPanelTable = new JPanel(new BorderLayout());
    /** 表格注脚 */
    JPanel jPanelButtom = new JPanel(new BorderLayout());

    Font fontDefault = getFont();
    Font fontTitle = null;
    Font fontSubTitle = null;
    Font fontTableHead = null;
    Font fontTable = null;


    public TablePanle(){
        super(new BorderLayout());
        fontTitle = new Font(fontDefault.getName(), Font.BOLD, fontDefault.getSize() + 5);
        fontSubTitle  = fontDefault;
        fontTableHead = new Font(fontDefault.getName(), Font.BOLD, fontDefault.getSize());;
        fontTable     = fontDefault;
    }

    /**
     * 设置表格标题
     * @param title
     */
    public void setTitle(String title){
        remove(jPanelTitle);
        jPanelTitle.removeAll();

        JLabel jLable = new JLabel(title);
        jLable.setHorizontalAlignment(TitleConstants);
        jLable.setFont(fontTitle);

        jPanelTitle.add(jLable, BorderLayout.NORTH);

        add(jPanelTitle, BorderLayout.NORTH);
    }

    /**
     * 设置副标题,必须先添加主标题
     */
    public void setSubhead(String title){
        JLabel jLable = new JLabel(title);
        jLable.setHorizontalAlignment(SubTitleConstants);
        jLable.setFont(fontSubTitle);
        jPanelTitle.add(jLable, BorderLayout.SOUTH);
    }

    /**
     * 设置注脚
     */
    public void setButtom(String title){
        jPanelButtom.removeAll();
        JLabel jLable = new JLabel(title);
        jLable.setHorizontalAlignment(ButtomTitleConstants);
        jLable.setFont(fontSubTitle);
        jPanelButtom.add(jLable, BorderLayout.SOUTH);

        add(jPanelButtom, BorderLayout.SOUTH);
    }



    /**
     * 设置表格
     * @param head  表格头内容
     * @param data  表格数据内容
     */
    public void setTable(final Object[] head, final Object[][] data){
        // 创建一个表格，指定 所有行数据 和 表头
        JTable table = new JTable(data, head);

        // 设置table内容居中
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, tcr);

        /* 设置字体 */
        table.getTableHeader().setFont(fontTableHead);
        table.setFont(fontTable);

        jPanelTable.removeAll();
        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）
        jPanelTable.add(table.getTableHeader(), BorderLayout.NORTH);
        // 把 表格内容 添加到容器中心
        jPanelTable.add(table, BorderLayout.CENTER);

        remove(jPanelTable);
        add(jPanelTable, BorderLayout.CENTER);
    }


    /**
     * 移除表格
     */
    public void removeTable(){
        remove(jPanelTable);
    }
}
