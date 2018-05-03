/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.idea.file;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.idea.IdeaIcons;

/**
 * @author VISTALL
 * @since 20:26/14.06.13
 */
public class IdeaModuleFileType implements FileType
{
	public static final String DEFAULT_EXTENSION = "iml";
	public static final IdeaModuleFileType INSTANCE = new IdeaModuleFileType();

	@NotNull
	@Override
	public String getId()
	{
		return "IDEA_MODULE";
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return "IntelliJ IDEA module files";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return DEFAULT_EXTENSION;
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return IdeaIcons.Idea;
	}

	@Override
	public boolean isBinary()
	{
		return false;
	}

	@Nullable
	@Override
	public String getCharset(@NotNull VirtualFile file, byte[] content)
	{
		return "UTF-8";
	}
}
