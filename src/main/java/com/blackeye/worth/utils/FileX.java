package com.blackeye.worth.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class FileX {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMdd/HHmmssSSS");
	static Random pathRandom = new Random();

	public static String uniqueFileName(String base, String ext) {
		String s = sdf.format(new Date());
		for(;;){
			int i = pathRandom.nextInt(1000);
			String fn = String.format("%s%3d.%s", s, i, ext).replace(" ", "0");
			File f = new File(base + fn);
			if (!f.exists()) {
				if(!f.getParentFile().exists()){
					f.getParentFile().mkdirs();
				}
				return fn;
			}
		}
	}

	public static String getString(String file, String charset) {
		try {
			byte[] b = getBytes(file);
			if (b == null)
				return null;

			return new String(b, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getCacheString(String file, String charset) {

		if (file.startsWith("jar:/")) {
			String path = file.substring(5);
			file = FileX.class.getResource(path).getFile();
		}
		
		if(file.startsWith("file:")){
			file = file.substring(5);
		}
		
		file = file.replace("%20", " ");

		File f = new File(file);
		if (!f.exists()) {
			System.err.println("file not found " + file);
			return null;
		}

		FileX x = cache.get(file);

		if (x != null && f.lastModified() == x.lastModified) {
			return x.content;
		}

		x = new FileX();
		String c = getString(file, charset);
		x.content = c;
		x.lastModified = f.lastModified();
		cache.put(file, x);

		return c;

	}

	private long lastModified;
	private String content;

	private final static Map<String, FileX> cache = new java.util.concurrent.ConcurrentHashMap();

	public static byte[] getBytes(String file) {
		InputStream is = null;
		try {
			if (file.startsWith("jar:/")) {
				String path = file.substring(5);
				file = FileX.class.getResource(path).getFile();
			}

			File f = new File(file);
			if (!f.exists()) {
				return null;
			}

			is = new FileInputStream(f);

			int i = is.available();
			byte[] b = new byte[i];
			is.read(b);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static boolean setString(String file, String charset, String content) {
		try {
			setBytes(file, content.getBytes(charset));
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean rename(String src, String dst) {
		File srcFile = new File(src);
		File dstFile = new File(dst);
		boolean success = srcFile.renameTo(dstFile);
		if (!success) {
			byte[] content = getBytes(src);
			success = setBytes(dst, content);
		}
		return success;
	}

	public static boolean setBytes(String file, byte[] content) {
		
		File f= new File(file);
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(content);
			fos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getString(InputStream fis, String charset) {
		try {
			int i = fis.available();
			byte[] b = new byte[i];
			fis.read(b);
			return new String(b, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
