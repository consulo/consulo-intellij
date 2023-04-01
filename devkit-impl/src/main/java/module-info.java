/**
 * @author VISTALL
 * @since 01/04/2023
 */
module consulo.intellij.devkit.impl
{
	requires consulo.intellij.api;
	requires consulo.java;
	requires consulo.devkit;

	requires consulo.intellij.java.impl;

	// TODO remove in future
	requires java.desktop;
}