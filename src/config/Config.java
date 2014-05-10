package config;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Config {

	public static EvolutionMethod evolution;
	public static int populationSize;
	public static int pictureWidth;
	public static int pictureHeight;
	public static int champions;
	public static int paintTime;
	public static String goalImage;
	public static double liftPercentage;
	public static int brushFactor;
	public static int backgroundColorR;
	public static int backgroundColorG;
	public static int backgroundColorB;
	public static boolean soundInput;
	public static double drawLinePercentage;
	public static double drawRoundRectPercentage;
	public static int maxNodes;
	public static int novel;
	public static double mutationRate;
	public static String outputFolder;
	
	public static void load(String file) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(file));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            parse(line);
	            line = br.readLine();
	        }
	        
	    } finally {
	        br.close();
	    }
		
	}

	private static void parse(String line) {
		
		if (line.contains("#") || !line.contains("="))
			return;
		
		String[] arr = line.split("=");
		
		String var = arr[0].trim();
		String val = arr[1].trim();
		
		if (var.equalsIgnoreCase("evolution")){
			
			if (val.equalsIgnoreCase("neat")){
				evolution = EvolutionMethod.NEAT;
			} else if (val.equalsIgnoreCase("mating")){
				evolution = EvolutionMethod.MATING;
			} else if (val.equalsIgnoreCase("mutation")){
				evolution = EvolutionMethod.MUTATION;
			} else {
				evolution = EvolutionMethod.MUTATION;
			}
			
		}
		
		if (var.equalsIgnoreCase("populationSize")){
			
			populationSize = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("pictureWidth")){
			
			pictureWidth = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("pictureHeight")){
			
			pictureHeight = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("champions")){
			
			champions = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("paintTime")){
			
			paintTime = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("goalImage")){
			
			goalImage = val;
			
		}
		
		if (var.equalsIgnoreCase("liftPercentage")){
			
			liftPercentage = Double.parseDouble(val);
			
		}
		
		if (var.equalsIgnoreCase("brushFactor")){
			
			brushFactor = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("backgroundColorR")){
			
			backgroundColorR = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("backgroundColorG")){
			
			backgroundColorG = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("backgroundColorB")){
			
			backgroundColorB = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("soundInput")){
			
			soundInput = Boolean.parseBoolean(val);
			
		}
		
		if (var.equalsIgnoreCase("drawLinePercentage")){
			
			drawLinePercentage = Double.parseDouble(val);
			
		}
		
		if (var.equalsIgnoreCase("drawRoundRectPercentage")){
			
			drawRoundRectPercentage = Double.parseDouble(val);
			
		}
		
		if (var.equalsIgnoreCase("maxNodes")){
			
			maxNodes = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("novel")){
			
			novel = Integer.parseInt(val);
			
		}
		
		if (var.equalsIgnoreCase("mutationRate")){
			
			mutationRate = Double.parseDouble(val);
			
		}
		
		if (var.equalsIgnoreCase("outputFolder")){
			
			outputFolder = val;
			
		}	
		
	}
	
}

