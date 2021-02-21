package blxt.qjava.qxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
 

public class QElement
  extends QNode
{
  protected List<QElement> subElements = new ArrayList<>();

  
  public QElement(int rankCount) { this.rankCount = rankCount; }

  
  public QElement(int rankCount, String lable) {
    this.rankCount = rankCount;
    this.lable = lable;
  }
 
  public static Map<String, Object> makeValue(String content) {
    if (content == null || content.length() <= 1) {
      return null;
    }
    Map<String, Object> map = new HashMap<>();
    int index = -1;
    while (content != null && content.length() > 0 && (index = content.indexOf("=")) > 0) {
      String key = content.substring(0, index).trim();
      int indexL = content.indexOf("=", ++index);
      if (indexL < 0) {
        indexL = content.length();
      } else {
        indexL = content.lastIndexOf("\"", indexL);
        if (indexL < 0 || content.lastIndexOf("\"", indexL - 1) < 0) {
          indexL = content.length();
        } else {
          indexL++;
        } 
      } 
      String vakue = content.substring(index, indexL).trim();
      
      vakue = vakue.endsWith("\"") ? vakue.substring(0, vakue.length() - 1) : vakue;
      vakue = vakue.startsWith("\"") ? vakue.substring(1, vakue.length()) : vakue;
      map.put(key, vakue);
      content = content.substring(indexL, content.length());
    } 
    return map;
  }
 
  public String getKeyValue(String key, String defStr) {
    if (this.datas == null) {
      return defStr;
    }
    Object oClass = this.datas.get(key);
    if (oClass != null) {
      return oClass.toString();
    }
    return defStr;
  }
 
  public void putKeyValue(String key, Object value) {
    if (this.datas == null) {
      this.datas = new HashMap<>();
    }
    this.datas.put(key, value);
  }
 
  public QElement getSubNodeByKey(String key, String value) {
    if (this.datas != null) {
      String valueStr = (String)this.datas.get(key);
      if (valueStr != null && valueStr.equals(value)) {
        return this;
      }
    } 
    
    if (this.subElements == null) {
      return null;
    }
    
    for (QElement e : this.subElements) {
      QElement res = e.getSubNodeByKey(key, value);
      if (res != null) {
        return res;
      }
    } 
    return null;
  }
 
  public List<QElement> getSubNodeByKeys(String key, String value) {
    List<QElement> list = new ArrayList<>();
    if (this.datas != null) {
      String valueStr = (String)this.datas.get(key);
      if (valueStr != null && valueStr.equals(value)) {
        list.add(this);
      }
    } 
    
    if (this.subElements == null) {
      return list;
    }
    
    for (QElement e : this.subElements) {
      e.getSubNodeByKey(key, value);
    }
    return list;
  }
 
  public QElement getNodeByLable(String lableName) {
    if (this.lable != null && 
      this.lable.equals(lableName)) {
      return this;
    }

    
    if (this.subElements == null) {
      return null;
    }
    for (QElement e : this.subElements) {
      QElement res = e.getNodeByLable(lableName);
      if (res != null) {
        return e;
      }
    } 
    return null;
  }
 
  public List<QElement> getNodeByLables(String lableName) {
    List<QElement> list = new ArrayList<>();
    if (this.lable != null && 
      this.lable.equals(lableName)) {
      list.add(this);
    }

    
    if (this.subElements == null) {
      return null;
    }
    for (QElement e : this.subElements) {
      e.getNodeByLable(lableName);
    }
    return list;
  }
 
  public List<QElement> getSubNode(String lable) {
    if (this.subElements == null) {
      return null;
    }
    
    List<QElement> lsList = new ArrayList<>();
    
    for (QElement element : this.subElements) {
      if (lable.equals(element.lable)) {
        lsList.add(element);
      }
    } 
    
    return lsList;
  }
 
  public QElement getSubElement(String lable) {
    if (this.subElements == null) {
      return null;
    }
    
    for (QElement element : this.subElements) {
      if (lable.equals(element.lable)) {
        return element;
      }
    } 
    
    return null;
  }
  
  private String makeLable(String strContent) {
    if (strContent == null) {
      return null;
    }
    strContent = strContent.trim();
    int index = strContent.indexOf(" ");
    if (strContent.startsWith("<")) {
      strContent = strContent.substring(1);
    }
    
    if (index < 0) {
      index = strContent.length();
      if (strContent.endsWith("/>")) {
        index -= 2;
      } else if (strContent.endsWith(">")) {
        index--;
      } 
    } 
    return strContent.substring(0, index).trim();
  }
  
  private Map<String, Object> makeDatas(String content) {
    if (content == null) {
      return null;
    }
    content = content.trim();
    int index = content.indexOf(" ");
    if (index < 0) {
      return null;
    }
    int indexL = content.length() - 1;
    if (content.endsWith("/>")) {
      indexL--;
    }
    String dataStr = content.substring(index, indexL);
    
    Map<String, Object> datas = makeValue(dataStr);
    
    return datas;
  }
 
  private String makeDefaultId(Map<String, Object> datas) {
    if (datas != null) {
      Object oId = datas.get("id");
      if (oId != null) {
        this.lableId = oId.toString();
      }
      Object oClass = datas.get("class");
      if (oClass != null) {
        this.lableClass = oClass.toString();
      }
    } 
    return null;
  }
  
  public void makeData(String dataStr) {
    this.lable = makeLable(dataStr);
    this.datas = makeDatas(dataStr);
    makeDefaultId(this.datas);
  }
 
  public List<QElement> getSubElement() { return this.subElements; }
 
  public void addSubElement(QElement subElement) {
    if (this.subElements == null) {
      this.subElements = new ArrayList<>();
    }
    this.subElements.add(subElement);
  }
  
  public String toString() {
    String str = "";
    if (this.note != null) {
      str = String.valueOf(str) + "\r\n" + this.note;
    }
    str = String.valueOf(str) + "\r\n<" + this.lable;
    if (this.datas != null) {
      str = String.valueOf(str) + " " + dataToString();
      if (this.text != null || this.subElements != null) {
        str = String.valueOf(str) + ">";
      }
    } else {
      str = String.valueOf(str) + ">";
    } 
    if (this.subElements != null) {
      String strSub = "";
      for (QElement e : this.subElements) {
        String s = e.toString();
        strSub = String.valueOf(strSub) + s.replace("\r\n", "\r\n\t");
      } 
      str = String.valueOf(str) + strSub;
    } 
    if (this.text != null) {
      if (this.subElements != null && this.subElements.size() > 0) {
        str = String.valueOf(str) + "\r\n\t";
      }
      str = String.valueOf(str) + this.text;
    } 
    if (this.subElements == null && this.text == null) {
      str = String.valueOf(str) + "/>";
    } else {
      if (this.subElements != null && this.subElements.size() > 0) {
        str = String.valueOf(str) + "\r\n";
      }
      str = String.valueOf(str) + "</" + this.lable + ">";
    } 
    return str;
  }
  
  private String dataToString() {
    String str = "";
    if (this.datas == null) {
      return str;
    }
    Set<Map.Entry<String, Object>> set = this.datas.entrySet();
    Iterator<Map.Entry<String, Object>> iterator = set.iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, Object> entry = iterator.next();
      str = String.valueOf(str) + (String)entry.getKey() + "=\"" + entry.getValue() + "\" ";
    } 
    return str.trim();
  }
}
 