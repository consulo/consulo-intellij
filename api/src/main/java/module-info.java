/**
 * @author VISTALL
 * @since 01/04/2023
 */
module consulo.intellij.api
{
	requires consulo.application.api;
	requires consulo.application.content.api;
	requires consulo.component.api;
	requires consulo.disposer.api;
	requires consulo.logging.api;
	requires consulo.module.api;
	requires consulo.module.content.api;
	requires consulo.ui.api;
	requires consulo.util.collection;
	requires consulo.util.io;
	requires consulo.util.jdom;
	requires consulo.util.lang;

	exports consulo.idea.model;
	exports consulo.idea.model.orderEnties;
	exports consulo.idea.util;
}
