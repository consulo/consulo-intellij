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
package consulo.idea.impl.devkit;

import com.intellij.java.language.LanguageLevel;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.content.bundle.Sdk;
import consulo.devkit.module.extension.PluginMutableModuleExtension;
import consulo.idea.model.IdeaModuleModel;
import consulo.idea.model.IdeaProjectModel;
import consulo.idea.model.orderEnties.IdeaOrderEntryModel;
import consulo.idea.model.orderEnties.InheritedIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.JdkSourceIdeaOrderEntryModel;
import consulo.idea.util.IdeaModuleTypeToModuleExtensionConverter;
import consulo.java.language.module.extension.JavaMutableModuleExtension;
import consulo.module.content.layer.ModuleRootModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 16:29/15.06.13
 */
@ExtensionImpl
public class PluginIdeaModuleTypeToModuleExtensionConverter extends IdeaModuleTypeToModuleExtensionConverter<PluginConfigurationPanel>
{
	@Nonnull
	@Override
	public String getModuleType()
	{
		return "PLUGIN_MODULE";
	}

	@Nullable
	@Override
	public PluginConfigurationPanel createConfigurationPanel(@Nonnull IdeaProjectModel ideaProjectModel, @Nonnull IdeaModuleModel ideaModuleModel)
	{
		return new PluginConfigurationPanel(ideaProjectModel);
	}

	@RequiredReadAction
	@Override
	public void convertTypeToExtension(@Nonnull ModuleRootModel moduleRootModel, @Nonnull IdeaModuleModel ideaModuleModel, @Nullable PluginConfigurationPanel pluginConfigurationPanel)
	{
		assert pluginConfigurationPanel != null;

		JavaMutableModuleExtension<?> moduleExtension = enableExtensionById("java", moduleRootModel);
		assert moduleExtension != null;

		for(IdeaOrderEntryModel ideaOrderEntryModel : ideaModuleModel.getOrderEntries())
		{
			if(ideaOrderEntryModel instanceof InheritedIdeaOrderEntryModel)
			{
				moduleExtension.getInheritableSdk().set(null, pluginConfigurationPanel.getSdkComboBox().getSelectedSdkName());
			}
			else if(ideaOrderEntryModel instanceof JdkSourceIdeaOrderEntryModel)
			{
				moduleExtension.getInheritableSdk().set(null, ((JdkSourceIdeaOrderEntryModel) ideaOrderEntryModel).getJdkName());
			}
		}

		String languageLevelProperty = ideaModuleModel.getProperty("LANGUAGE_LEVEL");
		if(languageLevelProperty != null)
		{
			try
			{
				LanguageLevel languageLevel = LanguageLevel.valueOf(languageLevelProperty);
				moduleExtension.getInheritableLanguageLevel().set(null, languageLevel);

			}
			catch(IllegalArgumentException ignored)
			{
			}
		}
		else
		{
			moduleExtension.getInheritableLanguageLevel().set(null, (LanguageLevel) pluginConfigurationPanel.getLanguageLevelBox().getSelectedItem());
		}

		PluginMutableModuleExtension pluginExtension = enableExtensionById("consulo-plugin", moduleRootModel);
		assert pluginExtension != null;

		Sdk selectedSdk = pluginConfigurationPanel.getPluginSdkComboBox().getSelectedSdk();
		if(selectedSdk != null)
		{
			pluginExtension.getInheritableSdk().set(null, selectedSdk);
		}
	}
}
