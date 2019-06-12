package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Utils {

	public static BufferedImage loadImage(String fileName) {
		try {
			return ImageIO.read(new File("images/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, String> loadTower(String towerFile){
		//GET CORRECT TOWER FILE
		File file = new File("data/towers/" + towerFile + ".csv");
		return loadCSV(file);
	}
	
	public static Map<String, String> loadEnemy(String enemyFile){
		//GET CORRECT TOWER FILE
		File file = new File("data/enemies/" + enemyFile + ".csv");
		return loadCSV(file);
	}
	
	public static Map<String, String> loadCSV(File file) {		
		//CREATE MAP OBJECT WHERE WE WILL STORE OUR DATA
		Map<String, String> data = new HashMap<String, String>();
		
		BufferedReader br = null;
		try {
			//LOAD TOWER FILE
			br = new BufferedReader(new FileReader(file));
			
			String line = "";
			//KEEP READING FILE UNTIL WE REACH THE END
			while((line = br.readLine()) != null) {
				String[] property = line.split(",");
				//PUT TOWER PROPERTIES INTO MAP
				data.put(property[0], property[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//CLOSE BUFFERED READER ONCE WE'RE DONE WITH IT
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return data;
	}
	
	public static Integer[] loadMap(File file) {
		ArrayList<Integer> data = new ArrayList<>();
		
		BufferedReader br = null;
		try {
			//LOAD TOWER FILE
			br = new BufferedReader(new FileReader(file));
			
			String line = "";
			//KEEP READING FILE UNTIL WE REACH THE END
			while((line = br.readLine()) != null) {
				String[] property = line.split(",");
				//PUT TOWER PROPERTIES INTO MAP
				data.add(Integer.parseInt(property[0]));
				data.add(Integer.parseInt(property[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//CLOSE BUFFERED READER ONCE WE'RE DONE WITH IT
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return data.toArray(new Integer[] {});
	}

	public static String genId() {
		String id = "";
		for(int i = 0; i < 10; i++) {
			id += (char) ((int) Math.round(Math.random() * 25) + 65);
		}
		return id;
	}
	
	public String[] findMaps() {
		File mapDir = new File("data/maps/");
		return mapDir.list();
	}
	
}
