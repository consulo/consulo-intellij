/*
 * Copyright 2013 Consulo.org
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

import com.intellij.openapi.roots.ModuleRootModel;
import org.consulo.idea.util.IdeaModuleTypeToModuleExtensionConverter;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 16:27/15.06.13
 */
public class JavaIdeaModuleTypeToModuleExtensionConverter extends IdeaModuleTypeToModuleExtensionConverter {
  @Override
  public void convertTypeToExtension(@NotNull ModuleRootModel moduleRootModel) {
    enableExtensionById("java", moduleRootModel);
  }
}
