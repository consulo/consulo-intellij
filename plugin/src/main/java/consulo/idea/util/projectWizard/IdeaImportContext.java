/*
 * Copyright 2013-2017 must-be.org
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import consulo.idea.IdeaConstants;
import consulo.idea.model.IdeaModuleModel;
import consulo.idea.model.IdeaModuleTableModel;
import consulo.idea.model.IdeaProjectModel;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import consulo.idea.util.IdeaModuleTypeToModuleExtensionConverter;
import consulo.moduleImport.ModuleImportContext;

/**
 * @author VISTALL
 * @since 24-Dec-17
 */
public class IdeaImportContext extends ModuleImportContext
{
	private IdeaProjectModel myIdeaProjectModel;
	private Map<String, IdeaModuleTypeConfigurationPanel> myConfigurationPanels;

	@Override
	public ModuleImportContext setFileToImport(String fileToImport)
	{
		File dotIdeaDirectory = new File(fileToImport, IdeaConstants.PROJECT_DIR);
		if(!dotIdeaDirectory.exists())
		{
			return super.setFileToImport(fileToImport);
		}

		myIdeaProjectModel = new IdeaProjectModel(dotIdeaDirectory);

		return super.setFileToImport(fileToImport);
	}

	@Nonnull
	public Map<String, IdeaModuleTypeConfigurationPanel> getConfiguration()
	{
		if(myIdeaProjectModel == null)
		{
			return Collections.emptyMap();
		}

		if(myConfigurationPanels != null)
		{
			return myConfigurationPanels;
		}

		List<IdeaModuleModel> ideaModuleModels = myIdeaProjectModel.getInstance(IdeaModuleTableModel.class).getModules();

		Map<String, IdeaModuleTypeConfigurationPanel> map = new HashMap<>();
		myConfigurationPanels = map;

		for(IdeaModuleModel ideaModuleModel : ideaModuleModels)
		{
			String moduleType = ideaModuleModel.getModuleType();
			IdeaModuleTypeToModuleExtensionConverter instance = IdeaModuleTypeToModuleExtensionConverter.EP.findSingle(moduleType);

			IdeaModuleTypeConfigurationPanel configurationPanel = instance.createConfigurationPanel(myIdeaProjectModel, ideaModuleModel);
			if(configurationPanel != null)
			{
				map.put(moduleType, configurationPanel);
			}
		}
		return map;
	}

	@Nullable
	public IdeaProjectModel getIdeaProjectModel()
	{
		return myIdeaProjectModel;
	}
}
