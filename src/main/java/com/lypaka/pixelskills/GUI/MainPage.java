package com.lypaka.pixelskills.GUI;

import com.codehusky.huskyui.StateContainer;
import com.codehusky.huskyui.states.Page;
import com.codehusky.huskyui.states.action.ActionType;
import com.codehusky.huskyui.states.action.runnable.RunnableAction;
import com.codehusky.huskyui.states.element.ActionableElement;
import com.codehusky.huskyui.states.element.Element;
import com.google.common.collect.Lists;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.config.SkillsAccountManager;
import com.pixelmonmod.pixelmon.config.*;
import com.pixelmonmod.pixelmon.items.PixelmonItemBlock;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Objects;

public class MainPage {
    private static SkillsAccountManager accountManager;

    public MainPage(PixelSkills plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigNode();
        this.accountManager = plugin.getAccountManager();
    }
    public PixelSkills plugin;
    public ConfigurationNode config;

    public static void openMainGUI (Player player) {
        StateContainer container = new StateContainer();
        Page.PageBuilder main = Page.builder()
                .setAutoPaging(false)
                .setInventoryDimension(InventoryDimension.of(9, 5))
                .setTitle(Text.of(TextColors.DARK_RED, "PixelSkills"))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44}) {
            main.putElement(i, new Element(EmptyPage.empty()));
        }
        main.putElement(0, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openBreederStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItems.hourglassSilver)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Breeder"))
                        .build()
        ));
        main.putElement(2, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openCatcherStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItemsPokeballs.pokeBall)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Catcher"))
                        .build()
        ));
        main.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openCrafterStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) Objects.requireNonNull(PixelmonItemBlock.getByNameOrId("pixelmon:clothed_table")))
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Crafter"))
                        .build()
        ));
        main.putElement(6, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openFierceBattlerStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItems.trainerEditor)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Fierce Battler"))
                        .build()
        ));
        main.putElement(8, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openFishermanStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItems.goodRod)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Fisherman"))
                        .build()
        ));
        main.putElement(18, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openShinyHunterStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) Objects.requireNonNull(PixelmonItemBlock.getByNameOrId("pixelmon:shinypokedoll_venusaur")))
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Shiny Hunter"))
                        .build()
        ));
        main.putElement(20, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openLegendaryMasterStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) Objects.requireNonNull(PixelmonItemBlock.getByNameOrId("pixelmon:pokedoll_regirock")))
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Legendary Master"))
                        .build()
        ));
        main.putElement(22, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openScientistStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItems.potion)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Scientist"))
                        .build()
        ));
        main.putElement(24, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openBotanistStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItemsApricorns.apricornYellow)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Botanist"))
                        .build()
        ));
        main.putElement(26, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMinerStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItemsTools.icestonePickaxeItem)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Miner"))
                        .build()
        ));
        main.putElement(36, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openArchaeologistStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItemsFossils.coverFossilCovered)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Archaeologist"))
                        .build()
        ));
        main.putElement(38, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openBlacksmithStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItemsTools.hammerIron)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Blacksmith"))
                        .build()
        ));
        main.putElement(40, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openTreasureHunterStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) Objects.requireNonNull(PixelmonItemBlock.getByNameOrId("pixelmon:poke_chest")))
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Treasure Hunter"))
                        .build()
        ));
        main.putElement(42, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openBossConquerorStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType((ItemType) PixelmonItemsHeld.altarianite)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Boss Conqueror"))
                        .build()
        ));
        main.putElement(44, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openPokeExterminatorStatsPage(container, player).openState(player, "modifiers")),
                ItemStack.builder()
                        .itemType(ItemTypes.BARRIER)
                        .add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Poke Exterminator"))
                        .build()
        ));
        container.setInitialState(main.build("main"));
        container.launchFor(player);
    }

    private static StateContainer openPokeExterminatorStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Poke Exterminator"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Poke Exterminator", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Poke Exterminator", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Poke Exterminator", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openBossConquerorStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Boss Conqueror"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Boss Conqueror", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Boss Conqueror", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Boss Conqueror", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openTreasureHunterStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Treasure Hunter"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Treasure Hunter", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Treasure Hunter", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Treasure Hunter", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openBlacksmithStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Blacksmith"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Blacksmith", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Blacksmith", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Blacksmith", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openArchaeologistStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Archaeologist"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Archaeologist", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Archaeologist", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Archaeologist", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openMinerStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Miner"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Miner", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Miner", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Miner", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openBotanistStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Botanist"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Botanist", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Botanist", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Botanist", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openScientistStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Scientist"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Scientist", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Scientist", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Scientist", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openLegendaryMasterStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Legendary Master"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Legendary Master", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Legendary Master", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Legendary Master", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openShinyHunterStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Shiny Hunter"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Shiny Hunter", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Shiny Hunter", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Shiny Hunter", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openFishermanStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Fisherman"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Fisherman", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Fisherman", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Fisherman", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openFierceBattlerStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Fierce Battler"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Fierce Battler", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Fierce Battler", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Fierce Battler", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openCrafterStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Crafter"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Crafter", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Crafter", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Crafter", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openCatcherStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Catcher"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Catcher", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Catcher", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Catcher", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    private static StateContainer openBreederStatsPage(StateContainer container, Player player) {
        if (container.hasState("modifiers")) {
            container.removeState("modifiers");
        }
        Page.PageBuilder builder = Page.builder()
                .setAutoPaging(false)
                .setTitle(Text.of(TextColors.BLACK, "Breeder"))
                .setInventoryDimension(InventoryDimension.of(9, 1))
                .setEmptyStack(EmptyPage.empty());
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}) {
            builder.putElement(i, new Element(EmptyPage.empty()));
        }
        builder.putElement(1, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("Level"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getlevelfromSkill("Breeder", player))))
                        .build()

        ));
        builder.putElement(4, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPfromSkill("Breeder", player))))
                        .build()

        ));
        builder.putElement(7, new ActionableElement(
                new RunnableAction(container, ActionType.NONE, "", c -> openMainGUI(player)),
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, Text.of("EXP-to-Levelup"))
                        .add(Keys.ITEM_LORE, Lists.newArrayList(Text.of(getEXPTOLEVELUPfromSkill("Breeder", player))))
                        .build()

        ));
        container.addState(builder.build("modifiers"));
        return container;
    }

    public static int getlevelfromSkill(String skill, Player player) {
        ConfigurationNode node = new SkillsAccountManager(PixelSkills.INSTANCE).accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "Level");
        int level = node.getInt();
        return level;
    }

    public static int getEXPfromSkill (String skill, Player player) {
        ConfigurationNode node = new SkillsAccountManager(PixelSkills.INSTANCE).accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP");
        int exp = node.getInt();
        return exp;
    }

    public static int getEXPTOLEVELUPfromSkill (String skill, Player player) {
        ConfigurationNode node = new SkillsAccountManager(PixelSkills.INSTANCE).accountManager.getAccountsConfig().getNode(player.getUniqueId().toString(), "Skills", skill, "EXP-to-Levelup");
        int exp2l = node.getInt();
        return exp2l;
    }

}
