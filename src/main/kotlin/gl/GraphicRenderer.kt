package gl

import glm_.mat4x4.Mat4

class GraphicRenderer : IRenderer
{
	var	matrix: Mat4?	= null
	var shader: Shader?	= null

	override fun render(screen: IScreen)
	{
		shader ?: return
		shader!! {
			TODO("RENDER MESH IN HERE FOR EACH MESH IN SCREEN")
		}
	}

	override fun render(tessellator: Tessellator)
	{
		shader?: return
		shader!! {
			tessellator.render()
		}
	}


}