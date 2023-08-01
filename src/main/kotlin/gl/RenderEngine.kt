package gl

import glfw.Window

class RenderEngine(width: Int, height: Int, owner: Window)
{
	private val backgroundFrameBuffer	: FrameBuffer	= FrameBuffer(width, height)
	private val gameFrameBuffer			: FrameBuffer	= FrameBuffer(width, height)
	private val characterFrameBuffer	: FrameBuffer	= FrameBuffer(width, height)
	private val uiFrameBuffer			: FrameBuffer	= FrameBuffer(width, height)
	val 		graphicsRenderer		: IRenderer		= `2dRenderer`()

	fun render(delta: Float)
	{
		backgroundFrameBuffer.bind()
		// Render background
		backgroundFrameBuffer.unbind()
		gameFrameBuffer.bind()
		// Render game
		gameFrameBuffer.unbind()
		characterFrameBuffer.bind()
		// Render characters
		characterFrameBuffer.unbind()
		uiFrameBuffer.bind()
		// Render UI
		uiFrameBuffer.unbind()
	}
}