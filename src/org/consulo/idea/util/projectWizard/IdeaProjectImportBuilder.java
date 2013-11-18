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
package org.consulo.idea.util.projectWizard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.consulo.idea.IdeaConstants;
import org.consulo.idea.IdeaIcons;
import org.consulo.idea.model.IdeaModuleTableModel;
import org.consulo.idea.model.IdeaProjectModel;
import org.consulo.idea.util.IdeaModuleTypeToModuleExtensionConverterEP;
import org.consulo.module.extension.ModuleExtensionWithSdk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.projectImport.ProjectImportBuilder;
import lombok.val;

/**
 * @author VISTALL
 * @since 18:49/14.06.13
 */
public class IdeaProjectImportBuilder extends ProjectImportBuilder<Object>
{
	@NotNull
	@Override
	public String getName()
	{
		return "IntelliJ IDEA";
	}

	@Override
	public Icon getIcon()
	{
		return IdeaIcons.Idea;
	}

	@Override
	public List<Object> getList()
	{
		return null;
	}

	@Override
	public boolean isMarked(Object element)
	{
		return false;
	}

	@Override
	public void setList(List<Object> list) throws ConfigurationException
	{
	}

	@Override
	public void setOpenProjectSettingsAfter(boolean on)
	{
	}

	@Nullable
	@Override
	public List<Module> commit(Project project, ModifiableModuleModel originalModel, ModulesProvider modulesProvider,
			ModifiableArtifactModel artifactModel)
	{
		val projectPath = project.getBasePath();
		val file = new File(projectPath, IdeaConstants.PROJECT_DIR);
		if(!file.exists())
		{
			return null;
		}

		val ideaProjectModel = new IdeaProjectModel(file);

		val fromProjectStructure = originalModel != null;
		val newModel = fromProjectStructure ? originalModel : ModuleManager.getInstance(project).getModifiableModel();

		val modules = new ArrayList<Module>();
		for(val ideaModuleModel : ideaProjectModel.getInstance(IdeaModuleTableModel.class).getModules())
		{
			val moduleFile = new File(ideaModuleModel.getFilePath());

			val module = newModel.newModule(FileUtil.getNameWithoutExtension(moduleFile), moduleFile.getParent());

			modules.add(module);

			val modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();

			for(val ep : IdeaModuleTypeToModuleExtensionConverterEP.EP_NAME.getExtensions())
			{
				if(ep.getKey().equals(ideaModuleModel.getModuleType()))
				{
					ep.getInstance().convertTypeToExtension(modifiableModel);
					break;
				}
			}

			for(val moduleExtension : modifiableModel.getExtensions())
			{
				if(moduleExtension instanceof ModuleExtensionWithSdk)
				{
					if(((ModuleExtensionWithSdk) moduleExtension).getInheritableSdk().isNull())
					{
						continue;
					}
					modifiableModel.addModuleExtensionSdkEntry((ModuleExtensionWithSdk<?>) moduleExtension);
				}
			}

			new WriteAction<Object>()
			{
				@Override
				protected void run(Result<Object> result) throws Throwable
				{
					modifiableModel.commit();
				}
			}.execute();
		}

		new WriteAction<Object>()
		{
			@Override
			protected void run(Result<Object> result) throws Throwable
			{
				if(!fromProjectStructure)
				{
					newModel.commit();
				}
			}
		}.execute();
		return modules;
	}
}
