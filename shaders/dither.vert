#version 130

in vec2 inPosition;
in vec2 inTextureCoord;
out vec2 textureCoord;

void main() {
    vec2 position = inPosition;
    gl_Position = vec4(position.x, position.y, 0,1);
    textureCoord = inTextureCoord;
} 