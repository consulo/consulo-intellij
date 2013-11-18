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
package org.consulo.idea.model;

import org.mustbe.consulo.roots.ContentFolderTypeProvider;
import com.intellij.util.containers.MultiMap;

/**
 * @author VISTALL
 * @since 10:08/16.06.13
 */
public class IdeaContentEntryModel {
  private final String myUrl;
  private MultiMap<ContentFolderTypeProvider, String> myContentFolderTypes = new MultiMap<ContentFolderTypeProvider, String>();

  public IdeaContentEntryModel(String url) {
    myUrl = url;
  }

  public String getUrl() {
    return myUrl;
  }

  public MultiMap<ContentFolderTypeProvider, String> getContentFolderTypes() {
    return myContentFolderTypes;
  }

  public void addFolder(String url2, ContentFolderTypeProvider contentFolderType) {
    myContentFolderTypes.putValue(contentFolderType, url2);
  }
}
