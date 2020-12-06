package com.blxt.utils.check;

import com.blxt.utils.PatternPool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string 检查
 * @Author: Zhang.Jialei
 * @Date: 2020/10/24 17:29
 */
public class StringCheck {

    public static final Pattern EMAIL;
    public static final Pattern MOBILE;

    ///** demo */
//
//    @Override
//    public Map post(Map<String,String> record) {
//        FieldUtils.stringIsNullCheck(record.get("username"), "用户名不能为空", CODE, "");
//        FieldUtils.stringIsNullCheck(record.get("password"), "密码不能为空", CODE, "");
//        FieldUtils.stringIsNullCheck(record.get("sex"), "性别不能为空", CODE, "");
//        FieldUtils.isNumberCheck(record.get("age"), "输入的年龄不合法", CODE, "");
//        FieldUtils.isIntCheck(record.get("age2"), "请输入整数", CODE, "");
//        FieldUtils.isEmailCheck(record.get("email"), "请输入正确的邮件", CODE, "");
//        FieldUtils.isMobileCheck(record.get("mobile"), "请输入正确的手机号", CODE, "");
//        //dao逻辑操作
//        return record;
//    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for(int i = 0; i < length; ++i) {
                if (!isBlankChar(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static boolean isNumber(CharSequence str) {
        if (isBlank(str)) {
            return false;
        } else {
            char[] chars = str.toString().toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            int start = chars[0] != '-' && chars[0] != '+' ? 0 : 1;
            int i;
            if (sz > start + 1 && chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
                i = start + 2;
                if (i == sz) {
                    return false;
                } else {
                    while(i < chars.length) {
                        if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                            return false;
                        }

                        ++i;
                    }

                    return true;
                }
            } else {
                --sz;

                for(i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        foundDigit = true;
                        allowSigns = false;
                    } else if (chars[i] == '.') {
                        if (hasDecPoint || hasExp) {
                            return false;
                        }

                        hasDecPoint = true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (chars[i] != '+' && chars[i] != '-') {
                            return false;
                        }

                        if (!allowSigns) {
                            return false;
                        }

                        allowSigns = false;
                        foundDigit = false;
                    } else {
                        if (hasExp) {
                            return false;
                        }

                        if (!foundDigit) {
                            return false;
                        }

                        hasExp = true;
                        allowSigns = true;
                    }
                }

                if (i < chars.length) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        return true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (chars[i] == '.') {
                            return !hasDecPoint && !hasExp ? foundDigit : false;
                        } else if (allowSigns || chars[i] != 'd' && chars[i] != 'D' && chars[i] != 'f' && chars[i] != 'F') {
                            if (chars[i] != 'l' && chars[i] != 'L') {
                                return false;
                            } else {
                                return foundDigit && !hasExp;
                            }
                        } else {
                            return foundDigit;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return !allowSigns && foundDigit;
                }
            }
        }
    }

    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
            return false;
        }

        Matcher m = EMAIL.matcher(email);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isMobile(CharSequence value) {
        if (null==value || "".equals(value)){
            return false;
        }

        Matcher m = EMAIL.matcher(value);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    static {
        EMAIL = PatternPool.EMAIL;
        MOBILE = PatternPool.MOBILE;
    }

}
