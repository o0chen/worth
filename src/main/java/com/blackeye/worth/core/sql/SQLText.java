package com.blackeye.worth.core.sql;


import com.blackeye.worth.utils.StringX;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SQLText {



	public static String get(String sqlName) {

		if(sqlName.startsWith("select ")){
			return sqlName;
		}
		
		String sql = getFileSQL(sqlName);
		if (sql != null) {
			return sql;
		}

		return null;
	}

	public static String getFileSQL(String name) {

		return getXMLFileSQL(name);

	}
	public static String getXMLFileSQL(String name) {
		return getXMLFileSQL(name,"src/main/resources/sql/config.xml");
	}


	public static String getXMLFileSQL(String name, String file) {
		String[] ns = new String[]{file};
		try {
			for (String n : ns) {
				SAXReader sax = new SAXReader();
				FileInputStream fin = new FileInputStream(new File(n));
				Document doc = sax.read(fin);
				Element ele = doc.getRootElement();
				String str = ele.getName();
				@SuppressWarnings("unchecked")
				List<Element> list = ele.elements();
				for (Element element : list) {
					if (element.getName().equals("sql")) {
						System.out.println("###########"+element.attributeValue("id") + "\t" + element.getText());
						if (element.attributeValue("id").equals(name)) {
							return element.getText();
						}
					}
				}
				String result = null;
				for (Element element : list) {
					if (element.getName().equals("include")) {
						if (!StringX.isEmpty(element.attributeValue("src"))) {
							result = getXMLFileSQL(name, element.attributeValue("src"));
							if (!StringX.isEmpty(result)) return result;
						}
					}
				}
			}
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Set<String> getClasses(String packageName) {

		Set<String> classes = new LinkedHashSet();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {
					JarFile jar;
					try {
						jar = ((java.net.JarURLConnection) url.openConnection())
								.getJarFile();
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							if (name.charAt(0) == '/') {
								name = name.substring(1);
							}
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								if (idx != -1) {
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								if ((idx != -1) || recursive) {
									if (name.endsWith(".sql")
											&& !entry.isDirectory()) {
										String className = name.substring(
												packageName.length() + 1,
												name.length() - 4);

										classes.add(className);

									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<String> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".sql"));
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
			} else {
				String className = file.getName().substring(0,
						file.getName().length() - 4);
				classes.add(className);

			}
		}
	}
}
