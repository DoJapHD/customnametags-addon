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

package net.labymod.addons.customnametags.listener;

import java.util.Map.Entry;
import java.util.Optional;
import net.labymod.addons.customnametags.CustomNameTag;
import net.labymod.addons.customnametags.CustomNameTags;
import net.labymod.addons.customnametags.CustomNameTagsConfiguration;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.event.NameTagBackgroundRenderEvent;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;

public class PlayerNameTagRenderListener {

  private final CustomNameTags addon;

  public PlayerNameTagRenderListener(CustomNameTags addon) {
    this.addon = addon;
  }

  @Subscribe
  public void modifyNameTagBackground(NameTagBackgroundRenderEvent event) {
    CustomNameTagsConfiguration configuration = this.addon.configuration();
    event.setCancelled(configuration.shouldHideNameTagBackground().get());
    event.setColor(configuration.color().get().get());
  }

  @Subscribe
  public void onPlayerNameTagRender(PlayerNameTagRenderEvent event) {
    NetworkPlayerInfo networkPlayerInfo = event.playerInfo();
    if (networkPlayerInfo == null) {
      return;
    }

    String playerName = networkPlayerInfo.profile().getUsername();
    Optional<CustomNameTag> optionalCustomTag = this.getCustomNameTag(playerName);
    if (!optionalCustomTag.isPresent() || !optionalCustomTag.get().isEnabled()) {
      return;
    }

    CustomNameTag customNameTag = optionalCustomTag.get();
    if (customNameTag.isReplaceScoreboard()) {
      event.setNameTag(customNameTag.displayName().copy());
    } else {
      Component newNameTag = event.nameTag().copy();
      this.addon.replaceUsername(newNameTag, playerName, () -> customNameTag.displayName().copy());
      event.setNameTag(newNameTag);
    }
  }

  private Optional<CustomNameTag> getCustomNameTag(String playerName) {
    for (Entry<String, CustomNameTag> customTagEntry : this.addon.configuration().getCustomTags()
        .entrySet()) {
      CustomNameTag customNameTag = customTagEntry.getValue();
      if (customTagEntry.getKey().equalsIgnoreCase(playerName)) {
        return Optional.of(customNameTag);
      }
    }

    return Optional.empty();
  }
}
