package com.daviddev16.util;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.daviddev16.flatlaf.FlatSVGIcon;

public final class IOUtils {

	public static boolean checkIsJson(File file) {
		return (file != null) && 
				(containsExtension(file.getName(), Arrays.asList(".json")));
	}
	
	public static Image createImageFromFile(File file) throws IOException {
		return ImageIO.read(file);
	}
	
	public static FlatSVGIcon createSvgImageFromFile(File file) throws IOException {
		return new FlatSVGIcon(file);
	}
	
	public static FlatSVGIcon createDerivedSvgImageFromFile(File file, int width, int height) throws IOException {
		return new FlatSVGIcon(file).derive(width, height);
	}
	
	public static boolean containsExtension(String fileName, List<String> extensions) {
		for (String extension : extensions) {
			if (fileName.endsWith(extension))
				return true;
		}
		return false;
	}

	public static String getFileName(File file) {
		String fileName = file.getName();
		return fileName.substring(0, fileName.lastIndexOf('.')).trim();
	}
	
	public static String readFile(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return readInputStream(fileInputStream);
	}
	
	public static void writeFile(File file, String content) throws IOException {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(content);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (fileWriter != null) {
				fileWriter.flush();
				fileWriter.close();
			}
		}
	}
	
	public static String readInputStream(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line).append('\n');
			}
			return builder.toString();
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}
	
	
	


}
