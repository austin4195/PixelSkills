package com.lypaka.pixelskills.Config;

import com.lypaka.pixelskills.PixelSkills;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SkillsAccountManager {

    public Logger logger;
    public File accountsFile;
    public ConfigurationLoader<CommentedConfigurationNode> loader;
    public ConfigurationNode accountConfig;
    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    public SkillsAccountManager (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
        setupAccountConfig();
    }

    private void setupAccountConfig() {
        accountsFile = new File(plugin.getConfigDir().toFile(), "accounts.conf");
        loader = HoconConfigurationLoader.builder().setFile(accountsFile).build();

        try {
            accountConfig = loader.load();
            if (!accountsFile.exists()) {
                accountConfig.getNode("placeholder").setValue(true);
                loader.save(accountConfig);
            }
        } catch (IOException e) {
            logger.warn("Error setting up account configuration!");
        }
    }

    public void createAccount(UUID uuid) {
        if (!hasAccount(uuid)) {
            accountConfig.getNode(uuid.toString(), "Skills", "Breeder", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Breeder", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Breeder", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Breeder", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Breeder", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Breeder", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Catcher", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Catcher", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Catcher", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Catcher", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Catcher", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Catcher", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Crafter", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Crafter", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Crafter", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Crafter", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Crafter", "nextPerkIncreaseLevel").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Fierce Battler", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Fierce Battler", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Fierce Battler", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Fierce Battler", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Fierce Battler", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Fierce Battler", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Fisherman", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Fisherman", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Fisherman", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Fisherman", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Fisherman", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Fisherman", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Shiny Hunter", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Shiny Hunter", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Shiny Hunter", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Shiny Hunter", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Shiny Hunter", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Shiny Hunter", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Legendary Master", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Legendary Master", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Legendary Master", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Legendary Master", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Legendary Master", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Legendary Master", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Scientist", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Scientist", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Scientist", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Scientist", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Scientist", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Scientist", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Botanist", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Botanist", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Botanist", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Botanist", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Botanist", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Botanist", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Miner", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Miner", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Miner", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Miner", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Miner", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Miner", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Archaeologist", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Archaeologist", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Archaeologist", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Archaeologist", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Archaeologist", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Archaeologist", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Blacksmith", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Blacksmith", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Blacksmith", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Blacksmith", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Blacksmith", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Blacksmith", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Treasure Hunter", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Treasure Hunter", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Treasure Hunter", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Treasure Hunter", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Treasure Hunter", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Treasure Hunter", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Boss Conqueror", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Boss Conqueror", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Boss Conqueror", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Boss Conqueror", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Boss Conqueror", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Boss Conqueror", "chance at perks").setValue(0);

            accountConfig.getNode(uuid.toString(), "Skills", "Poke Exterminator", "Level").setValue(1);
            accountConfig.getNode(uuid.toString(), "Skills", "Poke Exterminator", "EXP").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Poke Exterminator", "EXP-to-Levelup").setValue(ConfigManager.getConfigNode("Skills", "Poke Exterminator", "EXP", "amount of EXP needed to level up to level 2").getInt());
            accountConfig.getNode(uuid.toString(), "Skills", "Poke Exterminator", "nextPerkIncreaseLevel").setValue(0);
            accountConfig.getNode(uuid.toString(), "Skills", "Poke Exterminator", "chance at perks").setValue(0);
            try {
                loader.save(accountConfig);
            } catch (IOException e) {
                logger.warn("Error creating new account!");

            }
        }
    }

    public boolean hasAccount(UUID uuid) {
        //if (plugin.getConfigDir().toFile(), uuid + ".conf" !=null) {
        //if (plugin.getConfigDir().toFile().getuuid() != null && plugin.getConfigDir().toFile().getuuid() == uuid + ".conf") {
        if (accountConfig.getNode(uuid.toString().toString()).getValue() != null) {
            return true;
        }

        return false;
    }

    public void saveConfig() {
        try {
            loader.save(accountConfig);
        } catch (IOException e) {
            logger.warn("Error saving the accounts configuration!");

        }

    }

    public ConfigurationNode getAccountsConfig() {
        return accountConfig;

    }




}
