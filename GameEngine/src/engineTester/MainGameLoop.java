package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// OpenGL expects vertices to be defined counter clockwise by default


		
		
		
		
		DisplayManager.createDisplay();	
		Loader loader = new Loader();
		
		
		//********** TERRAIN TEXTURE STUFF *******************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//****************************************************
			
		
		/* Player */
		TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		ModelTexture playerTexture = playerModel.getTexture();
		playerTexture.setShineDamper(10);
		playerTexture.setReflectivity(1);
		
		/* Tree */
		TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
		
		/* Low Poly Tree */
		TexturedModel lowPolyTreeModel = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		
		/* Grass */
		TexturedModel grassModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);
		
		/* Flower */
		TexturedModel flowerModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flowerTexture")));
		flowerModel.getTexture().setHasTransparency(true);
		flowerModel.getTexture().setUseFakeLighting(true);
		
		/* Fern */
		TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
		fernModel.getTexture().setHasTransparency(true);
		
		/* Box */
		TexturedModel boxModel = new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("box")));

		
		
		
		
		/* Terrain */
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap"); 	// darker the spot the lower the spot
		//Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "heightmap");
		
		

		
		/* Create entities */
		Random random = new Random();
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < 400; ++i) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fernModel, new Vector3f(x, y, z), 0, 
						random.nextFloat() * 360, 0, 0.9f));

			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(treeModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
			}
			if (i % 2 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(grassModel, new Vector3f(x, y, z), 0, 0, 0, 1.8f));
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(flowerModel, new Vector3f(x, y, z), 0, 0, 0, 2.3f));
			}
		}
		
		entities.add(new Entity(boxModel, new Vector3f(100, 5, 
				-10), 0, 0, 0, 5f));
		
		entities.add(new Entity(boxModel, new Vector3f(100, 5, 
				-100), 0, 0, 0, 5f));
		
		
		
		
		
		
		
		
		/* Light */
		Light light = new Light(new Vector3f(20000,40000,20000), new Vector3f(1,1,1)); // light source // light color
		
		
		

		
		
		
		
		

		
		
		
		MasterRenderer renderer = new MasterRenderer();
		
		
		/* Player */
		Player player = new Player(playerModel, new Vector3f(100, 0, -50), 0, 180, 0, 0.6f);
		
		/* Camera */
		Camera camera = new Camera(player);
		
		
		while(!Display.isCloseRequested()) {
			camera.move();
			
			player.move(terrain);
			
			renderer.processEntity(player);
			
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			
			for (Entity entity : entities) {

				renderer.processEntity(entity);
			}

			

			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
