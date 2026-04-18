/**
 * @author VISTALL
 * @since 01/04/2023
 */
module consulo.intellij
{
	requires consulo.application.api;
	requires consulo.application.content.api;
	requires consulo.disposer.api;
	requires consulo.ide.api;
	requires consulo.language.api;
	requires consulo.localize.api;
	requires consulo.module.api;
	requires consulo.module.content.api;
	requires consulo.project.api;
	requires consulo.project.content.api;
	requires consulo.ui.api;
	requires consulo.ui.ex.api;
	requires consulo.ui.ex.awt.api;
	requires consulo.util.io;
	requires consulo.virtual.file.system.api;

	requires consulo.intellij.api;

	// TODO remove in future
	requires java.desktop;
}
