package com.lypaka.pixelskills;

import com.google.inject.Inject;
import com.lypaka.pixelskills.Commands.PixelSkillsCmd;
import com.lypaka.pixelskills.Skills.*;
import com.lypaka.pixelskills.config.ConfigManager;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Plugin(
        id = "pixelskills",
        name = "PixelSkills",
        version = "1.0.0",
        description = "A Pixelmon skills plugin",
        authors = {
                "Lypaka"
        }
)
public class PixelSkills {
    @Inject
    @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path defaultConfig;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    public PluginContainer container;


    @Inject
    public Logger logger;

    public ConfigurationNode config;

    public static PixelSkills INSTANCE;
    public SkillsAccountManager accountManager;

    @Listener
    public void onPreInit (GamePreInitializationEvent event) {
        try {
            config = loader.load();
            if (!defaultConfig.toFile().exists()) {
                config.getNode("Sets whether to use Pixelmon's currency (true) or a Sponge economy plugin's currency (false) for money rewards").setValue(false);
                loader.save(config);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        accountManager = new SkillsAccountManager(this);
        ConfigManager.setup(configDir);

        Pixelmon.EVENT_BUS.register(new Breeder(this));
        Pixelmon.EVENT_BUS.register(new Catcher(this));
        Pixelmon.EVENT_BUS.register(new Fisherman(this));
        Pixelmon.EVENT_BUS.register(new BossConqueror(this));
        Pixelmon.EVENT_BUS.register(new Botanist(this));
        Pixelmon.EVENT_BUS.register(new TreasureHunter(this));
        Pixelmon.EVENT_BUS.register(new Archaeologist(this));
        Pixelmon.EVENT_BUS.register(new FierceBattler(this));
        Sponge.getEventManager().registerListeners(this, new Crafter(this));
        MinecraftForge.EVENT_BUS.register(new Miner(this));

        Sponge.getCommandManager().register(this, PixelSkillsCmd.create(), "pixelskills");
    }

    @Listener
    public void onJoin (ClientConnectionEvent.Join event, @First Player player) {
        UUID uuid = player.getUniqueId();
        accountManager.createAccount(uuid);
    }

    @Listener
    public void onReload(GameReloadEvent e) {
        try {
            config = loader.load();
            reloadConfig();
            loader.save(config);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ConfigManager.load();
        System.out.println("PixelSkills config has reloaded!");
    }

    public void reloadConfig() {
        try {
            config.getNode("Sets whether to use Pixelmon's currency (true) or a Sponge economy plugin's currency (false) for money rewards").setValue(config.getNode("Sets whether to use Pixelmon's currency (true) or a Sponge economy plugin's currency (false) for money rewards").getValue());
            loader.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public SkillsAccountManager getAccountManager() {
        return accountManager;
    }

    public ConfigurationNode getConfigNode() {
        return config;
    }

    public PixelSkills() {
        INSTANCE = this;
    }

    public static Logger getLogger() {
        return INSTANCE.logger;
    }

    public Path getConfigDir() {
        return configDir;
    }

    public static PluginContainer getContainer() {
        return INSTANCE.container;
    }

    public void addPoints(String skill, int points, Player player) {
        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() + points);
        accountManager.saveConfig();
    }

    public boolean didLevelUp(String skill, Player player) {
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() >= (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").getInt())) {
            return true;
        } else {
            return false;
        }
    }
    public void levelUp(String skill, Player player) {
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() > (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").getInt())) {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() - accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").getInt());
            accountManager.saveConfig();
        } else {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").setValue(0);
            accountManager.saveConfig();
        }
        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() * ConfigManager.getConfigNode("Skills", skill, "EXP", "EXP needed increase interval (how much EXP to level up goes up on each level (if amount needed = 1, plugin will +1 to it), multiplies by:").getInt());
        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + 1);
        setNextPerkIncreaseLevel(skill, player);
        accountManager.saveConfig();
        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You leveled up your " + skill + " skill to level " + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + "!"));
        if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesPossibleExtraRewards").getValue().equals(true)) {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesRewards", "levelInterval (every <level> level)").getInt() || accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() % ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesRewards", "levelInterval (every <level> level)").getInt() == 0) {
                Random rand = new Random();
                int RNG = rand.nextInt(ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesRewards", "chance to give rewards at interval (1/<number>) (1/100 = 1% chance)").getInt() - 1) + 1;
                if (RNG == 1) {
                    int prize = rand.nextInt(ConfigManager.getConfigNode("Skills", skill, "Rewards", "possibleRewards", "numberOf").getInt() - 1) + 1;
                    if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "numberOf", "increases by", "number").getValue() != null) {
                        if (Objects.equals(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "type (item/Pokemon/money)").getString(), "item")) {
                            if (Objects.equals(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "numberOf", "increases by", "modifier (add/multiply/false) (if false, sets number to give at each level)").getValue(), false)) {
                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "prize").getString() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "numberOf", "increases by", "number").getInt());
                            } else if (Objects.equals(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "numberOf", "increases by", "modifier (add/multiply/false) (if false, sets number to give at each level)").getValue(), "multiply")) {
                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "prize").getString() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "numberOf", "increases by", "number").getInt() * accountManager.getAccountsConfig().getNode("Skills", skill, "Level").getInt());
                            } else if (Objects.equals(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "numberOf", "increases by", "modifier (add/multiply/false) (if false, sets number to give at each level)").getValue(), "add")) {
                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "prize").getString() + " " + (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", prize, "reward", "numberOf", "increases by", "number").getInt() + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt()));
                            }
                        }
                    }
                }
            }
        }
        if (didLevelUp(skill, player)) {
            levelUp(skill, player);
        }
    }

    public void setNextPerkIncreaseLevel (String skill, Player player) {
        //"Skills", "Breeder", "nextPerkIncreaseLevel"
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "perk", "starts at level").getInt()) {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "nextPerkIncreaseLevel").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt());
            accountManager.saveConfig();
            if (Objects.equals(ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance gets higher as level gets higher").getString(), "true")) {
                int chance = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt();
                String mod = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "modifier mode (add/multiply").getString();
                int num = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "modifier number").getInt();
                if (Objects.equals(mod, "add")) {
                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue(chance + num);
                    accountManager.saveConfig();
                } else if (Objects.equals(mod, "multiply")) {
                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue(chance * num);
                    accountManager.saveConfig();
                }
            }
        } else {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "nextPerkIncreaseLevel").getInt()) {
                accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "nextPerkIncreaseLevel").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt());
                accountManager.saveConfig();
                if (Objects.equals(ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance gets higher as level gets higher").getString(), "true")) {
                    int chance = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt();
                    String mod = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "modifier mode (add/multiply").getString();
                    int num = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "modifier number").getInt();
                    if (Objects.equals(mod, "add")) {
                        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue(chance + num);
                        accountManager.saveConfig();
                    } else if (Objects.equals(mod, "multiply")) {
                        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue(chance * num);
                        accountManager.saveConfig();
                    }
                }
            }
        }
    }

}
