package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.config.ConfigManager;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.util.Random;

public class Archaeologist {
    public Archaeologist (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onFossilRevive (PixelmonReceivedEvent e) throws IOException {
        if (e.receiveType.toString().contains("Fossil")) {
            if (ConfigManager.getConfigNode("Skills", "Archaeologist", "isEnabled").getValue().equals(true)) {
                if (ConfigManager.getConfigNode("Skills", "Archaeologist", "EXP", "Tasks", "Reviving fossils", "isEnabled").getValue().equals(true)) {
                    EntityPlayerMP forgePlayer = e.player;
                    Player player = (Player) forgePlayer;
                    int exp = ConfigManager.getConfigNode("Skills", "Archaeologist", "EXP", "Tasks", "Reviving fossils", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Archaeologist", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Archaeologist EXP points!"));
                    plugin.addPoints("Archaeologist", exp, player);
                    if (plugin.didLevelUp("Archaeologist", player)) {
                        plugin.levelUp("Archaeologist", player);
                        if (ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt()) {
                                Random rand = new Random();
                                if (ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                    int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "chance at perks").getInt();
                                    if (number != 0) {
                                        int rng = rand.nextInt(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "chance at perks").getInt() - 1) + 1;
                                        if (rng == 1) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                            if (e.pokemon.stats.IVs.Attack != 31) {
                                                e.pokemon.stats.IVs.Attack = ((e.pokemon.stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.Attack;
                                            }
                                            if (e.pokemon.stats.IVs.Defence != 31) {
                                                e.pokemon.stats.IVs.Defence = ((e.pokemon.stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.Defence;
                                            }
                                            if (e.pokemon.stats.IVs.SpAtt != 31) {
                                                e.pokemon.stats.IVs.SpAtt = ((e.pokemon.stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.SpAtt;
                                            }
                                            if (e.pokemon.stats.IVs.SpDef != 31) {
                                                e.pokemon.stats.IVs.SpDef = ((e.pokemon.stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.SpDef;
                                            }
                                            if (e.pokemon.stats.IVs.Speed != 31) {
                                                e.pokemon.stats.IVs.Speed = ((e.pokemon.stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.Speed;
                                            }
                                            if (e.pokemon.stats.IVs.HP != 31) {
                                                e.pokemon.stats.IVs.HP = ((e.pokemon.stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.HP;
                                            }
                                            e.pokemon.updateStats();
                                        }
                                    }
                                } else {
                                    int rng = rand.nextInt(ConfigManager.getConfigNode("Skills", "Archaeologist", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt() - 1) + 1;
                                    if (rng == 1) {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                        if (e.pokemon.stats.IVs.Attack != 31) {
                                            e.pokemon.stats.IVs.Attack = ((e.pokemon.stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.Attack;
                                        }
                                        if (e.pokemon.stats.IVs.Defence != 31) {
                                            e.pokemon.stats.IVs.Defence = ((e.pokemon.stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.Defence;
                                        }
                                        if (e.pokemon.stats.IVs.SpAtt != 31) {
                                            e.pokemon.stats.IVs.SpAtt = ((e.pokemon.stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.SpAtt;
                                        }
                                        if (e.pokemon.stats.IVs.SpDef != 31) {
                                            e.pokemon.stats.IVs.SpDef = ((e.pokemon.stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.SpDef;
                                        }
                                        if (e.pokemon.stats.IVs.Speed != 31) {
                                            e.pokemon.stats.IVs.Speed = ((e.pokemon.stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.Speed;
                                        }
                                        if (e.pokemon.stats.IVs.HP != 31) {
                                            e.pokemon.stats.IVs.HP = ((e.pokemon.stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Archaeologist", "Level").getInt() / 2)) + e.pokemon.stats.IVs.HP;
                                        }
                                        e.pokemon.updateStats();
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
