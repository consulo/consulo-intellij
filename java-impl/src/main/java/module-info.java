/**
 * @author VISTALL
 * @since 01/04/2023
 */
module consulo.intellij.java.impl
{
	requires consulo.application.content.api;
	requires consulo.module.api;
	requires consulo.module.content.api;
	requires consulo.module.ui.api;
	requires consulo.ui.ex.awt.api;

	requires consulo.intellij.api;
	requires consulo.java;
	requires consulo.java.language.api;

	// TODO remove in future
	requires java.desktop;
}
