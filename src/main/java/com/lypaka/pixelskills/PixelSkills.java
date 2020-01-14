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
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
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
    private Path defaultConfig;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    private PluginContainer container;


    @Inject
    private Logger logger;

    private ConfigurationNode config;

    public static PixelSkills INSTANCE;
    private SkillsAccountManager accountManager;
    private static final Random random = new Random();


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
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() != ConfigManager.getConfigNode("Skills", skill, "maxLevel").getInt()) {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() + points);
            accountManager.saveConfig();
        }
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Next-Reward-Level").isVirtual()) {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Next-Reward-Level").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesRewards", "levelInterval (every <level> level)").getInt());
            accountManager.saveConfig();
        }

    }

    public boolean didLevelUp(String skill, Player player) {
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() >= (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").getInt())) {
            return true;
        } else {
            return false;
        }
    }
    public void levelUp(String skill, Player player) {

        //Moves EXP forward to the next level
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() > accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").getInt()) {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").getInt() - accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").getInt());
            accountManager.saveConfig();
        } else {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP").setValue(0);
            accountManager.saveConfig();
        }

        //Moves player's level up 1
        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + 1);
        accountManager.saveConfig();

        //Sets the required EXP for the next levelup
        accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() * ConfigManager.getConfigNode("Skills", skill, "EXP", "EXP needed increase interval (how much EXP to level up goes up on each level (if amount needed = 1, plugin will +1 to it), multiplies by:").getInt());
        accountManager.saveConfig();

        //Lets the player know they leveled up
        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " Your " + skill + " skill has increased to level " + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + "!"));

        //Perks
        if (ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "isEnabled").getBoolean()) {
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                    accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "nextPerkIncreaseLevel").getInt()) {
                int chance = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "modifier number").getInt();
                switch (ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "modifier mode (add/multiply)").getString()) {
                    case "add":
                        if (chance == 0) {
                            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue((chance + 1) + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").getInt());
                            accountManager.saveConfig();
                        } else {
                            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue(chance + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").getInt());
                            accountManager.saveConfig();
                        }
                        break;
                    case "multiply":
                        if (chance == 0) {
                            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue((chance + 1) * accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").getInt());
                            accountManager.saveConfig();
                        } else {
                            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").setValue(chance * accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "chance at perks").getInt());
                            accountManager.saveConfig();
                        }
                        break;
                }
                accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "nextPerkIncreaseLevel").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt());
                accountManager.saveConfig();
            }
        }

        //Rewards
        //Checks if rewards for the skill are enabled
        if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesPossibleExtraRewards").getBoolean()) {
            //Checks if player's current level is a level that rewards are given on
            if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Next-Reward-Level").getInt()) {
                //Sets the new Next-Reward-Level value
                accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Next-Reward-Level").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesRewards", "levelInterval (every <level> level)").getInt());
                accountManager.saveConfig();
                //Checks the chance of rewards being given
                if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesRewards", "chance to give rewards at interval (1/<number>)").getInt() == 0) {
                    for (int n = 1; n <= ConfigManager.getConfigNode("Skills", skill, "Rewards", "possibleRewards", "numberOf").getInt(); n++) {
                        int rewardQuantityBase = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "number").getInt();
                        //Checks the type of reward
                        switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "type (item/Pokemon/money/command)").getString()) {
                            case "item":
                                //Checks the quantity of the reward
                                switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "modifier (add/multiply/false)").getString()) {
                                    case "false":
                                        //Checks to see if there are multiple items in the reward list
                                        if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                            String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                            for (int r = 0; r <= rewardList.length; r++) {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + rewardList[r] + " " + rewardQuantityBase);
                                            }
                                        } else {
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString() + " " + rewardQuantityBase);
                                        }
                                        break;
                                    case "add":
                                        int rewardQuantityAdd = rewardQuantityBase + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt();
                                        if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                            String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                            for (int r = 0; r <= rewardList.length; r++) {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + rewardList[r] + " " + rewardQuantityAdd);
                                            }
                                        } else {
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString() + " " + rewardQuantityAdd);
                                        }
                                        break;
                                    case "multiply":
                                        int rewardQuantityMulti = rewardQuantityBase * accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt();
                                        if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                            String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                            for (int r = 0; r <= rewardList.length; r++) {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + rewardList[r] + " " + rewardQuantityMulti);
                                            }
                                        } else {
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString() + " " + rewardQuantityMulti);
                                        }
                                        break;
                                }
                                break;
                            case "Pokemon":
                                //Checks the quantity of the reward
                                switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "modifier (add/multiply/false)").getString()) {
                                    case "false":
                                    case "add":
                                    case "multiply":
                                        //Checks to see if there are multiple items in the reward list
                                        if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                            String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                            for (int r = 0; r <= rewardList.length; r++) {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "pokegive " + player.getName() + " " + rewardList[r]);
                                            }
                                        } else {
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "pokegive " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString());
                                        }
                                        break;
                                }
                                break;
                            case "money":
                                EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, container).build();
                                Optional<EconomyService> econ = Sponge.getServiceManager().provide(EconomyService.class);
                                //Checks the quantity of the reward
                                switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "modifier (add/multiply/false)").getString()) {
                                    case "false":
                                        if (econ.isPresent()) {
                                            Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                            Currency defaultCur = econ.get().getDefaultCurrency();
                                            a.get().deposit(defaultCur, BigDecimal.valueOf(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getInt()), Cause.of(eventContext, container));
                                        }
                                        break;
                                    case "add":
                                        if (econ.isPresent()) {
                                            Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                            Currency defaultCur = econ.get().getDefaultCurrency();
                                            a.get().deposit(defaultCur, BigDecimal.valueOf(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getInt() + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skill", skill, "Level").getInt()), Cause.of(eventContext, container));
                                        }
                                        break;
                                    case "multiply":
                                        if (econ.isPresent()) {
                                            Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                            Currency defaultCur = econ.get().getDefaultCurrency();
                                            a.get().deposit(defaultCur, BigDecimal.valueOf(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getInt() * accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skill", skill, "Level").getInt()), Cause.of(eventContext, container));
                                        }
                                        break;
                                }
                                break;
                            case "command":
                                if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                    String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                    for (int c = 0; c < rewardList.length; c++) {
                                        System.out.println(rewardList[c]);
                                        if (rewardList[c].contains("%player%")) {
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardList[c].replace("%player%", player.getName()));
                                        } else {
                                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardList[c]);
                                        }
                                    }
                                } else {
                                    if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains("%player%")) {
                                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().replace("%player%", player.getName()));
                                    } else {
                                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString());
                                    }
                                }
                                break;
                        }
                    }
                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You received rewards for leveling up!"));
                } else {
                    int number = ConfigManager.getConfigNode("Skills", skill, "Rewards", "givesRewards", "chance to give rewards at interval (1/<number>)").getInt();
                    if (PixelSkills.getRandom().nextInt(100) < number) {
                        for (int n = 1; n <= ConfigManager.getConfigNode("Skills", skill, "Rewards", "possibleRewards", "numberOf").getInt(); n++) {
                            int rewardQuantity = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "number").getInt();
                            //Checks the type of reward
                            switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "type (item/Pokemon/money/command)").getString()) {
                                case "item":
                                    //Checks the quantity of the reward
                                    switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "modifier (add/multiply/false)").getString()) {
                                        case "false":
                                            //Checks to see if there are multiple items in the reward list
                                            if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                                String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                                for (int r = 0; r <= rewardList.length; r++) {
                                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + rewardList[r] + " " + rewardQuantity);
                                                }
                                            } else {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString() + " " + rewardQuantity);
                                            }
                                            break;
                                        case "add":
                                            if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                                String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                                for (int r = 0; r <= rewardList.length; r++) {
                                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + rewardList[r] + " " + (rewardQuantity + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt()));
                                                }
                                            } else {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString() + " " + rewardQuantity + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt());
                                            }
                                            break;
                                        case "multiply":
                                            if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                                String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                                for (int r = 0; r <= rewardList.length; r++) {
                                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + rewardList[r] + " " + rewardQuantity * accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt());
                                                }
                                            } else {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "give " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString() + " " + rewardQuantity * accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt());
                                            }
                                            break;
                                    }
                                    break;
                                case "Pokemon":
                                    //Checks the quantity of the reward
                                    switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "modifier (add/multiply/false)").getString()) {
                                        case "false":
                                        case "add":
                                        case "multiply":
                                            //Checks to see if there are multiple items in the reward list
                                            if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                                String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                                for (int r = 0; r <= rewardList.length; r++) {
                                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "pokegive " + player.getName() + " " + rewardList[r]);
                                                }
                                            } else {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "pokegive " + player.getName() + " " + ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString());
                                            }
                                            break;
                                    }
                                    break;
                                case "money":
                                    EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, container).build();
                                    Optional<EconomyService> econ = Sponge.getServiceManager().provide(EconomyService.class);
                                    //Checks the quantity of the reward
                                    switch (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "numberOf", "increases by", "modifier (add/multiply/false)").getString()) {
                                        case "false":
                                            if (econ.isPresent()) {
                                                Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                                Currency defaultCur = econ.get().getDefaultCurrency();
                                                a.get().deposit(defaultCur, BigDecimal.valueOf(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getInt()), Cause.of(eventContext, container));
                                            }
                                            break;
                                        case "add":
                                            if (econ.isPresent()) {
                                                Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                                Currency defaultCur = econ.get().getDefaultCurrency();
                                                a.get().deposit(defaultCur, BigDecimal.valueOf(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getInt() + accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skill", skill, "Level").getInt()), Cause.of(eventContext, container));
                                            }
                                            break;
                                        case "multiply":
                                            if (econ.isPresent()) {
                                                Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                                Currency defaultCur = econ.get().getDefaultCurrency();
                                                a.get().deposit(defaultCur, BigDecimal.valueOf(ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getInt() * accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skill", skill, "Level").getInt()), Cause.of(eventContext, container));
                                            }
                                            break;
                                    }
                                    break;
                                case "command":
                                    if (ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().contains(", ")) {
                                        String[] rewardList = ConfigManager.getConfigNode("Skills", skill, "Rewards", "rewards", "Reward " + n, "reward", "prize").getString().split(", ");
                                        for (int c = 0; c < rewardList.length; c++) {
                                            if (rewardList[c].contains("%player%")) {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardList[c].replace("%player%", player.getName()));
                                            } else {
                                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), rewardList[c]);
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You received rewards for leveling up!"));
                    }
                }
            }
        }

        //Checks for a double level up
        if (didLevelUp(skill, player)) {
            levelUp(skill, player);
        }
    }

    private void setNextPerkIncreaseLevel(String skill, Player player) {
        if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "perk", "starts at level").getInt()) {
            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "nextPerkIncreaseLevel").setValue(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() + ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt());
            accountManager.saveConfig();
            if (Objects.equals(ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance gets higher as level gets higher").getString(), "true")) {
                int chance = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt();
                String mod = ConfigManager.getConfigNode("Skills", skill, "Perks", "in-skill perks", "chance", "increased by", "modifier mode (add/multiply)").getString();
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

    public static boolean getIsMaxLevel(String skill, Player player) {
        if (PixelSkills.INSTANCE.accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level").getInt() == ConfigManager.getConfigNode("Skills", skill, "maxLevel").getInt()) {
            return true;
        }
        return false;
    }

    public static final Random getRandom() {
        return random;
    }

}
