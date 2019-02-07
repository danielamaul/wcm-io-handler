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
package io.wcm.handler.mediasource.dam.impl;

import static io.wcm.handler.media.testcontext.DummyMediaFormats.EDITORIAL_1COL;
import static io.wcm.handler.media.testcontext.DummyMediaFormats.EDITORIAL_2COL;
import static io.wcm.handler.media.testcontext.DummyMediaFormats.RATIO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.service.event.EventHandler;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamEvent;

import io.wcm.handler.media.Media;
import io.wcm.handler.media.MediaHandler;
import io.wcm.handler.media.MediaNameConstants;
import io.wcm.handler.media.Rendition;
import io.wcm.handler.media.testcontext.AppAemContext;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.wcm.commons.contenttype.ContentType;

public class AutoCroppingMediaHandlerTest {

  @Rule
  public final AemContext context = AppAemContext.newAemContext();

  private MediaHandler mediaHandler;
  private Resource resource;

  @Before
  public void setUp() {
    context.runMode("author");
    EventHandler eventHandler = context.registerInjectActivateService(new DamRenditionMetadataService());
    mediaHandler = AdaptTo.notNull(context.request(), MediaHandler.class);

    // prepare asset with web rendition
    Asset asset = context.create().asset("/content/dam/test.jpg", 400, 200, ContentType.JPEG);
    com.day.cq.dam.api.Rendition rendition = context.create().assetRendition(asset,
        "cq5dam.web.300.150.jpg", 300, 150, ContentType.JPEG);
    eventHandler.handleEvent(DamEvent.renditionUpdated(asset.getPath(), "admin", rendition.getPath()).toEvent());

    // prepare component with auto-cropping
    context.create().resource("/apps/app1/components/comp1",
        MediaNameConstants.PN_COMPONENT_MEDIA_AUTOCROP, true);

    // prepare resource with asset reference
    resource = context.create().resource("/content/test",
        "sling:resourceType", "app1/components/comp1",
        "mediaRef", asset.getPath());
  }

  @Test
  public void testMediaFormatWithRatio() {
    Media media = mediaHandler.get(resource)
        .mediaFormat(RATIO)
        .build();
    assertTrue(media.isValid());
    Rendition rendition = media.getRendition();
    assertEquals(320, rendition.getWidth());
    assertEquals(200, rendition.getHeight());
    assertEquals("/content/dam/test.jpg/_jcr_content/renditions/original.image_file.320.200.40,0,360,200.file/test.jpg", media.getUrl());
  }

  @Test
  public void testMediaFormatFixedDimension() {
    Media media = mediaHandler.get(resource)
        .mediaFormat(EDITORIAL_1COL)
        .build();
    assertTrue(media.isValid());
    Rendition rendition = media.getRendition();
    assertEquals(215, rendition.getWidth());
    assertEquals(102, rendition.getHeight());
    assertEquals("/content/dam/test.jpg/_jcr_content/renditions/original.image_file.215.102.0,5,400,194.file/test.jpg", media.getUrl());
  }

  @Test
  public void testMultipleMediaFormatsFixedDimension() {
    Media media = mediaHandler.get(resource)
        .mediaFormats(EDITORIAL_2COL, EDITORIAL_1COL)
        .build();
    assertTrue(media.isValid());
    Rendition rendition = media.getRendition();
    assertEquals(215, rendition.getWidth());
    assertEquals(102, rendition.getHeight());
    assertEquals("/content/dam/test.jpg/_jcr_content/renditions/original.image_file.215.102.0,5,400,194.file/test.jpg", media.getUrl());
  }

  @Test
  public void testMediaFormatFixedDimension_NoMatch() {
    Media media = mediaHandler.get(resource)
        .mediaFormat(EDITORIAL_2COL)
        .build();
    assertFalse(media.isValid());
  }

}