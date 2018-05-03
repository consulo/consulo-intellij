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
package consulo.idea.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.util.KeyedExtensionCollector;
import consulo.annotations.RequiredReadAction;
import consulo.idea.model.IdeaModuleModel;
import consulo.idea.model.IdeaProjectModel;
import consulo.module.extension.ModuleExtensionProviderEP;
import consulo.module.extension.MutableModuleExtension;
import consulo.module.extension.impl.ModuleExtensionProviders;

/**
 * @author VISTALL
 * @since 16:25/15.06.13
 */
public abstract class IdeaModuleTypeToModuleExtensionConverter<T extends IdeaModuleTypeConfigurationPanel>
{
	public static final KeyedExtensionCollector<IdeaModuleTypeToModuleExtensionConverter, String> EP = new KeyedExtensionCollector<>("consulo.intellij.moduleTypeToModuleExtensionConverter");

	@Nullable
	public T createConfigurationPanel(@Nonnull IdeaProjectModel ideaProjectModel, @Nonnull IdeaModuleModel ideaModuleModel)
	{
		return null;
	}

	@RequiredReadAction
	public abstract void convertTypeToExtension(@Nonnull ModuleRootModel moduleRootModel, @Nonnull IdeaModuleModel ideaModuleModel, @Nullable T panel);

	@SuppressWarnings("unchecked")
	protected static <K extends MutableModuleExtension<?>> K enableExtensionById(@Nonnull String id, @Nonnull ModuleRootModel rootModel)
	{
		ModuleExtensionProviderEP provider = ModuleExtensionProviders.findProvider(id);
		if(provider == null)
		{
			return null;
		}

		final MutableModuleExtension extension = rootModel.getExtensionWithoutCheck(id);
		assert extension != null;
		extension.setEnabled(true);
		return (K) extension;
	}
}
