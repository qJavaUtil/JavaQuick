import blxt.qjava.frame.TablePanle;

import javax.swing.*;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 15:13
 */
public class test {

    public static void main(String[] args) throws Exception {

        JFrame jf = new JFrame("测试窗口");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setUndecorated(true);
        jf.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);

        // 表头（列名）
        Object[] columnNames = {"姓名", "语文", "数学", "英语", "总分"};

        // 表格所有行数据
        Object[][] rowData = {
                {"张三", 80, 80, 80, 240},
                {"John", 70, 80, 90, 240},
                {"Sue", 70, 70, 70, 210},
                {"Jane", 80, 70, 60, 210},
                {"Joe", 80, 70, 60, 210}
        };


        TablePanle tablePanle = new TablePanle();
        tablePanle.setTable(columnNames, rowData);
        tablePanle.setTitle("xx表格");
        tablePanle.setSubhead("副标题");
        tablePanle.setButtom("注脚");
        tablePanle.setTable(columnNames, rowData);


        jf.setContentPane(tablePanle);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }
}
