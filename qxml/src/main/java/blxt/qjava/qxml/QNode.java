package blxt.qjava.qxml;

import java.util.Map;
 
public class QNode
{
  protected int rankCount = 0;
  protected String lable = null;
  protected String lableId = null;
  protected String lableClass = null;
  protected String note = null;
  protected String text = null;
  protected Map<String, Object> datas = null;
 
  public String getNote() { return this.note; }
 
  public void setNote(String note) { this.note = note; }
 
  public int getRankCount() { return this.rankCount; }
 
  public String getLable() { return this.lable; }
 
  public void setLable(String lable) { this.lable = lable; }
 
  public String getLableId() { return this.lableId; }
 
  public void setLableId(String lableId) { this.lableId = lableId; }
 
  public String getLableClass() { return this.lableClass; }
 
  public void setLableClass(String lableClass) { this.lableClass = lableClass; }
 
  public Map<String, Object> getDatas() { return this.datas; }
 
  public void setDatas(Map<String, Object> datas) { this.datas = datas; }
 
  public String getText() { return this.text; }
 
  public void setText(String text) {
    if (text != null) {
      text = text.trim();
    }
    this.text = text;
  }
  
  public void addText(String text) {
    if (text != null) {
      text = text.trim();
    }
    if (this.text == null) {
      this.text = "";
    }
    this.text = String.valueOf(this.text) + text;
  }
}
 