package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Utils {

	public static BufferedImage loadImage(String fileName) {
		try {
			return ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, String> loadTower(int type) {
		//GET CORRECT TOWER FILE
		File file = new File("res/tower" + type + ".csv");
		
		//CREATE MAP OBJECT WHERE WE WILL STORE OUR DATA
		Map<String, String> tower = new HashMap<String, String>();
		
		BufferedReader br = null;
		try {
			//LOAD TOWER FILE
			br = new BufferedReader(new FileReader(file));
			
			String line = "";
			//KEEP READING FILE UNTIL WE REACH THE END
			while((line = br.readLine()) != null) {
				String[] property = line.split(",");
				//PUT TOWER PROPERTIES INTO MAP
				tower.put(property[0], property[1]);
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
		
		return tower;
	}
	
}
