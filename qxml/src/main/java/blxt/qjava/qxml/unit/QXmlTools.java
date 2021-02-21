package blxt.qjava.qxml.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import blxt.qjava.qxml.QElement;


/**
 * 快速xml解析工具,不支持单标签,如<br />。标签必须成对存在，否则会解析出错
 * @author MI
 *
 */
public class QXmlTools{
	
	// 预处理源码,排除不兼容的
	private static String initContent(String content) {
		return content.replaceAll("<br />", "\r\n");
	}
	
	public static QElement init(String content) {
		// 预处理源码,排除不兼容的
		content = initContent(content);
		// 寻找标签位置
		HashMap<Integer, Integer> indexs = findIndex(content);

		// 预处理坐标
		List<IndexType> indexTypes = getIndexTypes(indexs);

		// 栈处理
		Stack<QElement> m_stack_Element = new Stack<QElement>();
		// 默认根节点
		QElement root = new QElement(0, "root");
		m_stack_Element.add(root);
		
		int rankCount = 0; // 节点处理计数
	
		for(int i = 0; i < indexTypes.size(); i++) {
			Integer key = indexTypes.get(i).index;
			Integer type = indexTypes.get(i).type;
			if(type > 0) { // 新的标签开始
				int indexStar = key;
				if(type == 1) {// 创建一个节点
					rankCount++;
					QElement  element = new QElement(rankCount);
					// 获取标签内数据 如   <meta charset="utf-8"> 123456</meta>
					int _tmpIdex = content.indexOf(">", indexStar);
					String strData =  content.substring(indexStar, _tmpIdex);
					element.makeData(strData);
				
					// 标签的文本内容
				//	IndexType indexTypeE = getLastEndStart(indexTypes, i); // 当前结束
					String _tmp = content.substring(_tmpIdex - 1, _tmpIdex + 1);
					if(!_tmp.equals("/>"))
					{
						int indexEnd = content.indexOf("<", _tmpIdex);
						_tmpIdex += 1;
						if(_tmpIdex < indexEnd ) {
							String strText =  content.substring(_tmpIdex, indexEnd);
							element.addText(strText);
							//System.out.println("添加" + strText);
						}
					}
				
					//System.out.println("开始:" + element.getLable() + " " + strText);
	
					m_stack_Element.lastElement().addSubElement(element);
					// 压入栈
					m_stack_Element.add(element);
				}
				else { // 注释
					int _tmpIdex = content.indexOf(">", indexStar);
					String note =  content.substring(indexStar, _tmpIdex + 1);
					m_stack_Element.lastElement().setNote(note);
				}
				
			}
			else{ // 结束一个标签
				rankCount--;
				int indexFind = (Integer)key;
				IndexType indexTypeS = getLastStart(indexTypes, i + 1);// 下一个开始
				QElement element = m_stack_Element.lastElement();
			
				// 移出栈
				m_stack_Element.pop();
				
				if(indexTypeS != null) {// 将剩余的追加到text
					int indexS = content.indexOf("<", indexFind);
					indexFind += 0;
					if(type == -2) {
						indexFind += element.getLable().length() + 1;
					}
					String _text = content.substring( indexFind, indexS);
					m_stack_Element.lastElement().addText(_text);
					//System.out.println("追加" + _text);
				}
				//System.out.println("结束:" + element.getLable() + " " + element.getLableClass());
			}
	
		}
		if(rankCount != 0) {
			System.err.println("解析完成,但文档结构有异常" + rankCount);
		}
	
		return root;
	}
	

	/**
	 * 预处理坐标位置,排序
	 * @param indexs
	 * @return
	 */
	private static List<IndexType> getIndexTypes(HashMap<Integer, Integer> indexs) {
		List<IndexType> indexTypes = new ArrayList<>();
		Object[] arr = indexs.keySet().toArray();
		// 排序
		Arrays.sort(arr);
		// 最后利用HashMap.get(key)得到键对应的值即可
		for (Object key : arr) {
			Integer value = indexs.get(key);
			indexTypes.add(new IndexType((Integer)key, value));
		}
		return indexTypes;
	}
	

	/**
	 * 寻找标签位置
	 * @param content
	 * @return
	 */
	private static HashMap<Integer, Integer> findIndex(String content) {
		HashMap<Integer, Integer> indexs = new HashMap<>();
		int indexStar = -1;
		int indexFind = -1;
		indexs.clear();
		// 寻找标签起始位置
		while ((indexFind = content.indexOf("<", indexStar)) >= 0) {
			char c = content.substring(indexFind + 1, indexFind + 2).toCharArray()[0];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) { // 正常标签
				indexs.put(indexFind, 1);
			}
			else if (c != '/') { // 注释<!-- 注释 --> <?-- 注释 -->
				indexs.put(indexFind, 2);
			}
			indexStar = indexFind + 1;
		}

		//System.out.println("找到:" + indexs.size());
		// 寻找标签结束位置
		indexStar = -1;
		indexFind = -1;
		while ((indexFind = content.indexOf("/>", indexStar)) >= 0) {
			indexFind += 2;
			indexs.put(indexFind, -1);
			indexStar = indexFind + 1;
		}
		indexStar = -1;
		indexFind = -1;
		while ((indexFind = content.indexOf("</", indexStar)) >= 0) {
			indexFind += 2;
			indexs.put(indexFind, -2);
			indexStar = indexFind + 1;
		}
		
		return indexs;
	}


	private static IndexType getLastStart(List<IndexType> indexTypes, int index) {
		
		for(int i = index; i < indexTypes.size(); i++) {
			if(indexTypes.get(i).type > 0) {
				return indexTypes.get(i);
			}
		}
		return null;
	}
	 
}