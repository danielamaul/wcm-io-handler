/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
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
package io.wcm.handler.media.format;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.sling.commons.resource.ImmutableValueMap;

/**
 * Fluent API for building media format definitions.
 */
@ProviderType
public final class MediaFormatBuilder {

  private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-\\_]+$");

  private final String name;
  private String label;
  private String description;
  private long width;
  private long minWidth;
  private long maxWidth;
  private long height;
  private long minHeight;
  private long maxHeight;
  private double ratio;
  private double ratioWidth;
  private double ratioHeight;
  private long fileSizeMax;
  private String[] extensions;
  private String renditionGroup;
  private boolean download;
  private boolean internal;
  private int ranking;
  private final Map<String, Object> properties = new HashMap<>();

  private MediaFormatBuilder(String name) {
    if (name == null || !NAME_PATTERN.matcher(name).matches()) {
      throw new IllegalArgumentException("Invalid name: " + name);
    }
    this.name = name;
  }

  /**
   * Create a new media format builder.
   * @param name Media format name. Only characters, numbers, hyphen and underline are allowed.
   * @return Media format builder
   */
  public static @NotNull MediaFormatBuilder create(@NotNull String name) {
    return new MediaFormatBuilder(name);
  }

  /**
   * @param value Label for displaying to user
   * @return this
   */
  public @NotNull MediaFormatBuilder label(String value) {
    this.label = value;
    return this;
  }

  /**
   * @param value Description for displaying to user
   * @return this
   */
  public @NotNull MediaFormatBuilder description(String value) {
    this.description = value;
    return this;
  }

  /**
   * @param value Fixed image width (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder width(long value) {
    this.width = value;
    return this;
  }

  /**
   * @param value Image width min (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder minWidth(long value) {
    this.minWidth = value;
    return this;
  }

  /**
   * @param value Image width max (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder maxWidth(long value) {
    this.maxWidth = value;
    return this;
  }

  /**
   * @param min Image width min (px)
   * @param max Image width max (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder width(long min, long max) {
    this.minWidth = min;
    this.maxWidth = max;
    return this;
  }

  /**
   * @param value Fixed image height (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder height(long value) {
    this.height = value;
    return this;
  }

  /**
   * @param value Image height min (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder minHeight(long value) {
    this.minHeight = value;
    return this;
  }

  /**
   * @param value Image height max (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder maxHeight(long value) {
    this.maxHeight = value;
    return this;
  }

  /**
   * @param min Image height min (px)
   * @param max Image height max (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder height(long min, long max) {
    this.minHeight = min;
    this.maxHeight = max;
    return this;
  }

  /**
   * @param widthValue Fixed image width (px)
   * @param heightValue Fixed image height (px)
   * @return this
   */
  public @NotNull MediaFormatBuilder fixedDimension(long widthValue, long heightValue) {
    this.width = widthValue;
    this.height = heightValue;
    return this;
  }

  /**
   * @param value Ratio (width/height)
   * @return this
   */
  public @NotNull MediaFormatBuilder ratio(double value) {
    this.ratio = value;
    return this;
  }

  /**
   * @param widthValue Ratio width sample value (is used for calculating the ratio together with ratioHeight, and for
   *          display)
   * @param heightValue Ratio height sample value (is used for calculating the ratio together with ratioWidth, and for
   *          display)
   * @return this
   */
  public @NotNull MediaFormatBuilder ratio(long widthValue, long heightValue) {
    this.ratioWidth = widthValue;
    this.ratioHeight = heightValue;
    return this;
  }

  /**
   * @param widthValue Ratio width sample value (is used for calculating the ratio together with ratioHeight, and for
   *          display)
   * @param heightValue Ratio height sample value (is used for calculating the ratio together with ratioWidth, and for
   *          display)
   * @return this
   */
  public @NotNull MediaFormatBuilder ratio(double widthValue, double heightValue) {
    this.ratioWidth = widthValue;
    this.ratioHeight = heightValue;
    return this;
  }

  /**
   * @param value Max. file size (bytes)
   * @return this
   */
  public @NotNull MediaFormatBuilder fileSizeMax(long value) {
    this.fileSizeMax = value;
    return this;
  }

  /**
   * @param value Allowed file extensions
   * @return this
   */
  public @NotNull MediaFormatBuilder extensions(String... value) {
    this.extensions = value != null ? value.clone() : null;
    return this;
  }

  /**
   * @param value Rendition group id
   * @return this
   */
  public @NotNull MediaFormatBuilder renditionGroup(String value) {
    this.renditionGroup = value;
    return this;
  }

  /**
   * @param value Media assets with this format should be downloaded and not displayed directly
   * @return this
   */
  public @NotNull MediaFormatBuilder download(boolean value) {
    this.download = value;
    return this;
  }

  /**
   * @param value For internal use only (not displayed for user)
   * @return this
   */
  public @NotNull MediaFormatBuilder internal(boolean value) {
    this.internal = value;
    return this;
  }

  /**
   * @param value Ranking for controlling priority in auto-detection. Lowest value = highest priority.
   * @return this
   */
  public @NotNull MediaFormatBuilder ranking(int value) {
    this.ranking = value;
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @param map Property map. Is merged with properties already set in builder.
   * @return this
   */
  public @NotNull MediaFormatBuilder properties(Map<String, Object> map) {
    if (map == null) {
      throw new IllegalArgumentException("Map argument must not be null.");
    }
    this.properties.putAll(map);
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @param key Property key
   * @param value Property value
   * @return this
   */
  public @NotNull MediaFormatBuilder property(String key, Object value) {
    if (key == null) {
      throw new IllegalArgumentException("Key argument must not be null.");
    }
    this.properties.put(key, value);
    return this;
  }

  /**
   * Builds the media format definition.
   * @return Media format definition
   */
  public @NotNull MediaFormat build() {
    if (this.name == null) {
      throw new IllegalArgumentException("Name is missing.");
    }
    return new MediaFormat(
        name,
        label,
        description,
        width,
        minWidth,
        maxWidth,
        height,
        minHeight,
        maxHeight,
        ratio,
        ratioWidth,
        ratioHeight,
        fileSizeMax,
        nonNullArray(extensions),
        renditionGroup,
        download,
        internal,
        ranking,
        ImmutableValueMap.copyOf(properties));
  }

  private static String[] nonNullArray(String[] value) {
    if (value == null) {
      return new String[0];
    }
    else {
      return value;
    }
  }

}
