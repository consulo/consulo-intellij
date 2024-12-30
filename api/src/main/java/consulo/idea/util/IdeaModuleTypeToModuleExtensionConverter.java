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

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.application.Application;
import consulo.component.extension.ExtensionPointCacheKey;
import consulo.idea.model.IdeaModuleModel;
import consulo.idea.model.IdeaProjectModel;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootModel;
import consulo.module.extension.MutableModuleExtension;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Map;

/**
 * @author VISTALL
 * @since 16:25/15.06.13
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public abstract class IdeaModuleTypeToModuleExtensionConverter<T extends IdeaModuleTypeConfigurationPanel>
{
	private static final ExtensionPointCacheKey<IdeaModuleTypeToModuleExtensionConverter, Map<String, IdeaModuleTypeToModuleExtensionConverter>> CACHE_KEY =
			ExtensionPointCacheKey.groupBy("IdeaModuleTypeToModuleExtensionConverter", IdeaModuleTypeToModuleExtensionConverter::getModuleType);

	@Nullable
	public static IdeaModuleTypeToModuleExtensionConverter find(String moduleType)
	{
		return Application.get().getExtensionPoint(IdeaModuleTypeToModuleExtensionConverter.class).getOrBuildCache(CACHE_KEY).get(moduleType);
	}

	@Nonnull
	public abstract String getModuleType();

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
		ModuleExtensionProvider provider = ModuleExtensionProvider.findProvider(id);
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
