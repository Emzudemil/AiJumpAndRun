package gl


import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.*
import kotlin.properties.Delegates

class FrameBuffer(private var width: Int, private var height: Int) {

	private var frameBufferId by Delegates.notNull<Int>()
	private var textureId by Delegates.notNull<Int>()
	private var depthBufferId by Delegates.notNull<Int>()

	init {
		createFrameBuffer()
	}

	private fun createFrameBuffer()
	{
		// Generate and bind frame buffer
		frameBufferId = glGenFramebuffers()
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)

		// Generate texture
		textureId = glGenTextures()
		glBindTexture(GL_TEXTURE_2D, textureId)

		// Specify texture data
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0)

		// Set texture parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)

		// Attach the texture to the frame buffer
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0)

		// Generate render buffer
		depthBufferId = glGenRenderbuffers()
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferId)

		// Specify render buffer data
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height)

		// Attach depth buffer to the frame buffer
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferId)

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw RuntimeException("Error creating framebuffer")
		}

		// Unbind
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	fun	resize(width: Int, height: Int)
	{
		cleanup()
		this.width = width
		this.height = height
		createFrameBuffer()
	}

	fun bind() {
		glBindTexture(GL_TEXTURE_2D, 0)
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)
		glViewport(0, 0, width, height)
	}

	fun unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0)
	}

	fun cleanup() {
		glDeleteFramebuffers(frameBufferId)
		glDeleteTextures(textureId)
		glDeleteRenderbuffers(depthBufferId)
	}
}