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

package consulo.idea.impl.util.projectWizard;

import consulo.disposer.Disposable;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import consulo.localize.LocalizeValue;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.wizard.WizardStep;
import consulo.ui.layout.LabeledLayout;
import consulo.ui.layout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class IdeaModuleConfigurationStep implements WizardStep<IdeaImportContext> {
    private final Map<String, IdeaModuleTypeConfigurationPanel> myMap;

    public IdeaModuleConfigurationStep(Map<String, IdeaModuleTypeConfigurationPanel> map) {
        myMap = map;
    }

    @RequiredUIAccess
    @Nonnull
    @Override
    public Component getComponent(@Nonnull IdeaImportContext context, @Nonnull Disposable uiDisposable) {
        VerticalLayout layout = VerticalLayout.create();
        for (Map.Entry<String, IdeaModuleTypeConfigurationPanel> entry : myMap.entrySet()) {
            Component component = entry.getValue().createComponent(uiDisposable);

            layout.add(LabeledLayout.create(LocalizeValue.of(entry.getKey()), component));
        }
        return layout;
    }
}
