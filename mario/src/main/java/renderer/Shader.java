package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filePath){
        this.filePath = filePath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z)]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n",index);
            String firstPattern = source.substring(index,eol).trim();

            index  = source.indexOf("#type",eol) + 6;
            eol = source.indexOf("\r\n",index);
            String secondPattern = source.substring(index,eol).trim();

            if (firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            }else {
                throw new IOException("Unexpected token " + firstPattern + " in " + filePath);
            }

            if (secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            }else {
                throw new IOException("Unexpected token " + secondPattern + " in " + filePath);
            }

        }catch (IOException e){
            e.printStackTrace();
            assert false : "ERROR : could not open file for shader" + filePath + " !";
        }
    }

    public void compile(){

        int vertexID,fragmentID;

        //Create Shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID,vertexSource);
        glCompileShader(vertexID);

        //check errors
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : " + filePath + " \n \t Vertex Shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false: "";
        }


        //Create Shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID,fragmentSource);
        glCompileShader(fragmentID);

        //check errors
        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR :" + filePath + " \n \t Fragment Shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false: "";
        }

        //Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);

        //check errors
        success = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : " + filePath + " \n \t Linking Shader failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));
            assert false: "";
        }

    }

    public void use(){

        //Bind Shader program
        glUseProgram(shaderProgramID);

    }

    public void detach(){
        //Bind Shader program
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID,varName);

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);

        glUniformMatrix4fv(varLocation,false,matBuffer);
    }
}
