package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import imgui.ImGui;
import jade.Camera;
import jade.GameObject;
import jade.GameObjectDeserializer;
import jade.Transform;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();



    protected boolean levelLoaded = false;


    public Scene(){

    }

    public void init(){

    }

    public void start(){
       for (GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
       }
       isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){
        if (!isRunning){
            gameObjects.add(go);
        }else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public GameObject getGameObject(int gameObjectID){
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectID)
                .findFirst();
        return result.orElse(null);
    }

    public abstract void render();
    public abstract void update(float dt);

    public Camera camera(){
        return this.camera;
    }

    public void imGui(){

    }

    public GameObject createGameObject(String name){
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);

        return go;
    }

    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class,new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class,new GameObjectDeserializer())
                .create();

        try {

            FileWriter writer =new FileWriter("level.txt");
            List<GameObject> objToSerialize= new ArrayList<>();
            for (GameObject obj : gameObjects){
                if(obj.doSerialization()){
                    objToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objToSerialize));
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void load(){

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class,new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class,new GameObjectDeserializer())
                .create();

        String inFile = "";

        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        }catch (IOException e){
            e.printStackTrace();
        }

        if(!inFile.equals("")){
            int maxGoID =-1;
            int maxComID = -1;
            GameObject[] objs = gson.fromJson(inFile,GameObject[].class);
            for (int i =0;i < objs.length;i++){
                addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()){
                    if(c.getUid() > maxComID){
                        maxComID = c.getUid();
                    }
                }

                if(objs[i].getUid() > maxGoID){
                    maxGoID = objs[i].getUid();
                }
            }

            maxGoID ++;
            maxComID ++;

            GameObject.init(maxGoID);
            Component.init(maxComID);
            this.levelLoaded = true;
        }
    }
}
