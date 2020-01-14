package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.config.ConfigManager;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.PokeLootClaimedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Random;

public class TreasureHunter {
    public TreasureHunter (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onPokeLootOpen (PokeLootClaimedEvent e) {
        Player player = (Player) e.player;
        if (ConfigManager.getConfigNode("Skills", "Treasure Hunter", "isEnabled").getValue().equals(true)) {
            if (ConfigManager.getConfigNode("Skills", "Treasure Hunter", "EXP", "Tasks", "Opening Poke Loot chests", "isEnabled").getValue().equals(true)) {
                int exp = ConfigManager.getConfigNode("Skills", "Treasure Hunter", "EXP", "Tasks", "Opening Poke Loot chests", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Treasure Hunter", "EXP", "expModifier").getInt();
                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Treasure Hunter EXP points!"));
                plugin.addPoints("Treasure Hunter", exp, player);
                if (plugin.didLevelUp("Treasure Hunter", player)) {
                    plugin.levelUp("Treasure Hunter", player);
                    if (ConfigManager.getConfigNode("Skills", "Treasure Hunter", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Treasure Hunter", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Treasure Hunter", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Treasure Hunter", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Treasure Hunter", "nextPerkIncreaseLevel").getInt()) {
                            if (ConfigManager.getConfigNode("Skills", "Treasure Hunter", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                if (ConfigManager.getConfigNode("Skills", "Treasure Hunter", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                    if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Treasure Hunter", "chance at perks").getInt() != 0) {
                                        int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Treasure Hunter", "chance at perks").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The chest contained another chest!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.chest.toString() + " 1");
                                        }
                                    }
                                } else {
                                    int number = ConfigManager.getConfigNode("Skills", "Treasure Hunter", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                    if (PixelSkills.getRandom().nextInt(100) < number) {
                                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The chest contained another chest!"));
                                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.chest.toString() + " 1");
                                    }
                                }
                            } else {
                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The chest contained another chest!"));
                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + e.chest.toString() + " 1");
                            }
                        }
                    }
                }
            }
        }
    }
}
