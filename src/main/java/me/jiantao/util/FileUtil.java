package me.jiantao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class FileUtil {
	
	private FileUtil(){};
	
	public static File createNewFile(String pathname){
		return createNewFile(getFile(pathname));
	}
	
	public static File createNewFile(File file){
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
			return file;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File getFile(String pathname){
		File file = new File(pathname);
		if(!file.exists()){
			createNewFile(file);
		}
		return file;
	}
	
	public static InputStream getInputStream(String pathname){
		InputStream is;
		try {
			is = new FileInputStream(pathname);
			return is;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
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
		Objects.requireNonNull(is);
		Objects.requireNonNull(os);
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
		Objects.requireNonNull(is);
		Objects.requireNonNull(file);
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
