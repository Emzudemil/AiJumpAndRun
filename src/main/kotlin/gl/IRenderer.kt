package gl

interface IRenderer
{
	fun render(screen: IScreen)

	fun render(tessellator: Tessellator)
}