package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.config.ConfigManager;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Random;

public class Miner {
    /**
     *
     * Miner and Archaeologist's mining task are handled in this class, as they fire off the same event
     *
     */
    public Miner (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onMine (BlockEvent.BreakEvent e) {
        if (isPixelmonOre(e.getState().getBlock().toString())) {
            if (ConfigManager.getConfigNode("Skills", "Miner", "isEnabled").getValue().equals(true)) {
                if (ConfigManager.getConfigNode("Skills", "Miner", "EXP", "Tasks", "Mining Pixelmon ores", "isEnabled").getValue().equals(true)) {
                    EntityPlayer forgePlayer = e.getPlayer();
                    Player player = (Player) forgePlayer;
                    int exp = ConfigManager.getConfigNode("Skills", "Miner", "EXP", "Tasks", "Mining Pixelmon ores", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Miner", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Miner EXP points!"));
                    plugin.addPoints("Miner", exp, player);
                    if (plugin.didLevelUp("Miner", player)) {
                        plugin.levelUp("Miner", player);
                        if (ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "nextPerkIncreaseLevel").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                    if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "chance at perks").getInt() != 0) {
                                        int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "chance at perks").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a thick chunk of ore! You found two more pieces!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getState().getBlock().getLocalizedName() + " 3");
                                        }
                                    }
                                } else {
                                    int number = ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt();
                                    if (PixelSkills.getRandom().nextInt(100) < number) {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a thick chunk of ore! You found two more pieces!"));
                                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getState().getBlock().getLocalizedName() + " 3");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (isVanillaOre(e.getState().getBlock().toString())) {
                if (ConfigManager.getConfigNode("Skills", "Miner", "isEnabled").getValue().equals(true)) {
                    if (ConfigManager.getConfigNode("Skills", "Miner", "EXP", "Tasks", "Mining vanilla ores", "isEnabled").getValue().equals(true)) {
                        EntityPlayer forgePlayer = e.getPlayer();
                        Player player = (Player) forgePlayer;
                        int exp = ConfigManager.getConfigNode("Skills", "Miner", "EXP", "Tasks", "Mining vanilla ores", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Miner", "EXP", "expModifier").getInt();
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Miner EXP points!"));
                        plugin.addPoints("Miner", exp, player);
                        if (plugin.didLevelUp("Miner", player)) {
                            plugin.levelUp("Miner", player);
                            if (ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                                if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "nextPerkIncreaseLevel").getInt()) {
                                    if (ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                        if (ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "chance at perks").getInt() != 0) {
                                                int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Miner", "chance at perks").getInt();
                                                if (PixelSkills.getRandom().nextInt(100) < number) {
                                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a thick chunk of ore! You found two more pieces!"));
                                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getState().getBlock().toString().replace("{", "").replace("{", "").replace("block", "") + " 2");
                                                }
                                            }
                                        } else {
                                            int number = ConfigManager.getConfigNode("Skills", "Miner", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                            if (PixelSkills.getRandom().nextInt(100) < number) {
                                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a thick chunk of ore! You found two more pieces!"));
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getState().getBlock().toString().replace("{", "").replace("{", "").replace("block", "") + " 2");
                                            }
                                        }
                                    } else {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a thick chunk of ore! You found two more pieces!"));
                                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.getState().getBlock().toString().replace("{", "").replace("{", "").replace("block", "") + " 2");
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (e.getState().getBlock().toString().contains("pixelmon:fossil")) {
                    if (ConfigManager.getConfigNode("Skills", "Archaeologist", "isEnabled").getValue().equals(true)) {
                        if (ConfigManager.getConfigNode("Skills", "Archaeologist", "EXP", "Tasks", "Mining fossils", "isEnabled").getValue().equals(true)) {
                            EntityPlayer forgePlayer = e.getPlayer();
                            Player player = (Player) forgePlayer;
                            int exp = ConfigManager.getConfigNode("Skills", "Archaeologist", "EXP", "Tasks", "Mining fossils", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Archaeologist", "EXP", "expModifier").getInt();
                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Archaeologist EXP points!"));
                            plugin.addPoints("Archaeologist", exp, player);
                            if (plugin.didLevelUp("Archaeologist", player)) {
                                plugin.levelUp("Archaeologist", player);
                                if (ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                                    if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "nextPerkIncreaseLevel").getInt()) {
                                        if (ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                            if (ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "chance gets higher as level gets higher").getBoolean()) {
                                                if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "chance at perks").getInt() != 0) {
                                                    int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "chance at perks").getInt();
                                                    if (PixelSkills.getRandom().nextInt(100) < number) {
                                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a big fossil! You found one more fossil!"));
                                                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:fossil 1");
                                                    }
                                                }
                                            } else {
                                                int number = ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                                if (PixelSkills.getRandom().nextInt(100) < number) {
                                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a big fossil! You found one more fossil!"));
                                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:fossil 1");
                                                }
                                            }
                                        } else {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " That was a big fossil! You found one more fossil!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:fossil 1");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isPixelmonOre (String name) {
        if (name.contains("thunder_stone_ore") || name.contains("leaf_stone_ore") || name.contains("water_stone_ore") || name.contains("fire_stone_ore") || name.contains("dawn_dusk_ore") || name.contains("bauxite_ore") || name.contains("sun_stone_ore") ||
            name.contains("ruby_ore") || name.contains("sapphire_ore") || name.contains("amethyst_ore") || name.contains("crystal_ore") || name.contains("silicon_ore") || name.contains("icestoneore")) {
            return true;
        }
        return false;
    }

    public boolean isVanillaOre (String name) {
        if (name.contains("emerald_ore") || name.contains("lapis_ore") || name.contains("diamond_ore") || name.contains("redstone_ore") || name.contains("gold_ore") || name.contains("iron_ore") ||
            name.contains("coal_ore") || name.contains("quartz_ore")) {
            return true;
        }
        return false;
    }
}
