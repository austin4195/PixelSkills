package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.Config.ConfigManager;
import com.lypaka.pixelskills.Config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BossConqueror {
    /**
     *
     * Boss Conqueror and Poke Exterminator are handled in this class, as they fire off the same event
     *
     */

    public BossConqueror (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onPokeKill (BeatWildPixelmonEvent e) {
        Player player = (Player) e.player;
        if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "isEnabled").getValue().equals(true)) {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "Level").getInt() < ConfigManager.getConfigNode("Skills", "Boss Conqueror", "maxLevel").getInt()) {
                if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "EXP", "Tasks", "Kill normal bosses", "isEnabled").getValue().equals(true) && !isMega(e.wpp.controlledPokemon.get(0).pokemon) && isBoss(e.wpp.controlledPokemon.get(0).pokemon)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Boss Conqueror", "EXP", "Tasks", "Kill normal bosses", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Boss Conqueror", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Boss Conqueror EXP points!"));
                    plugin.addPoints("Boss Conqueror", exp, player);
                    if (plugin.didLevelUp("Boss Conqueror", player)) {
                        plugin.levelUp("Boss Conqueror", player);
                        if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "nextPerkIncreaseLevel").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "chance at perks").getInt() != 0) {
                                            if (PixelSkills.getRandom().nextInt(100) < accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "chance at perks").getInt()) {
                                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                            }
                                        }
                                    } else {
                                        if (PixelSkills.getRandom().nextInt(100) < ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt()) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                }
                            }
                        }
                    }
                } else if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "EXP", "Tasks", "Kill Mega bosses", "isEnabled").getValue().equals(true) && isMega(e.wpp.controlledPokemon.get(0).pokemon) && isBoss(e.wpp.controlledPokemon.get(0).pokemon)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Boss Conqueror", "EXP", "Tasks", "Kill Mega bosses", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Boss Conqueror", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Boss Conqueror EXP points!"));
                    plugin.addPoints("Boss Conqueror", exp, player);
                    if (plugin.didLevelUp("Boss Conqueror", player)) {
                        plugin.levelUp("Boss Conqueror", player);
                        if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "Level").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "chance at perks").getInt() != 0) {
                                            int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "chance at perks").getInt();
                                            if (PixelSkills.getRandom().nextInt(100) < number) {
                                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                            }
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Boss Conqueror", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "isEnabled").getValue().equals(true)) {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Poke Exterminator", "Level").getInt() < ConfigManager.getConfigNode("Skills", "Poke Exterminator", "maxLevel").getInt()) {
                if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "EXP", "Tasks", "Killing normal Pokemon", "isEnabled").getValue().equals(true) && !isMega(e.wpp.controlledPokemon.get(0).pokemon) && !isBoss(e.wpp.controlledPokemon.get(0).pokemon)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Poke Exterminator", "EXP", "Tasks", "Killing normal Pokemon", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Poke Exterminator", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Poke Exterminator EXP points!"));
                    plugin.addPoints("Poke Exterminator", exp, player);
                    if (plugin.didLevelUp("Poke Exterminator", player)) {
                        plugin.levelUp("Poke Exterminator", player);
                        if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Poke Exterminator", "Level").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Poke Exterminator", "chance at perks").getInt() != 0) {
                                            int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Poke Exterminator", "chance at perks").getInt();
                                            if (PixelSkills.getRandom().nextInt(100) < number) {
                                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                            }
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The Pokemon dropped 3 extra Ultra Balls!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The Pokemon dropped 3 extra Ultra Balls!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                }
                            }
                        }
                    }
                } else if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "EXP", "Tasks", "Killing normal Pokemon", "isEnabled").getValue().equals(true) && !isMega(e.wpp.controlledPokemon.get(0).pokemon) && !isBoss(e.wpp.controlledPokemon.get(0).pokemon)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Poke Exterminator", "EXP", "Tasks", "Killing normal Pokemon", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Poke Exterminator", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Poke Exterminator EXP points!"));
                    plugin.addPoints("Poke Exterminator", exp, player);
                    if (plugin.didLevelUp("Poke Exterminator", player)) {
                        plugin.levelUp("Poke Exterminator", player);
                        if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Boss Conqueror", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Poke Exterminator", "Level").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Poke Exterminator", "chance at perks").getInt() != 0) {
                                            int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Poke Exterminator", "chance at perks").getInt();
                                            if (PixelSkills.getRandom().nextInt(100) < number) {
                                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The boss dropped 3 extra Ultra Balls!"));
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                            }
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Poke Exterminator", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The Pokemon dropped 3 extra Ultra Balls!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The Pokemon dropped 3 extra Ultra Balls!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " pixelmon:ultra_ball 3");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isMega (EntityPixelmon pokemon) {
        if (pokemon.isMega) {
            return true;
        }
        return false;
    }

    public boolean isBoss (EntityPixelmon pokemon) {
        if (pokemon.isBossPokemon()) {
            return true;
        }
        return false;
    }
}
