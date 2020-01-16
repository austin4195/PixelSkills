package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.Config.ConfigManager;
import com.lypaka.pixelskills.Config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.api.events.BerryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Botanist {
    public Botanist (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onApricornPick (ApricornEvent.PickApricorn e) {
        Player player = (Player) e.player;
        if (ConfigManager.getConfigNode("Skills", "Botanist", "EXP", "Tasks", "Picking apricorns", "isEnabled").getValue().equals(true)) {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "Level").getInt() < ConfigManager.getConfigNode("Skills", "Botanist", "maxLevel").getInt()) {
                int exp = ConfigManager.getConfigNode("Skills", "Botanist", "EXP", "Tasks", "Picking apricorns", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Botanist", "EXP", "expModifier").getInt();
                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Botanist EXP points!"));
                plugin.addPoints("Botanist", exp, player);
                if (plugin.didLevelUp("Botanist", player)) {
                    plugin.levelUp("Botanist", player);
                    if (ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "nextPerkIncreaseLevel").getInt()) {
                            if (ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                if (ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                    int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "chance at perks").getInt();
                                    if (number != 0) {
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some hidden Apricorns!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.apricorn.name() + " 2");
                                        }
                                    }
                                } else {
                                    int number = ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                    if (PixelSkills.getRandom().nextInt(100) < number) {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some hidden Apricorns!"));
                                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.apricorn.name() + " 2");
                                    }
                                }
                            } else {
                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some hidden Apricorns!"));
                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.apricorn.name() + " 2");
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBerryPick (BerryEvent.PickBerry e) {
        Player player = (Player) e.player;
        if (ConfigManager.getConfigNode("Skills", "Botanist", "isEnabled").getValue().equals(true)) {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "Level").getInt() < ConfigManager.getConfigNode("Skills", "Botanist", "maxLevel").getInt()) {
                if (ConfigManager.getConfigNode("Skills", "Botanist", "EXP", "Tasks", "Picking berries", "isEnabled").getValue().equals(true)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Botanist", "EXP", "Tasks", "Picking berries", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Botanist", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Botanist EXP points!"));
                    plugin.addPoints("Botanist", exp, player);
                    if (plugin.didLevelUp("Botanist", player)) {
                        plugin.levelUp("Botanist", player);
                        if (ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "Level").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Botanist", "chance at perks").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some hidden berries!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.berry.name() + " 2");
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Botanist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some hidden berries!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.berry.name() + " 2");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You found some hidden berries!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.berry.name() + " 2");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
