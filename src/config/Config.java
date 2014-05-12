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
	        
	    } catch (Exception e) {
	    	System.out.println("Could not load config file at " + file);
	    	System.out.println(e);
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
			System.out.println("Evolution mode set to " + evolution.name());
			
		}
		
		if (var.equalsIgnoreCase("populationSize")){
			
			populationSize = Integer.parseInt(val);
			System.out.println("populationSize parsed to " + populationSize);
			
		}
		
		if (var.equalsIgnoreCase("pictureWidth")){
			
			pictureWidth = Integer.parseInt(val);
			System.out.println("pictureWidth parsed to " + pictureWidth);
			
		}
		
		if (var.equalsIgnoreCase("pictureHeight")){
			
			pictureHeight = Integer.parseInt(val);
			System.out.println("pictureHeight parsed to " + pictureHeight);
			
		}
		
		if (var.equalsIgnoreCase("champions")){
			
			champions = Integer.parseInt(val);
			System.out.println("champions parsed to " + champions);
			
		}
		
		if (var.equalsIgnoreCase("paintTime")){
			
			paintTime = Integer.parseInt(val);
			System.out.println("paintTime parsed to " + paintTime);
			
		}
		
		if (var.equalsIgnoreCase("goalImage")){
			
			goalImage = val;
			System.out.println("goalImage parsed to " + goalImage);
			
		}
		
		if (var.equalsIgnoreCase("liftPercentage")){
			
			liftPercentage = Double.parseDouble(val);
			System.out.println("liftPercentage parsed to " + liftPercentage);
			
		}
		
		if (var.equalsIgnoreCase("brushFactor")){
			
			brushFactor = Integer.parseInt(val);
			System.out.println("brushFactor parsed to " + brushFactor);
			
		}
		
		if (var.equalsIgnoreCase("backgroundColorR")){
			
			backgroundColorR = Integer.parseInt(val);
			System.out.println("backgroundColorR parsed to " + backgroundColorR);
			
		}
		
		if (var.equalsIgnoreCase("backgroundColorG")){
			
			backgroundColorG = Integer.parseInt(val);
			System.out.println("backgroundColorG parsed to " + backgroundColorG);
			
		}
		
		if (var.equalsIgnoreCase("backgroundColorB")){
			
			backgroundColorB = Integer.parseInt(val);
			System.out.println("backgroundColorB parsed to " + backgroundColorB);
			
		}
		
		if (var.equalsIgnoreCase("soundInput")){
			
			soundInput = Boolean.parseBoolean(val);
			System.out.println("soundInput parsed to " + soundInput);
			
		}
		
		if (var.equalsIgnoreCase("drawLinePercentage")){
			
			drawLinePercentage = Double.parseDouble(val);
			System.out.println("drawLinePercentage parsed to " + drawLinePercentage);
			
		}
		
		if (var.equalsIgnoreCase("drawRoundRectPercentage")){
			
			drawRoundRectPercentage = Double.parseDouble(val);
			System.out.println("drawRoundRectPercentage parsed to " + drawRoundRectPercentage);
			
		}
		
		if (var.equalsIgnoreCase("maxNodes")){
			
			maxNodes = Integer.parseInt(val);
			System.out.println("maxNodes parsed to " + maxNodes);
			
		}
		
		if (var.equalsIgnoreCase("novel")){
			
			novel = Integer.parseInt(val);
			System.out.println("novel parsed to " + novel);
			
		}
		
		if (var.equalsIgnoreCase("mutationRate")){
			
			mutationRate = Double.parseDouble(val);
			System.out.println("mutationRate parsed to " + mutationRate);
			
		}
		
		if (var.equalsIgnoreCase("outputFolder")){
			
			outputFolder = val;
			System.out.println("outputFolder parsed to " + outputFolder);
			
		}	
		
	}
	
}

