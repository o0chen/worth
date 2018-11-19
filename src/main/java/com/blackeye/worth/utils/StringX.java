package com.blackeye.worth.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class StringX {



	public static String after(String content, String split){

		int end = content.lastIndexOf(split);
		if(end == -1)return "";

		return content.substring(end + split.length());
	}

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String valueOf(Object str){
		if(str==null)return "";
		if(str instanceof Date) {
			return sdf.format((Date)str);
		}
		return str.toString();
	}
	public static List<String> tokenizer(Object str){
		return tokenizer(str, ", \t\n\r\f");
	}

	public static List<String> tokenizer(Object str, String split){
		if(str == null)return new LinkedList();
		String string = str.toString();
		StringTokenizer st = new StringTokenizer(string, split, false);
		List<String> valueList = new ArrayList();
		while( st.hasMoreElements() ){
			valueList.add(st.nextToken());
		}
		return valueList;
	}

	public static String join(List list, String separator) {
		StringBuilder s = new StringBuilder();
		for (Object o : list) {
			if (s.length() > 0) {
				s.append(separator);
			}
			s.append(o);
		}
		return s.toString();

	}



	public static String cut(String s, int l) {
		if (s != null && s.length() > l) {
			s = s.substring(0, l) + "...";
		}
		return s;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	public static boolean isEmpty(String... strl) {
		for(String str:strl){
			if( str == null || str.trim().length() == 0){
				return true;
			}
		}
		return false;
	}


	public static boolean isNumber(String str){
		if(str == null)return false;

		char[] cs = str.toCharArray();
		for(char c : cs){
			if(c<'0' || c>'9'){
				return false;
			}
		}
		return true;
	}


	public static boolean isNumberMayContainPoint(String str){
		if(str == null)return false;
		String regExp = "^[0-9]*(\\.[0-9]{1,})?$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}


	public static String regex(String str, String regex) {
		Pattern regex2 = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = regex2.matcher(str);
		String r = null;
		while (m.find()) {
			r = m.group();
			r = r.substring(1, r.length() - 1);
			break;
		}
		return r;
	}

	public static String fromStream(InputStream in, String charset) throws IOException{

		ByteArrayOutputStream out = new ByteArrayOutputStream(8192);
		byte[] b = new byte[8192];
		int l;
		for (;;) {
			l = in.read(b);
			if (l == -1) {
				break;
			}
			out.write(b, 0, l);
		}

		byte[] bytes = out.toByteArray();
		String string = new String(bytes, charset);
		return string;

	}

	public static String[] rmRepeat(String[] strs) {
		HashSet<String> s = new java.util.HashSet<String>();
		for (String st : strs) {
			s.add(st);
		}
		return s.toArray(new String[s.size()]);
	}

	/** 
	 * @Title: filterString
	 * @Description:  过滤特殊字符
	 * @param
	 * @author:jaybril-pro
	 * @create_time:2017年10月20日 上午11:03:38  
	 * @return String     
	 * @throws 
	 */
	public static String filterString(String string){
		String regEx="[`~!@#$%^&*()+=【】{}—+|@#￥%……&*（）”“。，、？－:<>；：.‘’\"?！ ]";
		Pattern   p   =   Pattern.compile(regEx);        
		Matcher   m   =   p.matcher(string);  
		return m.replaceAll("").trim();
	}

	public static boolean equals(String str1,String str2){
		return str1.equals(str2);
	}



//	/**
//	 * @Title: filterEmoji
//	 * @Description: 过滤表情符号
//	 * @param
//	 * @author:jaybril-pro
//	 * @create_time:2017年10月21日 下午12:02:55
//	 * @return String
//	 * @throws
//	 */
//	public static String filterEmoji(String source) {
//		if (StringUtils.isNotBlank(source)) {
//			return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
//		} else {
//			return source;
//		}
//	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		//		String[] strs = new String[] { "1", "2", "2" };
		//		System.out.println(StringX.rmRepeat(strs).length);
		System.out.println(filterString("filterString *fsa"));
		System.out.println("holle world");
	}

}
