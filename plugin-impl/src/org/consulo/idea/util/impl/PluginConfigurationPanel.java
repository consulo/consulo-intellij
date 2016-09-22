/*
 * Copyright 2013-2015 must-be.org
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

import org.consulo.idea.model.IdeaProjectModel;
import org.jetbrains.idea.devkit.sdk.ConsuloSdkType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.util.Condition;
import consulo.roots.ui.configuration.SdkComboBox;
import consulo.util.ui.components.VerticalLayoutPanel;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class PluginConfigurationPanel extends JavaConfigurationPanel
{
	private SdkComboBox myPluginSdkComboBox;

	public PluginConfigurationPanel(Project project, IdeaProjectModel ideaProjectModel)
	{
		super(project, ideaProjectModel);
	}

	@Override
	protected void addOtherComponents(VerticalLayoutPanel panel, ProjectSdksModel model)
	{
		myPluginSdkComboBox = new SdkComboBox(model, new Condition<SdkTypeId>()
		{
			@Override
			public boolean value(SdkTypeId sdkTypeId)
			{
				return sdkTypeId == ConsuloSdkType.getInstance();
			}
		}, true);
		panel.addComponent(LabeledComponent.left(myPluginSdkComboBox, "IntelliJ Project SDK"));
	}

	public SdkComboBox getPluginSdkComboBox()
	{
		return myPluginSdkComboBox;
	}
}
