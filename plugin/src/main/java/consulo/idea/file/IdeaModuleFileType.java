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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.idea.IdeaIcons;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 20:26/14.06.13
 */
public class IdeaModuleFileType implements FileType
{
	public static final String DEFAULT_EXTENSION = "iml";
	public static final IdeaModuleFileType INSTANCE = new IdeaModuleFileType();

	@Nonnull
	@Override
	public String getId()
	{
		return "IDEA_MODULE";
	}

	@Nonnull
	@Override
	public String getDescription()
	{
		return "IntelliJ IDEA module files";
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return DEFAULT_EXTENSION;
	}

	@Nullable
	@Override
	public Image getIcon()
	{
		return IdeaIcons.Idea;
	}

	@Nullable
	@Override
	public String getCharset(@Nonnull VirtualFile file, byte[] content)
	{
		return "UTF-8";
	}
}
