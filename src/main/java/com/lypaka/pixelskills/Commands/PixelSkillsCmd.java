package com.lypaka.pixelskills.Commands;

import com.lypaka.pixelskills.GUI.MainPage;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class PixelSkillsCmd implements CommandExecutor {
    public static CommandSpec create() {
        return CommandSpec.builder()
                .executor(new PixelSkillsCmd())
                .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = (Player) src;
        MainPage.openMainGUI(player);
        return CommandResult.success();
    }
}
