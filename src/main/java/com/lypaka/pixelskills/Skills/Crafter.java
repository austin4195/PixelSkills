package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.config.ConfigManager;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Random;

public class Crafter {
    /**
     *
     * Crafter class handles Crafter, Blacksmith, and Scientist skills, as they fire off the same event
     *
     **/

    public Crafter (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @Listener
    public void onCraft (CraftItemEvent.Craft e, @Root Player player) {
        if (isCrafterItem(e.getCrafted().createStack())) {
            if (ConfigManager.getConfigNode("Skills", "Crafter", "isEnabled").getValue().equals(true)) {
                //I'm not sure why they would have Crafter enabled but have the only task for it disabled, but here we go
                if (ConfigManager.getConfigNode("Skills", "Crafter", "EXP", "Tasks", "Crafting Poke Balls", "isEnabled").getValue().equals(true)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Crafter", "EXP", "Tasks", "Crafting Poke Balls", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Crafter", "EXP", "expModifier").getInt();
                    if (e.getCrafted().getQuantity() > 1) {
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp * e.getCrafted().getQuantity() + " Crafter EXP points!"));
                    } else {
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Crafter EXP points!"));
                    }
                    plugin.addPoints("Crafter", exp, player);
                    if (plugin.didLevelUp("Crafter", player)) {
                        plugin.levelUp("Crafter", player);
                        if (ConfigManager.getConfigNode("Skills", "Crafter", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Crafter", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Crafter", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Crafter", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Crafter", "nextPerkIncreaseLevel").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Crafter", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Crafter", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Crafter", "chance at perks").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Crafter", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                }
                            }
                        }
                    }
                }
            }
        } else if (isScientistItem(e.getCrafted().createStack())) {
            if (ConfigManager.getConfigNode("Skills", "Scientist", "isEnabled").getValue().equals(true)) {
                //I'm not sure why they would have Scientist enabled but have the only task for it disabled, but here we go
                if (ConfigManager.getConfigNode("Skills", "Scientist", "EXP", "Tasks", "Crafting healing items", "isEnabled").getValue().equals(true)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Scientist", "EXP", "Tasks", "Crafting healing items", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Scientist", "EXP", "expModifier").getInt();
                    if (e.getCrafted().getQuantity() > 1) {
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp * e.getCrafted().getQuantity() + " Scientist EXP points!"));
                    } else {
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Scientist EXP points!"));
                    }
                    plugin.addPoints("Scientist", exp, player);
                    if (plugin.didLevelUp("Scientist", player)) {
                        plugin.levelUp("Scientist", player);
                        if (ConfigManager.getConfigNode("Skills", "Scientist", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Scientist", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Scientist", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Scientist", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Scientist", "nextPerkIncreaseLevel").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Scientist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Scientist", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Scientist", "chance at perks").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Scientist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                }
                            }
                        }
                    }
                }
            }
        } else if (isBlacksmithItem(e.getCrafted().createStack())) {
            if (ConfigManager.getConfigNode("Skills", "Blacksmith", "isEnabled").getValue().equals(true)) {
                if (ConfigManager.getConfigNode("Skills", "Blacksmith", "EXP", "Tasks", "Crafting Pixelmon tools", "isEnabled").getValue().equals(true)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Blacksmith", "EXP", "Tasks", "Crafting Pixelmon tools", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Blacksmith", "EXP", "expModifier").getInt();
                    if (e.getCrafted().getQuantity() > 1) {
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp * e.getCrafted().getQuantity() + " Blacksmith EXP points!"));
                    } else {
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Blacksmith EXP points!"));
                    }
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Blacksmith EXP points!"));
                    plugin.addPoints("Blacksmith", exp, player);
                    if (plugin.didLevelUp("Blacksmith", player)) {
                        plugin.levelUp("Blacksmith", player);
                        if (ConfigManager.getConfigNode("Skills", "Blacksmith", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Blacksmith", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Blacksmith", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Blacksmith", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Blacksmith", "nextPerkIncreaseLevel").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Blacksmith", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Blacksmith", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Blacksmith", "chance at perks").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Blacksmith", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some leftover material and made more!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getCrafted().getType().getName() + " 2");
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public boolean isScientistItem(ItemStack is) {
        if (is.getType().matches(ItemStack.of((ItemType) PixelmonItems.potion)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.superPotion)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.hyperPotion))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.fullRestore)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.maxPotion)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.parlyzHeal))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.antidote)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.burnHeal)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.iceHeal))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.iceHeal)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.revive)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.maxRevive))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.elixir)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.maxElixir)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.ether))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItems.maxEther))) {
            return true;
        }
        return false;
    }

    public boolean isCrafterItem (ItemStack is) {
        if (is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.beastBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.cherishBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.diveBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.dreamBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.duskBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.fastBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.friendBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.greatBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.gsBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.healBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.heavyBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.levelBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.loveBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.lureBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.luxuryBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.moonBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.nestBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.netBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.masterBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.parkBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.pokeBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.quickBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.repeatBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.premierBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.safariBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.sportBall)) || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.timerBall))
            || is.getType().matches(ItemStack.of((ItemType) PixelmonItemsPokeballs.ultraBall))) {
            return true;
        }
        return false;
    }

    public boolean isBlacksmithItem (ItemStack is) {
        if (is.getType().getName().contains("thunder") || is.getType().getName().contains("water") || is.getType().getName().contains("fire") || is.getType().getName().contains("moon") || is.getType().getName().contains("sun") || is.getType().getName().contains("dawn")
            || is.getType().getName().contains("dusk") || is.getType().getName().contains("amethsyt") || is.getType().getName().contains("crystal") || is.getType().getName().contains("aether") || is.getType().getName().contains("magma") || is.getType().getName().contains("aqua")
            || is.getType().getName().contains("plasma") || is.getType().getName().contains("galactic") || is.getType().getName().contains("rocket") || is.getType().getName().contains("leaf") || is.getType().getName().contains("ice") || is.getType().getName().contains("flare")
            || is.getType().getName().contains("skull") || is.getType().getName().contains("aluminum") || is.getType().getName().contains("thunder")) {
                if (is.getType() instanceof PixelmonItemsTools) {
                    return true;
                } else {
                    System.out.println("DEBUG BLACKSMITH");
                }

        }
        return false;
    }
}
