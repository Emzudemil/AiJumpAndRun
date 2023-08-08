package gl

class Vertex(
		var x: Float,
		var y: Float,
		var z: Float,
		var u: Float = 0.0f,
		var v: Float = 0.0f,
		val parent: VertexBuilder? = null)
{

	fun tex(u: Float, v: Float): Vertex
	{
		this.u = u
		this.v = v
		return this
	}

	operator fun plus(other: Float): Vertex
	{
		return Vertex(x + other, y + other, z + other, u + other, v + other)
	}

	operator fun minus(other: Float): Vertex
	{
		return Vertex(x - other, y - other, z - other, u - other, v - other)
	}

	operator fun times(other: Float): Vertex
	{
		return Vertex(x * other, y * other, z * other, u * other, v * other)
	}

	operator fun div(other: Float): Vertex
	{
		return Vertex(x / other, y / other, z / other, u / other, v / other)
	}

	fun end()
	{
		parent?.let {
			it.vertices.addAll(x, y, z)
			it.texture.addAll(u, v)
		}
	}

}

private fun <E> MutableList<E>.addAll(vararg elements: E)
{
	addAll(elements)
}