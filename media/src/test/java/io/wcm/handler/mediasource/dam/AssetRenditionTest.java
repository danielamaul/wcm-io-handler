/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.handler.mediasource.dam;

import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static io.wcm.handler.mediasource.dam.impl.metadata.RenditionMetadataNameConstants.NN_RENDITIONS_METADATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;

import io.wcm.handler.media.Dimension;
import io.wcm.handler.mediasource.dam.impl.metadata.RenditionMetadataListenerService;
import io.wcm.handler.mediasource.dam.impl.metadata.AssetSynchonizationService;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import io.wcm.wcm.commons.util.RunMode;

@ExtendWith(AemContextExtension.class)
class AssetRenditionTest {

  private final AemContext context = new AemContext();

  private Asset asset;
  private Rendition original;
  private Rendition rendition;

  @BeforeEach
  void setUp() {
    // register DamRenditionMetadataService (which is only active on author run mode) to generate rendition metadata
    context.runMode(RunMode.AUTHOR);
    context.registerInjectActivateService(new AssetSynchonizationService());
    context.registerInjectActivateService(new RenditionMetadataListenerService());

    asset = context.create().asset("/content/dam/asset1.jpg", 16, 9, "image/jpeg");
    original = asset.getOriginal();
    rendition = context.create().assetRendition(asset, "rendition1.png", 10, 5, "image/png");
  }

  @Test
  void testIsOriginal() {
    assertTrue(AssetRendition.isOriginal(original));
    assertFalse(AssetRendition.isOriginal(rendition));
  }

  @Test
  void testGetFilename() {
    assertEquals("asset1.jpg", AssetRendition.getFilename(original));
    assertEquals("rendition1.png", AssetRendition.getFilename(rendition));
  }

  @Test
  void testGetDimension() {
    assertEquals(new Dimension(16, 9), AssetRendition.getDimension(original));
    assertEquals(new Dimension(10, 5), AssetRendition.getDimension(rendition));
  }

  @Test
  void testGetDimensionWithoutRenditionsMetadata() throws PersistenceException {
    // remove renditions metadata generated by DamRenditionMetadataService
    Resource renditionsMetadata = AdaptTo.notNull(asset, Resource.class).getChild(JCR_CONTENT + "/" + NN_RENDITIONS_METADATA);
    if (renditionsMetadata != null) {
      context.resourceResolver().delete(renditionsMetadata);
    }

    assertEquals(new Dimension(16, 9), AssetRendition.getDimension(original, true));
    assertEquals(new Dimension(10, 5), AssetRendition.getDimension(rendition, true));
  }

  @Test
  void testIsThumbnailRendition() {
    assertTrue(AssetRendition.isThumbnailRendition(renditionByName("cq5dam.thumbnail.10.10.png")));
    assertFalse(AssetRendition.isThumbnailRendition(renditionByName("cq5dam.web.100.100.jpg")));
    assertFalse(AssetRendition.isThumbnailRendition(renditionByName("othername.gif")));
  }

  @Test
  void testIsWebRendition() {
    assertFalse(AssetRendition.isWebRendition(renditionByName("cq5dam.thumbnail.10.10.png")));
    assertTrue(AssetRendition.isWebRendition(renditionByName("cq5dam.web.100.100.jpg")));
    assertFalse(AssetRendition.isWebRendition(renditionByName("othername.gif")));
  }

  @SuppressWarnings("null")
  private static Rendition renditionByName(String name) {
    Rendition rendition = mock(Rendition.class);
    when(rendition.getName()).thenReturn(name);
    return rendition;
  }

}
