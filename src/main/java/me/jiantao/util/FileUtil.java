package me.jiantao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	
	private FileUtil(){};
	
	/**
	 * 根据指定路径创建一个新的文件
	 * @param pathname
	 * @return
	 */
	public static File createNewFile(String pathname){
		return createNewFile(new File(pathname));
	}
	
	/**
	 * 创建一个新的文件
	 * @param file
	 * @return
	 */
	public static File createNewFile(File file){
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
			return file;
		} catch (IOException e) {
			throw new RuntimeException("创建文件失败" ,e);
		}
	}
	
	/**
	 * 根据指定路径获取一个文件，若文件不存在，则返回null
	 * @param pathname
	 * @return
	 */
	public static File getFile(String pathname){
		File file = new File(pathname);
		if(file.exists()){
			return file;
		}
		return null;
	}
	
	/**
	 * 根据指定路径获取一个文件流
	 * @param pathname
	 * @return
	 */
	public static InputStream getInputStream(String pathname){
		InputStream is;
		try {
			is = new FileInputStream(pathname);
			return is;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("文件不存在" ,e);
		}
	}
	
	/**
	 * 获取文件对应的输入流
	 * @param file
	 * @return
	 */
	public static InputStream getInputStream(File file){
		InputStream is;
		try {
			is = new FileInputStream(file);
			return is;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void inputStreamToOutputStream(InputStream is, OutputStream os){
		byte [] b = new byte[8 * 1024];
		
		try {
			while(is.read(b) != -1){
				os.write(b);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void inputStreamToFile(InputStream is, File file){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			inputStreamToOutputStream(is, fos);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static void inputStreamToFile(InputStream is, String pathname){
		File file = getFile(pathname);
		inputStreamToFile(is, file);
	}
	
	public static boolean deleteFile(String pathname){
		File file = getFile(pathname);
		return file.delete();
	}
	
	public static boolean deleteFile(File file){
		return file.delete();
	}
	
}
