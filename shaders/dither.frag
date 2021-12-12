#version 130

in vec2 textureCoord;

uniform sampler2D mosaic;

out vec4 outColor;

void main(){
vec3 textureColor = texture(mosaic, textureCoord).rgb;
    outColor = vec4(textureColor, 1.0);
}