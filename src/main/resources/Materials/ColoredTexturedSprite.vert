uniform mat4 g_WorldViewProjectionMatrix;

uniform int m_Index;

attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;

const float nbFrames = 8;

void main(){
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    //texCoord = inTexCoord;

    float x = inTexCoord.x / nbFrames + float(m_Index) / nbFrames ;
    texCoord = vec2(x,inTexCoord.y);
}

