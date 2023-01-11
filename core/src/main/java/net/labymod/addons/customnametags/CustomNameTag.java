/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.labymod.addons.customnametags;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;

public class CustomNameTag {

  private boolean enabled;
  private String customName;
  private boolean replaceScoreboard;

  private transient Component displayName;

  private CustomNameTag(boolean enabled, String customName, boolean replaceScoreboard) {
    this.enabled = enabled;
    this.customName = customName;
    this.replaceScoreboard = replaceScoreboard;
  }

  public static CustomNameTag create(boolean enabled, String customName,
      boolean replaceScoreboard) {
    return new CustomNameTag(enabled, customName, replaceScoreboard);
  }

  public static CustomNameTag createDefault() {
    return create(true, "", false);
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getCustomName() {
    return this.customName;
  }

  public void setCustomName(String customName) {
    if(this.customName.equals(customName)) {
      return;
    }

    this.customName = customName;
    this.displayName = null;
  }

  public boolean isReplaceScoreboard() {
    return this.replaceScoreboard;
  }

  public void setReplaceScoreboard(boolean replaceScoreboard) {
    this.replaceScoreboard = replaceScoreboard;
  }

  public Component displayName() {
    if(this.displayName == null) {
      this.displayName = LegacyComponentSerializer.legacyAmpersand().deserialize(this.customName);
    }

    return this.displayName;
  }
}
