package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.config.ConfigManager;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Random;

public class Catcher {
    /**
    *
    * Catcher, Legendary Master, and Shiny Hunter are handled in this class, as they use the same event.
    *
    * */

    public Catcher (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onCatch (CaptureEvent.SuccessfulCapture e) {
        if (e.getPokemon().isShiny()) {
            if (ConfigManager.getConfigNode("Skills", "Shiny Hunter", "isEnabled").getValue().equals(true)) {
                //I'm not sure why they would have Shiny Hunting enabled but have the only task for it disabled, but here we go
                if (ConfigManager.getConfigNode("Skills", "Shiny Hunter", "EXP", "Tasks", "Catching shinies", "isEnabled").getValue().equals(true)) {
                    Player player = (Player) e.player;
                    int exp = ConfigManager.getConfigNode("Skills", "Shiny Hunter", "EXP", "Tasks", "Catching shinies", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Shiny Hunter", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Shiny Hunter EXP points!"));
                    plugin.addPoints("Shiny Hunter", exp, player);
                    if (plugin.didLevelUp("Shiny Hunter", player)) {
                        plugin.levelUp("Shiny Hunter", player);
                        if (ConfigManager.getConfigNode("Skills", "Shiny Hunter", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Shiny Hunter", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Shiny Hunter", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt()) {
                                Random rand = new Random();
                                if (ConfigManager.getConfigNode("Skills", "Shiny Hunter", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                    if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "chance at perks").getInt() != 0){
                                        int rng = rand.nextInt(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "chance at perks").getInt() - 1) + 1;
                                        if (rng == 1) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                            if (e.getPokemon().stats.IVs.Attack != 31) {
                                                e.getPokemon().stats.IVs.Attack = ((e.getPokemon().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Attack;
                                            }
                                            if (e.getPokemon().stats.IVs.Defence != 31) {
                                                e.getPokemon().stats.IVs.Defence = ((e.getPokemon().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Defence;
                                            }
                                            if (e.getPokemon().stats.IVs.SpAtt != 31) {
                                                e.getPokemon().stats.IVs.SpAtt = ((e.getPokemon().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpAtt;
                                            }
                                            if (e.getPokemon().stats.IVs.SpDef != 31) {
                                                e.getPokemon().stats.IVs.SpDef = ((e.getPokemon().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpDef;
                                            }
                                            if (e.getPokemon().stats.IVs.Speed != 31) {
                                                e.getPokemon().stats.IVs.Speed = ((e.getPokemon().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Speed;
                                            }
                                            if (e.getPokemon().stats.IVs.HP != 31) {
                                                e.getPokemon().stats.IVs.HP = ((e.getPokemon().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.HP;
                                            }
                                            e.getPokemon().updateStats();
                                        }
                                    }
                                } else {
                                    int rng = rand.nextInt(ConfigManager.getConfigNode("Skills", "Shiny Hunter", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt() - 1) + 1;
                                    if (rng == 1) {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                        if (e.getPokemon().stats.IVs.Attack != 31) {
                                            e.getPokemon().stats.IVs.Attack = ((e.getPokemon().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Attack;
                                        }
                                        if (e.getPokemon().stats.IVs.Defence != 31) {
                                            e.getPokemon().stats.IVs.Defence = ((e.getPokemon().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Defence;
                                        }
                                        if (e.getPokemon().stats.IVs.SpAtt != 31) {
                                            e.getPokemon().stats.IVs.SpAtt = ((e.getPokemon().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpAtt;
                                        }
                                        if (e.getPokemon().stats.IVs.SpDef != 31) {
                                            e.getPokemon().stats.IVs.SpDef = ((e.getPokemon().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpDef;
                                        }
                                        if (e.getPokemon().stats.IVs.Speed != 31) {
                                            e.getPokemon().stats.IVs.Speed = ((e.getPokemon().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Speed;
                                        }
                                        if (e.getPokemon().stats.IVs.HP != 31) {
                                            e.getPokemon().stats.IVs.HP = ((e.getPokemon().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.HP;
                                        }
                                        e.getPokemon().updateStats();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (isLegendary(e.getPokemon())) {
            if (ConfigManager.getConfigNode("Skills", "Legendary Master", "isEnabled").getValue().equals(true)) {
                //I'm not sure why they would have Legendary Hunter enabled but have the only task for it disabled, but here we go
                if (ConfigManager.getConfigNode("Skills", "Legendary Master", "EXP", "Tasks", "Catching legendaries", "isEnabled").getValue().equals(true)) {
                    Player player = (Player) e.player;
                    int exp = ConfigManager.getConfigNode("Skills", "Legendary Master", "EXP", "Tasks", "Catching legendaries", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Legendary Master", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Legendary Master EXP points!"));
                    plugin.addPoints("Legendary Master", exp, player);
                    if (plugin.didLevelUp("Legendary Master", player)) {
                        plugin.levelUp("Legendary Master", player);
                        if (ConfigManager.getConfigNode("Skills", "Legendary Master", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Legendary Master", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Legendary Master", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt()) {
                                Random rand = new Random();
                                if (ConfigManager.getConfigNode("Skills", "Legendary Master", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                    if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "chance at perks").getInt() != 0){
                                        int rng = rand.nextInt(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "chance at perks").getInt() - 1) + 1;
                                        if (rng == 1) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                            if (e.getPokemon().stats.IVs.Attack != 31) {
                                                e.getPokemon().stats.IVs.Attack = ((e.getPokemon().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Attack;
                                            }
                                            if (e.getPokemon().stats.IVs.Defence != 31) {
                                                e.getPokemon().stats.IVs.Defence = ((e.getPokemon().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Defence;
                                            }
                                            if (e.getPokemon().stats.IVs.SpAtt != 31) {
                                                e.getPokemon().stats.IVs.SpAtt = ((e.getPokemon().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpAtt;
                                            }
                                            if (e.getPokemon().stats.IVs.SpDef != 31) {
                                                e.getPokemon().stats.IVs.SpDef = ((e.getPokemon().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpDef;
                                            }
                                            if (e.getPokemon().stats.IVs.Speed != 31) {
                                                e.getPokemon().stats.IVs.Speed = ((e.getPokemon().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Speed;
                                            }
                                            if (e.getPokemon().stats.IVs.HP != 31) {
                                                e.getPokemon().stats.IVs.HP = ((e.getPokemon().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.HP;
                                            }
                                            e.getPokemon().updateStats();
                                        }
                                    }
                                } else {
                                    int rng = rand.nextInt(ConfigManager.getConfigNode("Skills", "Legendary Master", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt() - 1) + 1;
                                    if (rng == 1) {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                        if (e.getPokemon().stats.IVs.Attack != 31) {
                                            e.getPokemon().stats.IVs.Attack = ((e.getPokemon().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Attack;
                                        }
                                        if (e.getPokemon().stats.IVs.Defence != 31) {
                                            e.getPokemon().stats.IVs.Defence = ((e.getPokemon().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Defence;
                                        }
                                        if (e.getPokemon().stats.IVs.SpAtt != 31) {
                                            e.getPokemon().stats.IVs.SpAtt = ((e.getPokemon().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpAtt;
                                        }
                                        if (e.getPokemon().stats.IVs.SpDef != 31) {
                                            e.getPokemon().stats.IVs.SpDef = ((e.getPokemon().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpDef;
                                        }
                                        if (e.getPokemon().stats.IVs.Speed != 31) {
                                            e.getPokemon().stats.IVs.Speed = ((e.getPokemon().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Speed;
                                        }
                                        if (e.getPokemon().stats.IVs.HP != 31) {
                                            e.getPokemon().stats.IVs.HP = ((e.getPokemon().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Legendary Master", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.HP;
                                        }
                                        e.getPokemon().updateStats();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (!e.getPokemon().isShiny() && !isLegendary(e.getPokemon())) {
            if (ConfigManager.getConfigNode("Skills", "Catcher", "isEnabled").getValue().equals(true)) {
                //I'm not sure why they would have Catcher enabled but have the only task for it disabled, but here we go
                if (ConfigManager.getConfigNode("Skills", "Catcher", "EXP", "Tasks", "Catching normal Pokemon", "isEnabled").getValue().equals(true)) {
                    Player player = (Player) e.player;
                    int exp = ConfigManager.getConfigNode("Skills", "Catcher", "EXP", "Tasks", "Catching normal Pokemon", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Catcher", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Catcher EXP points!"));
                    plugin.addPoints("Catcher", exp, player);
                    if (plugin.didLevelUp("Catcher", player)) {
                        plugin.levelUp("Catcher", player);
                        if (ConfigManager.getConfigNode("Skills", "Catcher", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Catcher", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Catcher", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt()) {
                                Random rand = new Random();
                                if (ConfigManager.getConfigNode("Skills", "Catcher", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                    if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "chance at perks").getInt() != 0){
                                        int rng = rand.nextInt(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "chance at perks").getInt() - 1) + 1;
                                        if (rng == 1) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                            if (e.getPokemon().stats.IVs.Attack != 31) {
                                                e.getPokemon().stats.IVs.Attack = ((e.getPokemon().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Attack;
                                            }
                                            if (e.getPokemon().stats.IVs.Defence != 31) {
                                                e.getPokemon().stats.IVs.Defence = ((e.getPokemon().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Defence;
                                            }
                                            if (e.getPokemon().stats.IVs.SpAtt != 31) {
                                                e.getPokemon().stats.IVs.SpAtt = ((e.getPokemon().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpAtt;
                                            }
                                            if (e.getPokemon().stats.IVs.SpDef != 31) {
                                                e.getPokemon().stats.IVs.SpDef = ((e.getPokemon().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpDef;
                                            }
                                            if (e.getPokemon().stats.IVs.Speed != 31) {
                                                e.getPokemon().stats.IVs.Speed = ((e.getPokemon().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Speed;
                                            }
                                            if (e.getPokemon().stats.IVs.HP != 31) {
                                                e.getPokemon().stats.IVs.HP = ((e.getPokemon().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Shiny Hunter", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.HP;
                                            }
                                            e.getPokemon().updateStats();
                                        }
                                    }
                                } else {
                                    int rng = rand.nextInt(ConfigManager.getConfigNode("Skills", "Catcher", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt() - 1) + 1;
                                    if (rng == 1) {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your experience tells you this Pokemon will be a good one!"));
                                        if (e.getPokemon().stats.IVs.Attack != 31) {
                                            e.getPokemon().stats.IVs.Attack = ((e.getPokemon().stats.IVs.Attack / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Attack;
                                        }
                                        if (e.getPokemon().stats.IVs.Defence != 31) {
                                            e.getPokemon().stats.IVs.Defence = ((e.getPokemon().stats.IVs.Defence / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Defence;
                                        }
                                        if (e.getPokemon().stats.IVs.SpAtt != 31) {
                                            e.getPokemon().stats.IVs.SpAtt = ((e.getPokemon().stats.IVs.SpAtt / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpAtt;
                                        }
                                        if (e.getPokemon().stats.IVs.SpDef != 31) {
                                            e.getPokemon().stats.IVs.SpDef = ((e.getPokemon().stats.IVs.SpDef / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.SpDef;
                                        }
                                        if (e.getPokemon().stats.IVs.Speed != 31) {
                                            e.getPokemon().stats.IVs.Speed = ((e.getPokemon().stats.IVs.Speed / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.Speed;
                                        }
                                        if (e.getPokemon().stats.IVs.HP != 31) {
                                            e.getPokemon().stats.IVs.HP = ((e.getPokemon().stats.IVs.HP / 31) * (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Catcher", "Level").getInt() / 2)) + e.getPokemon().stats.IVs.HP;
                                        }
                                        e.getPokemon().updateStats();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isLegendary(EntityPixelmon pokemon) {

        if (pokemon.getPokemonName().contains("Mewtwo") || pokemon.getPokemonName().contains("Mew") || pokemon.getPokemonName().contains("HoOh") || pokemon.getPokemonName().contains("Ho-Oh") || pokemon.getPokemonName().contains("Lugia") ||
                pokemon.getPokemonName().contains("Entei") || pokemon.getPokemonName().contains("Suicune") || pokemon.getPokemonName().contains("Raikou") || pokemon.getPokemonName().contains("Celebi") || pokemon.getPokemonName().contains("Latios") ||
                pokemon.getPokemonName().contains("Latias") || pokemon.getPokemonName().contains("Latios") || pokemon.getPokemonName().contains("Groudon") || pokemon.getPokemonName().contains("Kyogre") || pokemon.getPokemonName().contains("Rayquaza") ||
                pokemon.getPokemonName().contains("Jirachi") || pokemon.getPokemonName().contains("Deoxys") || pokemon.getPokemonName().contains("Heatran") || pokemon.getPokemonName().contains("Moltres") || pokemon.getPokemonName().contains("Zapdos") ||
                pokemon.getPokemonName().contains("Articuno") || pokemon.getPokemonName().contains("Regirock") || pokemon.getPokemonName().contains("Regice") || pokemon.getPokemonName().contains("Registeel") || pokemon.getPokemonName().contains("Azelf") ||
                pokemon.getPokemonName().contains("Uxie") || pokemon.getPokemonName().contains("Mesprit") || pokemon.getPokemonName().contains("Dialga") || pokemon.getPokemonName().contains("Palkia") || pokemon.getPokemonName().contains("Giratina") ||
                pokemon.getPokemonName().contains("Cresselia") || pokemon.getPokemonName().contains("Regigigas") || pokemon.getPokemonName().contains("Darkrai") || pokemon.getPokemonName().contains("Shaymin") || pokemon.getPokemonName().contains("Arceus") ||
                pokemon.getPokemonName().contains("Manaphy") || pokemon.getPokemonName().contains("Victini") || pokemon.getPokemonName().contains("Keldeo") || pokemon.getPokemonName().contains("Terrakion") || pokemon.getPokemonName().contains("Virizion") ||
                pokemon.getPokemonName().contains("Colabion") || pokemon.getPokemonName().contains("Thundurus") || pokemon.getPokemonName().contains("Tornadus") || pokemon.getPokemonName().contains("Landorus") || pokemon.getPokemonName().contains("Reshiram") ||
                pokemon.getPokemonName().contains("Kyurem") || pokemon.getPokemonName().contains("Zekrom") || pokemon.getPokemonName().contains("Xerneas") || pokemon.getPokemonName().contains("Yveltal") || pokemon.getPokemonName().contains("Zygarde") ||
                pokemon.getPokemonName().contains("TypeNull") || pokemon.getPokemonName().contains("Type:Null") || pokemon.getPokemonName().contains("Silvally") || pokemon.getPokemonName().contains("TapuKoko") || pokemon.getPokemonName().contains("Tapu Koko") ||
                pokemon.getPokemonName().contains("TapuFini") || pokemon.getPokemonName().contains("Tapu Fini") || pokemon.getPokemonName().contains("TapuLele") || pokemon.getPokemonName().contains("Tapu Lele") || pokemon.getPokemonName().contains("TapuBulu") ||
                pokemon.getPokemonName().contains("Tapu Bulu") || pokemon.getPokemonName().contains("Cosmog") || pokemon.getPokemonName().contains("Cosmoem") || pokemon.getPokemonName().contains("Solgaleo") || pokemon.getPokemonName().contains("Lunala") ||
                pokemon.getPokemonName().contains("Necrozma") || pokemon.getPokemonName().contains("Meloetta") || pokemon.getPokemonName().contains("Genesect") || pokemon.getPokemonName().contains("Diancie") || pokemon.getPokemonName().contains("Hoopa") ||
                pokemon.getPokemonName().contains("Volcanion") || pokemon.getPokemonName().contains("Magearna") || pokemon.getPokemonName().contains("Marshadow") || pokemon.getPokemonName().contains("Zeraora") || pokemon.getPokemonName().contains("Zacian") ||
                pokemon.getPokemonName().contains("Zamazenta") || pokemon.getPokemonName().contains("Eternatus") || pokemon.getPokemonName().contains("Meltan") || pokemon.getPokemonName().contains("Melmetal")) {
            return true;
        }
        return false;
    }
}
