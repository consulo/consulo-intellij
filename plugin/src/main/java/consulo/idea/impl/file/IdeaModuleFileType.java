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
package consulo.idea.impl.file;

import consulo.idea.impl.icon.IdeaImplIconGroup;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	public LocalizeValue getDescription()
	{
		return LocalizeValue.localizeTODO("IntelliJ IDEA module files");
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return DEFAULT_EXTENSION;
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return IdeaImplIconGroup.ideamodule();
	}

	@Nullable
	@Override
	public String getCharset(@Nonnull VirtualFile file, byte[] content)
	{
		return "UTF-8";
	}
}
