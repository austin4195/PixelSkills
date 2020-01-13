package com.lypaka.pixelskills.Skills;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.config.ConfigManager;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

public class FierceBattler {
    public FierceBattler (PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }

    public PixelSkills plugin;
    public ConfigurationNode config;
    public SkillsAccountManager accountManager;

    @SubscribeEvent
    public void onTrainerDefeat (BeatTrainerEvent e) {
        Player player = (Player) e.player;
        if (ConfigManager.getConfigNode("Skills", "Fierce Battler", "EXP", "Tasks", "Defeating Trainers", "isEnabled").getValue().equals(true)) {
            int exp = ConfigManager.getConfigNode("Skills", "Fierce Battler", "EXP", "Tasks", "Defeating Trainers", "EXP gained per").getInt() * ConfigManager.getConfigNode("Skills", "Fierce Battler", "EXP", "expModifier").getInt();
            player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " You gained " + exp + " Fierce Battler EXP points!"));
            plugin.addPoints("Fierce Battler", exp, player);
            if (plugin.didLevelUp("Fierce Battler", player)) {
                plugin.levelUp("Fierce Battler", player);
                if (ConfigManager.getConfigNode("Skills", "Fierce Battler", "Perks", "in-skill perks", "isEnabled").getValue().equals(true)) {
                    if (accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fierce Battler", "Level").getInt() == ConfigManager.getConfigNode("Skills", "Fierce Battler", "Perks", "in-skill perks", "perk", "starts at level").getInt() ||
                            accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fierce Battler", "nextIncreaseLevel").getInt() - ConfigManager.getConfigNode("Skills", "Fierce Battler", "Perks", "in-skill perks", "chance", "increased by", "every <level> level").getInt() == accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fierce Battler", "Level").getInt()) {
                        Random rand = new Random();
                        if (ConfigManager.getConfigNode("Skills", "Fierce Battler", "Perks", "in-skill perks", "chance gets higher as level gets higher").getValue().equals(true)) {
                            int number = accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fierce Battler", "chance at perks").getInt();
                            if (number != 0) {
                                int rng = rand.nextInt(accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", "Fierce Battler", "chance at perks").getInt() - 1) + 1;
                                if (rng == 1) {
                                    player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The Trainer paid a little extra money!"));
                                    EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, PixelSkills.getContainer()).build();
                                    Optional<EconomyService> econ = Sponge.getServiceManager().provide(EconomyService.class);
                                    if (econ.isPresent()) {
                                        Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                        Currency defaultCur = econ.get().getDefaultCurrency();
                                        a.get().deposit(defaultCur, BigDecimal.valueOf((e.trainer.getWinMoney() + (e.trainer.getWinMoney() * 0.25))), Cause.of(eventContext, PixelSkills.getContainer()));
                                    }
                                }
                            }
                        } else {
                            int rng = rand.nextInt(ConfigManager.getConfigNode("Skills", "Fierce Battler", "Perks", "in-skill perks", "chance of triggering at task completed (1/<number>) (1/100 = 1% chance)").getInt() - 1) + 1;
                            if (rng == 1) {
                                player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "PixelSkills", TextColors.GOLD, "]", TextColors.WHITE, " The Trainer paid a little extra money!"));
                                EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, PixelSkills.getContainer()).build();
                                Optional<EconomyService> econ = Sponge.getServiceManager().provide(EconomyService.class);
                                if (econ.isPresent()) {
                                    Optional<UniqueAccount> a = econ.get().getOrCreateAccount(player.getUniqueId());
                                    Currency defaultCur = econ.get().getDefaultCurrency();
                                    a.get().deposit(defaultCur, BigDecimal.valueOf((e.trainer.getWinMoney() + (e.trainer.getWinMoney() * 0.25))), Cause.of(eventContext, PixelSkills.getContainer()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
