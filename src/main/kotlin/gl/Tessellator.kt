package gl

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer

object Tessellator {
	private val vaoId		: Int
	private val vboId		: Int
	private var vertexCount	: Int		= 0
	var			mode		: Int		= GL11.GL_QUADS

	init {
		vaoId = createVAO()
		vboId = createVBO()
	}

	private fun createVAO(): Int {
		val id = GL30.glGenVertexArrays()
		GL30.glBindVertexArray(id)
		return id
	}

	private fun createVBO(): Int {
		val id = GL15.glGenBuffers()
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id)
		return id
	}

	/**
	 * Use this to add vertices to the Tessellator. To draw the tesselator.
	 */
	operator fun invoke(function: VertexBuilder.() -> Unit) {
		val builder = VertexBuilder()
		builder.function()
		builder.__build()
		addVertices(builder.vertices.toFloatArray(), builder.texture.toFloatArray())
	}

	fun addVertices(vertices: FloatArray, textures: FloatArray) {
		val interleavedData = interleaveVertexTextureData(vertices, textures)
		val buffer = asFloatBuffer(interleavedData)

		vertexCount = vertices.size / 3

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, (3+2) * Float.SIZE_BYTES, 0)
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, (3+2) * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
		GL30.glBindVertexArray(0)
	}

	fun cleanUp() {
		GL20.glDisableVertexAttribArray(0)
		GL20.glDisableVertexAttribArray(1)

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
		GL15.glDeleteBuffers(vboId)

		GL30.glBindVertexArray(0)
		GL30.glDeleteVertexArrays(vaoId)
	}

	private fun asFloatBuffer(data: FloatArray): FloatBuffer {
		val buffer = BufferUtils.createFloatBuffer(data.size)
		buffer.put(data)
		buffer.flip()
		return buffer
	}

	private fun interleaveVertexTextureData(vertices: FloatArray, textures: FloatArray): FloatArray {
		val data = FloatArray((vertices.size / 3) * 5) // 3 vertex + 2 texture per set

		for (i in 0..<vertices.size / 3) {
			data[i * 5] = vertices[i * 3]     // x
			data[i * 5 + 1] = vertices[i * 3 + 1] // y
			data[i * 5 + 2] = vertices[i * 3 + 2] // z

			data[i * 5 + 3] = textures[i * 2] // u
			data[i * 5 + 4] = textures[i * 2 + 1] // v
		}

		return data
	}

	fun render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
		GL30.glBindVertexArray(vaoId)
		GL20.glEnableVertexAttribArray(0)
		GL20.glEnableVertexAttribArray(1)

		GL11.glDrawArrays(mode, 0, vertexCount)

		GL20.glDisableVertexAttribArray(0)
		GL20.glDisableVertexAttribArray(1)
		GL30.glBindVertexArray(0)
	}
}