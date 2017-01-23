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
package consulo.idea.util.projectWizard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ex.ProjectManagerEx;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ContentFolder;
import com.intellij.openapi.roots.ExportableOrderEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.projectImport.ProjectImportBuilder;
import consulo.annotations.RequiredReadAction;
import consulo.idea.IdeaConstants;
import consulo.idea.IdeaIcons;
import consulo.idea.model.IdeaContentEntryModel;
import consulo.idea.model.IdeaContentFolderModel;
import consulo.idea.model.IdeaLibraryModel;
import consulo.idea.model.IdeaModuleModel;
import consulo.idea.model.IdeaModuleTableModel;
import consulo.idea.model.IdeaOrderRootType;
import consulo.idea.model.IdeaProjectLibraryTableModel;
import consulo.idea.model.IdeaProjectModel;
import consulo.idea.model.orderEnties.IdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleLibraryIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ProjectLibraryIdeaOrderEntryModel;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import consulo.idea.util.IdeaModuleTypeToModuleExtensionConverter;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.ModuleExtensionWithSdk;
import consulo.roots.ContentFolderTypeProvider;
import consulo.roots.impl.ProductionContentFolderTypeProvider;
import consulo.roots.impl.ProductionResourceContentFolderTypeProvider;
import consulo.roots.impl.TestContentFolderTypeProvider;
import consulo.roots.impl.TestResourceContentFolderTypeProvider;
import consulo.roots.impl.property.GeneratedContentFolderPropertyProvider;

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
	@RequiredReadAction
	public List<Module> commit(Project project, ModifiableModuleModel originalModel, ModulesProvider modulesProvider, ModifiableArtifactModel artifactModel)
	{
		File dotIdeaDirectory = new File(getFileToImport(), IdeaConstants.PROJECT_DIR);
		if(!dotIdeaDirectory.exists())
		{
			return null;
		}

		IdeaProjectModel ideaProjectModel = new IdeaProjectModel(dotIdeaDirectory);

		boolean projectNull = project == null;
		if(project == null)
		{
			project = ProjectManager.getInstance().createProject(ideaProjectModel.getName(), getFileToImport());
		}

		boolean fromProjectStructure = originalModel != null;
		ModifiableModuleModel newModel = fromProjectStructure ? originalModel : ModuleManager.getInstance(project).getModifiableModel();

		List<IdeaModuleModel> ideaModuleModels = ideaProjectModel.getInstance(IdeaModuleTableModel.class).getModules();

		Map<String, IdeaModuleTypeConfigurationPanel> map = new HashMap<String, IdeaModuleTypeConfigurationPanel>();

		for(IdeaModuleModel ideaModuleModel : ideaModuleModels)
		{
			String moduleType = ideaModuleModel.getModuleType();
			IdeaModuleTypeToModuleExtensionConverter instance = IdeaModuleTypeToModuleExtensionConverter.EP.findSingle(moduleType);

			IdeaModuleTypeConfigurationPanel configurationPanel = instance.createConfigurationPanel(project, ideaProjectModel, ideaModuleModel);
			if(configurationPanel != null)
			{
				map.put(moduleType, configurationPanel);
			}
		}

		if(!map.isEmpty())
		{
			IdeaModuleConfigurationDialog dialog = new IdeaModuleConfigurationDialog(project, map);

			dialog.show();
		}

		List<Module> modules = new ArrayList<Module>();
		for(IdeaModuleModel ideaModuleModel : ideaModuleModels)
		{
			File moduleFile = ideaModuleModel.getFile();

			String nameWithoutExtension = FileUtil.getNameWithoutExtension(moduleFile);

			List<IdeaContentEntryModel> contentEntries = ideaModuleModel.getContentEntries();
			String modulePath = null;
			if(!contentEntries.isEmpty())
			{
				modulePath = VfsUtil.urlToPath(contentEntries.get(0).getUrl());
			}

			Module module = newModel.newModule(nameWithoutExtension, modulePath);

			modules.add(module);

			final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();

			String group = ideaModuleModel.getGroup();
			if(group != null)
			{
				newModel.setModuleGroupPath(module, group.split("/"));
			}

			for(IdeaContentEntryModel ideaContentEntryModel : contentEntries)
			{
				ContentEntry contentEntry = modifiableModel.addContentEntry(ideaContentEntryModel.getUrl());

				for(IdeaContentFolderModel entry : ideaContentEntryModel.getContentFolders())
				{
					ContentFolderTypeProvider provider = ProductionContentFolderTypeProvider.getInstance();
					if(entry.getBoolProperty("isTestSource"))
					{
						provider = TestContentFolderTypeProvider.getInstance();
					}

					String type = entry.getProperty("type");
					if("java-resource".equals(type))
					{
						provider = ProductionResourceContentFolderTypeProvider.getInstance();
					}
					else if("java-test-resource".equals(type))
					{
						provider = TestResourceContentFolderTypeProvider.getInstance();
					}

					ContentFolder contentFolder = contentEntry.addFolder(entry.getUrl(), provider);

					if(entry.getBoolProperty("generated"))
					{
						contentFolder.setPropertyValue(GeneratedContentFolderPropertyProvider.IS_GENERATED, Boolean.TRUE);
					}
				}
			}

			for(IdeaOrderEntryModel orderEntryModel : ideaModuleModel.getOrderEntries())
			{
				OrderEntry orderEntry = null;
				if(orderEntryModel instanceof ModuleIdeaOrderEntryModel)
				{
					orderEntry = modifiableModel.addInvalidModuleEntry(((ModuleIdeaOrderEntryModel) orderEntryModel).getModuleName());
				}
				else if(orderEntryModel instanceof ProjectLibraryIdeaOrderEntryModel)
				{
					orderEntry = modifiableModel.addInvalidLibrary(((ProjectLibraryIdeaOrderEntryModel) orderEntryModel).getLibraryName(), "project");
				}
				else if(orderEntryModel instanceof ModuleLibraryIdeaOrderEntryModel)
				{
					IdeaLibraryModel libraryModel = ((ModuleLibraryIdeaOrderEntryModel) orderEntryModel).getLibraryModel();

					Library library = modifiableModel.getModuleLibraryTable().createLibrary(libraryModel.getName());

					convertLibrary(library, libraryModel);

					orderEntry = modifiableModel.findLibraryOrderEntry(library);
				}

				//noinspection ConstantConditions
				if(orderEntry instanceof ExportableOrderEntry)
				{
					((ExportableOrderEntry) orderEntry).setExported(orderEntryModel.isExported());
				}
			}

			String moduleType = ideaModuleModel.getModuleType();

			IdeaModuleTypeConfigurationPanel ideaModuleTypeConfigurationPanel = map.get(moduleType);
			if(ideaModuleTypeConfigurationPanel != null)
			{
				IdeaModuleTypeToModuleExtensionConverter converter = IdeaModuleTypeToModuleExtensionConverter.EP.findSingle(moduleType);
				assert converter != null;
				//noinspection unchecked
				converter.convertTypeToExtension(modifiableModel, ideaModuleModel, ideaModuleTypeConfigurationPanel);
			}

			for(ModuleExtension moduleExtension : modifiableModel.getExtensions())
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

		LibraryTable libraryTable = ProjectLibraryTable.getInstance(project);

		LibraryTable.ModifiableModel libraryTableModifiableModel = libraryTable.getModifiableModel();

		for(IdeaLibraryModel ideaLibraryModel : ideaProjectModel.getInstance(IdeaProjectLibraryTableModel.class).getLibraries())
		{
			Library library = libraryTableModifiableModel.createLibrary(ideaLibraryModel.getName());

			convertLibrary(library, ideaLibraryModel);
		}

		new WriteAction<Object>()
		{
			@Override
			protected void run(Result<Object> result) throws Throwable
			{
				libraryTableModifiableModel.commit();
				if(!fromProjectStructure)
				{
					newModel.commit();
				}
			}
		}.execute();

		if(projectNull)
		{
			ProjectManagerEx.getInstanceEx().openProject(project);
		}
		return modules;
	}

	private static void convertLibrary(Library library, IdeaLibraryModel ideaLibraryModel)
	{
		Library.ModifiableModel modifiableModel = library.getModifiableModel();
		for(Map.Entry<IdeaOrderRootType, Collection<String>> entry : ideaLibraryModel.getOrderRoots().entrySet())
		{
			for(String url : entry.getValue())
			{
				modifiableModel.addRoot(url, entry.getKey().getOrderRootType());
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
}