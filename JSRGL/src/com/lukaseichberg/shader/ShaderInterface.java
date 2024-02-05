package com.lukaseichberg.shader;

import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public interface ShaderInterface {
	
	Vertex main(Vertex in, Uniform uniform);

}
