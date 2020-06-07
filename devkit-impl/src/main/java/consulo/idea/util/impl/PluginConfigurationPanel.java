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

package consulo.idea.util.impl;

import com.intellij.openapi.roots.ui.configuration.projectRoot.DefaultSdksModel;
import com.intellij.openapi.ui.LabeledComponent;
import consulo.desktop.util.awt.component.VerticalLayoutPanel;
import consulo.idea.model.IdeaProjectModel;
import consulo.roots.ui.configuration.SdkComboBox;
import org.jetbrains.idea.devkit.sdk.ConsuloSdkType;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class PluginConfigurationPanel extends JavaConfigurationPanel
{
	private SdkComboBox myPluginSdkComboBox;

	public PluginConfigurationPanel(IdeaProjectModel ideaProjectModel)
	{
		super(ideaProjectModel);
	}

	@Override
	protected void addOtherComponents(VerticalLayoutPanel panel, DefaultSdksModel model)
	{
		myPluginSdkComboBox = new SdkComboBox(model, sdkTypeId -> sdkTypeId == ConsuloSdkType.getInstance(), true);
		panel.addComponent(LabeledComponent.create(myPluginSdkComboBox, "IntelliJ Project SDK"));
	}

	public SdkComboBox getPluginSdkComboBox()
	{
		return myPluginSdkComboBox;
	}
}
