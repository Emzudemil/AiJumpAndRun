package gl

import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*
import java.io.File

class Shader(vertexFilePath: String, fragmentFilePath: String)
{
	private var vertexShaderID		: Int					= 0
	private var fragmentShaderID	: Int					= 0
	private var shaderProgramID		: Int					= 0
	val 		uniforms			: HashMap<String, Int>	= HashMap()

	init
	{
		try
		{
			vertexShaderID = loadShader(vertexFilePath, GL_VERTEX_SHADER)
			fragmentShaderID = loadShader(fragmentFilePath, GL_FRAGMENT_SHADER)
			shaderProgramID = link()

			if (shaderProgramID == 0)
			{
				throw RuntimeException("Could not create shader.")
			}
		} catch (ex: Exception)
		{
			ex.printStackTrace()
		}
	}

	private fun loadShader(fileName: String, type: Int, compiled: Boolean = true): Int
	{
		val shaderSource = StringBuilder()
		val shaderID = glCreateShader(type)
		val shaderFile = File(fileName)
		if (!shaderFile.exists())
		{
			throw RuntimeException("Could not find shader file: $fileName")
		}
		shaderFile.forEachLine { shaderSource.append(it).append("\n") }
		glShaderSource(shaderID, shaderSource)
		glCompileShader(shaderID)
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			throw RuntimeException("Could not compile shader: ${glGetShaderInfoLog(shaderID)}")
		}
		return shaderID
	}

	private fun link(): Int
	{
		val programID = glCreateProgram()
		glAttachShader(programID, vertexShaderID)
		glAttachShader(programID, fragmentShaderID)
		glLinkProgram(programID)
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
		{
			throw RuntimeException("Could not link shader: ${glGetProgramInfoLog(programID)}")
		}
		glDetachShader(programID, vertexShaderID)
		glDetachShader(programID, fragmentShaderID)
		glDeleteShader(vertexShaderID)
		glDeleteShader(fragmentShaderID)
		return programID
	}

	fun cleanup()
	{
		glDeleteProgram(shaderProgramID)
	}

	fun bind()
	{
		glUseProgram(shaderProgramID)
	}

	fun unbind()
	{
		glUseProgram(0)
	}

	operator fun get(uniformName: String): Int
	{
		if (!uniforms.containsKey(uniformName))
		{
			uniforms[uniformName] = glGetUniformLocation(shaderProgramID, uniformName)
		}
		return uniforms[uniformName]!!
	}

	operator fun invoke(function: Shader.() -> Unit)
	{
		bind()
		function()
		unbind()
	}

	internal fun setUniform(uniformName: String, value: Int)
	{
		glUniform1i(this[uniformName], value)
	}
}