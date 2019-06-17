package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

//VARIOUS STATIC METHODS THAT CAN BE USED BY ANY CLASS TO DO A VARIETY
//OF FUNCTIONS SUCH AS LOAD IMAGES OR DATA FILES

public class Utils {

	//LOAD AN IMAGE
	public static BufferedImage loadImage(String fileName) {
		try {
			return ImageIO.read(new File("images/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//LOAD A TOWER'S PROPERTIES FROM A TOWER FILE
	public static Map<String, String> loadTower(String towerFile){
		//GET CORRECT TOWER FILE
		File file = new File("data/towers/" + towerFile + ".csv");
		return loadCSV(file);
	}
	
	//LOAD AN ENEMY'S PROPERTIES FORM AN ENEMY FILE
	public static Map<String, String> loadEnemy(String enemyFile){
		//GET CORRECT ENEMY FILE
		File file = new File("data/enemies/" + enemyFile + ".csv");
		return loadCSV(file);
	}
	
	//LOAD A CSV FILE INTO A MAP ARRAY OBJECT
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
	
	//LOAD A GAME MAP FROM A MAP FILE
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

	//GENERATE A 10 CHARACTER STRING TO BE USED AS A TOWER OR ENEMY ID
	public static String genId() {
		String id = "";
		for(int i = 0; i < 10; i++) {
			id += (char) ((int) Math.round(Math.random() * 25) + 65);
		}
		return id;
	}
	
	//GET THE NAMES OF ALL THE MAP FILES IN THE /maps/ FOLDER
	public static String[] findMaps() {
		File mapDir = new File("data/maps/");
		List<String> finalMapList = new ArrayList<>();
		String[] maps = mapDir.list();
		for(int i = 0; i < maps.length; i++) {
			if(maps[i].contains(".csv")) {
				finalMapList.add(maps[i].replace(".csv", ""));
			}
		}
		return finalMapList.toArray(new String[] {});
	}
	
}
