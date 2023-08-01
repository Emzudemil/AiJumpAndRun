package glfw

import event.EventManager
import glfw.events.KeyboardEvent
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryStack.stackPush
import java.awt.SystemColor.window


class Window(windowAttributes: WindowAttributes) {
    val windowHandle: Long
    var shouldClose: Boolean
        get() = glfwWindowShouldClose(windowHandle)
        set(value) = glfwSetWindowShouldClose(windowHandle, value)

    init
    {
        windowHandle = createWindow(windowAttributes)
    }

    private fun createWindow(windowAttributes: WindowAttributes): Long
    {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")
        setWindowAttributes(windowAttributes)
        val windowHandle = glfwCreateWindow(windowAttributes.width, windowAttributes.height, windowAttributes.title,
            0L, 0L)
        glfwSetKeyCallback(windowHandle) { windowHandle, key, scancode, action, mods ->
            EventManager.fire(KeyboardEvent(KeyboardEvent.Key.entries[key], KeyboardEvent.State.entries[action], this))
        }
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            glfwGetWindowSize(windowHandle, pWidth, pHeight)

            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            vidmode?: throw IllegalStateException("Unable to get video mode")

            glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }
        glfwMakeContextCurrent(windowHandle)
        glfwSwapInterval(1)
        glfwShowWindow(windowHandle)
        return 0L
    }

    private fun setWindowAttributes(windowAttributes: WindowAttributes)
    {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        setHint(GLFW_RESIZABLE, windowAttributes.resizable)
        setHint(GLFW_DECORATED, windowAttributes.decorated)
        setHint(GLFW_FOCUSED, windowAttributes.focused)
        setHint(GLFW_AUTO_ICONIFY, windowAttributes.autoIconify)
        setHint(GLFW_FLOATING, windowAttributes.floating)
        setHint(GLFW_MAXIMIZED, windowAttributes.maximized)
        setHint(GLFW_CENTER_CURSOR, windowAttributes.centerCursor)
        setHint(GLFW_TRANSPARENT_FRAMEBUFFER, windowAttributes.transparentFramebuffer)
        setHint(GLFW_FOCUS_ON_SHOW, windowAttributes.focusOnShow)
        setHint(GLFW_SCALE_TO_MONITOR, windowAttributes.scaleToMonitor)

    }

    private fun setHint(hint: Int, value: Boolean)
    {
        glfwWindowHint(hint, if (value) GLFW_TRUE else GLFW_FALSE)
    }
}