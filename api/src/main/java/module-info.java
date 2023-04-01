/**
 * @author VISTALL
 * @since 01/04/2023
 */
module consulo.intellij.api
{
	requires consulo.ide.api;

	// TODO remove in future
	requires java.desktop;

	exports consulo.idea.model;
	exports consulo.idea.model.orderEnties;
	exports consulo.idea.util;
}