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
package org.consulo.idea.util.impl;

import org.consulo.idea.model.IdeaModuleModel;
import org.consulo.idea.model.IdeaProjectModel;
import org.consulo.idea.model.orderEnties.IdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.InheritedIdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.JdkSourceIdeaOrderEntryModel;
import org.consulo.idea.util.IdeaModuleTypeToModuleExtensionConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.java.module.extension.JavaMutableModuleExtension;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.pom.java.LanguageLevel;

/**
 * @author VISTALL
 * @since 16:27/15.06.13
 */
public class JavaIdeaModuleTypeToModuleExtensionConverter extends IdeaModuleTypeToModuleExtensionConverter<JavaConfigurationPanel>
{
	@Override
	public JavaConfigurationPanel createConfigurationPanel(@NotNull Project project,
			@NotNull IdeaProjectModel ideaProjectModel,
			@NotNull IdeaModuleModel ideaModuleModel)
	{
		return new JavaConfigurationPanel(project, ideaProjectModel);
	}

	@Override
	public void convertTypeToExtension(@NotNull ModuleRootModel moduleRootModel,
			@NotNull IdeaModuleModel ideaModuleModel,
			@Nullable JavaConfigurationPanel javaConfigurationPanel)
	{
		assert javaConfigurationPanel != null;

		JavaMutableModuleExtension<?> moduleExtension = enableExtensionById("java", moduleRootModel);
		assert moduleExtension != null;

		for(IdeaOrderEntryModel ideaOrderEntryModel : ideaModuleModel.getOrderEntries())
		{
			if(ideaOrderEntryModel instanceof InheritedIdeaOrderEntryModel)
			{
				moduleExtension.getInheritableSdk().set(null, javaConfigurationPanel.getSdkComboBox().getSelectedSdkName());
			}
			else if(ideaOrderEntryModel instanceof JdkSourceIdeaOrderEntryModel)
			{
				moduleExtension.getInheritableSdk().set(null, ((JdkSourceIdeaOrderEntryModel) ideaOrderEntryModel)
						.getJdkName());
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
			moduleExtension.getInheritableLanguageLevel().set(null, (LanguageLevel) javaConfigurationPanel.getLanguageLevelBox()
					.getSelectedItem());
		}
	}
}
