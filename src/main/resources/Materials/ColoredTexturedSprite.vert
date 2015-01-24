uniform mat4 g_WorldViewProjectionMatrix;

uniform int m_Index;
uniform int m_Frames;

attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;

//const float nbFrames = 8;

void main(){
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    //texCoord = inTexCoord;

    float nbFrames = float(m_Frames);
    float x = inTexCoord.x / nbFrames + float(m_Index) / nbFrames ;
    texCoord = vec2(x,inTexCoord.y);
}

