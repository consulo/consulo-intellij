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
package org.consulo.idea.util;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.util.KeyedLazyInstanceEP;

/**
 * @author VISTALL
 * @since 16:22/15.06.13
 */
public class IdeaModuleTypeToModuleExtensionConverterEP extends KeyedLazyInstanceEP<IdeaModuleTypeToModuleExtensionConverter> {
  public static final ExtensionPointName<IdeaModuleTypeToModuleExtensionConverterEP> EP_NAME = ExtensionPointName.create("org.consulo.intellij.moduleTypeToModuleExtensionConverter");
}
