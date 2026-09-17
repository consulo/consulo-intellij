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

package consulo.idea.impl.java;

import com.intellij.java.language.LanguageLevel;
import com.intellij.java.language.projectRoots.JavaSdkType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModel;
import consulo.content.bundle.SdkModelFactory;
import consulo.disposer.Disposable;
import consulo.idea.model.IdeaProjectModel;
import consulo.idea.model.IdeaProjectRootModel;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import consulo.localize.LocalizeValue;
import consulo.module.ui.BundleBox;
import consulo.module.ui.BundleBoxBuilder;
import consulo.ui.ComboBox;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import consulo.ui.util.LabeledBuilder;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class JavaConfigurationPanel implements IdeaModuleTypeConfigurationPanel {
    private final IdeaProjectModel myIdeaProjectModel;

    private BundleBox myBundleBox;
    private ComboBox<LanguageLevel> myLanguageLevelBox;

    public JavaConfigurationPanel(IdeaProjectModel ideaProjectModel) {
        myIdeaProjectModel = ideaProjectModel;
    }

    @RequiredUIAccess
    @Nonnull
    @Override
    public Component createComponent(@Nonnull Disposable uiDisposable) {
        SdkModel model = SdkModelFactory.getInstance().getOrCreateModel();

        myBundleBox = BundleBoxBuilder.create(model, uiDisposable)
            .withSdkTypeFilter(it -> it instanceof JavaSdkType)
            .withNoneItem()
            .build();

        IdeaProjectRootModel projectRootModel = myIdeaProjectModel.getInstance(IdeaProjectRootModel.class);

        String jdkProperty = projectRootModel.getProperty("project-jdk-name");
        if (jdkProperty != null) {
            Sdk jdk = model.findSdk(jdkProperty);
            if (jdk == null) {
                myBundleBox.setInvalidBundle(jdkProperty);
            }
            else {
                myBundleBox.setSelectedBundle(jdk.getName());
            }
        }

        myLanguageLevelBox = ComboBox.create(LanguageLevel.values());
        myLanguageLevelBox.setTextRenderer(LanguageLevel::getDescription);
        myLanguageLevelBox.setValue(LanguageLevel.HIGHEST);

        String languageLevelProperty = projectRootModel.getProperty("languageLevel");
        if (languageLevelProperty != null) {
            try {
                LanguageLevel languageLevel = LanguageLevel.valueOf(languageLevelProperty);
                myLanguageLevelBox.setValue(languageLevel);
            }
            catch (IllegalArgumentException ignored) {
            }
        }

        VerticalLayout layout = VerticalLayout.create();
        layout.add(LabeledBuilder.simple(LocalizeValue.localizeTODO("Java Project SDK"), myBundleBox));
        layout.add(LabeledBuilder.simple(LocalizeValue.localizeTODO("Java Language Level"), myLanguageLevelBox));
        addOtherComponents(layout, model);
        return layout;
    }

    @RequiredUIAccess
    protected void addOtherComponents(VerticalLayout layout, SdkModel projectSdksModel) {
    }

    public BundleBox getBundleBox() {
        return myBundleBox;
    }

    public ComboBox<LanguageLevel> getLanguageLevelBox() {
        return myLanguageLevelBox;
    }
}
