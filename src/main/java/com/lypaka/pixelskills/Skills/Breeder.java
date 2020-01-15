package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.Config.ConfigManager;
import com.lypaka.pixelskills.Config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.events.player.PlayerEggHatchEvent;
import com.pixelmonmod.pixelmon.api.events.player.PlayerEggStepsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.util.UUID;

public class Breeder {
    public Breeder (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onEggGet (BreedEvent.MakeEgg e) throws IOException {
        if (ConfigManager.getConfigNode("Skills", "Breeder", "isEnabled").getValue().equals(true)) {
            UUID uuid = e.owner;
            Player player = Sponge.getServer().getPlayer(uuid).get();
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() < ConfigManager.getConfigNode("Skills", "Breeder", "maxLevel").getInt()) {
                if (ConfigManager.getConfigNode("Skills", "Breeder", "EXP", "Tasks", "Making eggs", "isEnabled").getValue().equals(true)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Breeder", "EXP", "Tasks", "Making eggs", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Breeder", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Breeder EXP points!"));
                    plugin.addPoints("Breeder", exp, player);
                    if (plugin.didLevelUp("Breeder", player)) {
                        plugin.levelUp("Breeder", player);
                        if (ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "nextPerkIncreaseLevel").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "chance at perks").getInt() != 0) {
                                            int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "chance at perks").getInt();
                                            if (PixelSkills.getRandom().nextInt(100) < number) {
                                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                                if (e.getEgg().stats.IVs.Attack != 31) {
                                                    e.getEgg().stats.IVs.Attack = ((e.getEgg().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Attack;
                                                }
                                                if (e.getEgg().stats.IVs.Defence != 31) {
                                                    e.getEgg().stats.IVs.Defence = ((e.getEgg().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Defence;
                                                }
                                                if (e.getEgg().stats.IVs.SpAtt != 31) {
                                                    e.getEgg().stats.IVs.SpAtt = ((e.getEgg().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.SpAtt;
                                                }
                                                if (e.getEgg().stats.IVs.SpDef != 31) {
                                                    e.getEgg().stats.IVs.SpDef = ((e.getEgg().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.SpDef;
                                                }
                                                if (e.getEgg().stats.IVs.Speed != 31) {
                                                    e.getEgg().stats.IVs.Speed = ((e.getEgg().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Speed;
                                                }
                                                if (e.getEgg().stats.IVs.HP != 31) {
                                                    e.getEgg().stats.IVs.HP = ((e.getEgg().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.HP;
                                                }
                                                e.getEgg().updateStats();
                                            }
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                            if (e.getEgg().stats.IVs.Attack != 31) {
                                                e.getEgg().stats.IVs.Attack = ((e.getEgg().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Attack;
                                            }
                                            if (e.getEgg().stats.IVs.Defence != 31) {
                                                e.getEgg().stats.IVs.Defence = ((e.getEgg().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Defence;
                                            }
                                            if (e.getEgg().stats.IVs.SpAtt != 31) {
                                                e.getEgg().stats.IVs.SpAtt = ((e.getEgg().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.SpAtt;
                                            }
                                            if (e.getEgg().stats.IVs.SpDef != 31) {
                                                e.getEgg().stats.IVs.SpDef = ((e.getEgg().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.SpDef;
                                            }
                                            if (e.getEgg().stats.IVs.Speed != 31) {
                                                e.getEgg().stats.IVs.Speed = ((e.getEgg().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Speed;
                                            }
                                            if (e.getEgg().stats.IVs.HP != 31) {
                                                e.getEgg().stats.IVs.HP = ((e.getEgg().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.HP;
                                            }
                                            e.getEgg().updateStats();
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                    if (e.getEgg().stats.IVs.Attack != 31) {
                                        e.getEgg().stats.IVs.Attack = ((e.getEgg().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Attack;
                                    }
                                    if (e.getEgg().stats.IVs.Defence != 31) {
                                        e.getEgg().stats.IVs.Defence = ((e.getEgg().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Defence;
                                    }
                                    if (e.getEgg().stats.IVs.SpAtt != 31) {
                                        e.getEgg().stats.IVs.SpAtt = ((e.getEgg().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.SpAtt;
                                    }
                                    if (e.getEgg().stats.IVs.SpDef != 31) {
                                        e.getEgg().stats.IVs.SpDef = ((e.getEgg().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.SpDef;
                                    }
                                    if (e.getEgg().stats.IVs.Speed != 31) {
                                        e.getEgg().stats.IVs.Speed = ((e.getEgg().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.Speed;
                                    }
                                    if (e.getEgg().stats.IVs.HP != 31) {
                                        e.getEgg().stats.IVs.HP = ((e.getEgg().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() / 2)) + e.getEgg().stats.IVs.HP;
                                    }
                                    e.getEgg().updateStats();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEggHatch (PlayerEggHatchEvent e) {
        if (ConfigManager.getConfigNode("Skills", "Breeder", "isEnabled").getValue().equals(true)) {
            Player player = (Player) e.getPlayer();
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() < ConfigManager.getConfigNode("Skills", "Breeder", "maxLevel").getInt()) {
                if (ConfigManager.getConfigNode("Skills", "Breeder", "EXP", "Tasks", "Hatching eggs", "isEnabled").getValue().equals(true)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Breeder", "EXP", "Tasks", "Hatching eggs", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Breeder", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Breeder EXP points!"));
                    plugin.addPoints("Breeder", exp, player);
                    if (plugin.didLevelUp("Breeder", player)) {
                        plugin.levelUp("Breeder", player);
                    }
                }
            }
        }
    }
    //

    @SubscribeEvent
    public void onEggHatching (PlayerEggStepsEvent e) {
        if (ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
            Player player = (Player) e.getPlayer();
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "nextPerkIncreaseLevel").getInt()) {
                if (ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                    if (ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "chance at perks").getInt() != 0) {
                            int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "chance at perks").getInt();
                            if (PixelSkills.getRandom().nextInt(100) < number) {
                                double mod = (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Breeder", "Level").getInt() * 0.01);
                                double newSteps = mod * e.getStepsRequired();
                                e.setStepsRequired((int) (e.getStepsRequired() - newSteps));
                            }
                        }

                    } else {
                        int number = ConfigManager.getConfigNode("Skills", "Breeder", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                        if (PixelSkills.getRandom().nextInt(100) < number) {
                            double newSteps = e.getStepsRequired() * 0.25;
                            e.setStepsRequired((int) (e.getStepsRequired() - newSteps));
                        }
                    }
                } else {
                    double newSteps = e.getStepsRequired() * 0.25;
                    e.setStepsRequired((int) (e.getStepsRequired() - newSteps));
                }
            }
        }
    }
}
