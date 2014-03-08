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
package org.consulo.idea.util;

import org.consulo.module.extension.ModuleExtensionProviderEP;
import org.consulo.module.extension.MutableModuleExtension;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.roots.ModuleRootModel;

/**
 * @author VISTALL
 * @since 16:25/15.06.13
 */
public abstract class IdeaModuleTypeToModuleExtensionConverter
{
	public abstract void convertTypeToExtension(@NotNull ModuleRootModel moduleRootModel);

	protected static void enableExtensionById(@NotNull String id, @NotNull ModuleRootModel rootModel)
	{
		final ModuleExtensionProviderEP provider = ModuleExtensionProviderEP.findProviderEP(id);
		if(provider == null)
		{
			return;
		}

		final MutableModuleExtension extension = rootModel.getExtensionWithoutCheck(id);

		extension.setEnabled(true);
	}
}
