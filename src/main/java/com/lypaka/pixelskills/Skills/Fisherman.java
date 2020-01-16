package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.Config.ConfigManager;
import com.lypaka.pixelskills.Config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.FishingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Fisherman {

    public Fisherman (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onFish (FishingEvent.Catch e) {
        Player player = (Player) e.player;
        if (ConfigManager.getConfigNode("Skills", "Fisherman", "isEnabled").getValue().equals(true)) {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fisherman", "Level").getInt() < ConfigManager.getConfigNode("Skills", "Fisherman", "maxLevel").getInt()) {
                //I'm not sure why they would have Fisherman enabled but have the only task for it disabled, but here we go
                if (ConfigManager.getConfigNode("Skills", "Fisherman", "EXP", "Tasks", "Successful captures", "isEnabled").getValue().equals(true)) {
                    int exp = ConfigManager.getConfigNode("Skills", "Fisherman", "EXP", "Tasks", "Successful captures", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Fisherman", "EXP", "expModifier").getInt();
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Fisherman EXP points!"));
                    plugin.addPoints("Fisherman", exp, player);
                    if (plugin.didLevelUp("Fisherman", player)) {
                        plugin.levelUp("Fisherman", player);
                        if (ConfigManager.getConfigNode("Skills", "Fisherman", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fisherman", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Fisherman", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fisherman", "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fisherman", "nextPerkIncreaseLevel").getInt()) {
                                if (ConfigManager.getConfigNode("Skills", "Fisherman", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt() != 0) {
                                    if (ConfigManager.getConfigNode("Skills", "Fisherman", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                                        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fisherman", "chance at perks").getInt() != 0) {
                                            int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fisherman", "chance at perks").getInt();
                                            if (PixelSkills.getRandom().nextInt(100) < number) {
                                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " A Poke Ball got fished up with the Pokemon!"));
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + "pixelmon:poke_ball 1");
                                            }
                                        }
                                    } else {
                                        int number = ConfigManager.getConfigNode("Skills", "Fisherman", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>)").getInt();
                                        if (PixelSkills.getRandom().nextInt(100) < number) {
                                            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " A Poke Ball got fished up with the Pokemon!"));
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + "pixelmon:poke_ball 1");
                                        }
                                    }
                                } else {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " A Poke Ball got fished up with the Pokemon!"));
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + "pixelmon:poke_ball 1");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
