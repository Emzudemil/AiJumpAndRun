package gl

class VertexBuilder
{
	internal val vertices 	= mutableListOf<Float>()
	internal val texture 	= mutableListOf<Float>()

	internal fun pos(x: Float, y: Float, z: Float): Vertex
	{
		return Vertex(x, y, z, parent = this)
	}

	/**
	 * Builds the object by adding vertices and texture coordinates to the Tessellator.<br>
	 * Never call this method out of the context of the VertexBuilder.
	 */
	fun __build()
	{
		Tessellator.addVertices(vertices.toFloatArray(), texture.toFloatArray())
	}
}