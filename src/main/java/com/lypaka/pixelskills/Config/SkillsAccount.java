package com.lypaka.pixelskills.Config;

import com.lypaka.pixelskills.PixelSkills;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.UUID;

public class SkillsAccount {
    public PixelSkills plugin;
    public SkillsAccountManager accountManager;
    public UUID uuid;
    public ConfigurationNode skillsAccountsConfig;

    public SkillsAccount (PixelSkills plugin, SkillsAccountManager accountManager, UUID uuid) {
        this.plugin = plugin;
        this.accountManager = accountManager;
        this.uuid = uuid;
        skillsAccountsConfig = accountManager.getAccountsConfig();
    }
}
