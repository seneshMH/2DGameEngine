package editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import jade.GameObject;
import jade.Window;
import org.lwjgl.system.windows.WinBase;

import java.util.List;

public class SceneHierarchyWindow {
    private static String payloadDragDropType = "SceneHierarchy";

    public void imGui(){
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for (GameObject obj : gameObjects){
            if(!obj.doSerialization()){
                continue;
            }

            boolean treeNodeOpen = doTreeNode(obj,index);

            if(treeNodeOpen){
                ImGui.treePop();
            }


            index++;
        }
        ImGui.end();
    }

    public boolean doTreeNode(GameObject obj,int index){
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                obj.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                obj.name
        );

        ImGui.popID();

        if(ImGui.beginDragDropSource()){
            ImGui.setDragDropPayloadObject(payloadDragDropType,obj);
            ImGui.text(obj.name);
            ImGui.endDragDropSource();
        }

        if(ImGui.beginDragDropTarget()){
            Object payloadObj = ImGui.acceptDragDropPayloadObject(payloadDragDropType);
            if(payloadObj != null){
                if(payloadObj.getClass().isAssignableFrom(GameObject.class)){
                    GameObject playerGameObj = (GameObject)  payloadObj;
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }

}
