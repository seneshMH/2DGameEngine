package editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import jade.GameObject;
import jade.Window;
import org.lwjgl.system.windows.WinBase;

import java.util.List;

public class SceneHierarchyWindow {

    public void imGui(){
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for (GameObject obj : gameObjects){
            if(!obj.doSerialization()){
                continue;
            }

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

            if(treeNodeOpen){
                ImGui.treePop();
            }


            index++;
        }
        ImGui.end();
    }
}
