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
package io.wcm.handler.media.impl;

import static io.wcm.handler.media.impl.ImageTransformation.ROTATE_180;
import static io.wcm.handler.media.impl.ImageTransformation.ROTATE_270;
import static io.wcm.handler.media.impl.ImageTransformation.ROTATE_90;
import static io.wcm.handler.media.impl.ImageTransformation.calculateAutoCropDimension;
import static io.wcm.handler.media.impl.ImageTransformation.isValidRotation;
import static io.wcm.handler.media.impl.ImageTransformation.rotateMapDimension;
import static io.wcm.handler.media.impl.ImageTransformation.rotateMapHeight;
import static io.wcm.handler.media.impl.ImageTransformation.rotateMapWidth;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.wcm.handler.media.CropDimension;
import io.wcm.handler.media.Dimension;

class ImageTransformationTest {

  @Test
  void testIsValidRotation() {
    assertTrue(isValidRotation(90));
    assertTrue(isValidRotation(180));
    assertTrue(isValidRotation(270));
    assertFalse(isValidRotation(0));
    assertFalse(isValidRotation(45));
    assertFalse(isValidRotation(-90));
  }

  @Test
  void testRotateMapWidth() {
    assertEquals(20, rotateMapWidth(20, 10, null));
    assertEquals(10, rotateMapWidth(20, 10, ROTATE_90));
    assertEquals(20, rotateMapWidth(20, 10, ROTATE_180));
    assertEquals(10, rotateMapWidth(20, 10, ROTATE_270));
  }

  @Test
  void testRotateMapHeight() {
    assertEquals(10, rotateMapHeight(20, 10, null));
    assertEquals(20, rotateMapHeight(20, 10, ROTATE_90));
    assertEquals(10, rotateMapHeight(20, 10, ROTATE_180));
    assertEquals(20, rotateMapHeight(20, 10, ROTATE_270));
  }

  @Test
  void testRotateMapDimension() {
    assertEquals(new Dimension(20, 10), rotateMapDimension(new Dimension(20, 10), null));
    assertEquals(new Dimension(10, 20), rotateMapDimension(new Dimension(20, 10), ROTATE_90));
    assertEquals(new Dimension(20, 10), rotateMapDimension(new Dimension(20, 10), ROTATE_180));
    assertEquals(new Dimension(10, 20), rotateMapDimension(new Dimension(20, 10), ROTATE_270));
  }

  @Test
  void testRotateMapDimension_CropDimension() {
    assertEquals(new CropDimension(2, 4, 20, 10), rotateMapDimension(new CropDimension(2, 4, 20, 10), null));
    assertEquals(new CropDimension(4, 2, 10, 20), rotateMapDimension(new CropDimension(2, 4, 20, 10), ROTATE_90));
    assertEquals(new CropDimension(2, 4, 20, 10), rotateMapDimension(new CropDimension(2, 4, 20, 10), ROTATE_180));
    assertEquals(new CropDimension(4, 2, 10, 20), rotateMapDimension(new CropDimension(2, 4, 20, 10), ROTATE_270));
  }

  @Test
  void testCalculateAutoCropDimension_AdaptWidth() {
    CropDimension result = calculateAutoCropDimension(180, 90, 16d / 9d);
    assertEquals(10, result.getLeft());
    assertEquals(0, result.getTop());
    assertEquals(160, result.getWidth());
    assertEquals(90, result.getHeight());
  }

  @Test
  void testCalculateAutoCropDimension_AdaptHeight() {
    CropDimension result = calculateAutoCropDimension(160, 100, 16d / 9d);
    assertEquals(0, result.getLeft());
    assertEquals(5, result.getTop());
    assertEquals(160, result.getWidth());
    assertEquals(90, result.getHeight());
  }

}
