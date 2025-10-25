package biraw.online.b_s_TacticalPings.Managers;

import biraw.online.b_s_TacticalPings.Objects.PlayerObject;
import biraw.online.b_s_TacticalPings.Objects.TeamObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeamCommandManager implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player))
        {
            Bukkit.getLogger().warning("Only players can use this command: /pt");
            return true;
        }

        if(strings.length<1) return false;
        if (strings[0].equalsIgnoreCase("create"))
        {
            if(strings.length<2) {
                player.sendMessage("§c/pt create <name>");
                return true;
            }
            if (TeamManager.getTeamOf(player) != null)
            {
                player.sendMessage("§cYou already have a team!");
                return true;
            }

            new TeamObject(player,strings[1]);
            player.sendMessage("§aYou have created your team successfully: §2"+strings[1]);
            return true;
        }
        else if (strings[0].equalsIgnoreCase("leave"))
        {
            TeamObject to = TeamManager.getTeamOf(player);
            if (to!=null) to.isPartOfTeam(player).playerLeaves();
            player.sendMessage("§aYou left your team...");
            return true;
        }
        else if (strings[0].equalsIgnoreCase("invite"))
        {
            if(strings.length<2) {
                player.sendMessage("§c/pt invite <player>");
                return true;
            }

            Player invited = Bukkit.getServer().getPlayer(strings[1]);
            if (invited == null){
                player.sendMessage("§cThe invited player is not valid!");
                return true;
            }

            TeamObject leadTeam = TeamManager.getLeadTeamOf(player);
            if (leadTeam!=null)
            {
                TeamManager.sendInvite(invited,player,leadTeam);
                player.sendMessage("§aInvitation sent to §2"+invited.getName());
                invited.sendMessage("§eYou have been invited to §6"+leadTeam.name+"§e by §6"+player.getName());
                invited.sendMessage(
                        new ComponentBuilder("§eTo accept this, click here and press enter: ")
                                .append("§6[Accept Invite]")
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pt accept"))
                                .create()
                );
                return true;
            }
            TeamObject team = TeamManager.getTeamOf(player);
            if (team == null){
                player.sendMessage("§cYou are not in a team!");
                return true;
            }

            team.creator.player.sendMessage("§6"+player.getName()+"§e requests §6"+invited.getName()+"§e to be added to your team.");
            team.creator.player.spigot().sendMessage(
                    new ComponentBuilder("§eTo accept this, click here and press enter: ")
                            .append("§6[Invite Player]")
                            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pt invite " + invited.getName()))
                            .create()
            );
            return true;
        }
        else if (strings[0].equalsIgnoreCase("accept"))
        {
            TeamManager.acceptInvitation(player);
            return true;
        }
        else if (strings[0].equalsIgnoreCase("list"))
        {
            TeamObject team = TeamManager.getTeamOf(player);
            if (team == null) {
                player.sendMessage("§cYou are not in a team!");
                return true;
            }
            player.sendMessage("§2Your teammates:");
            for(PlayerObject plr_obj : team.playerObjects)
            {
                if (plr_obj == team.creator)
                    player.sendMessage("    §6"+plr_obj.player.getName()+" ♛");
                else
                    player.sendMessage("    §a"+plr_obj.player.getName());
            }
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 2 && strings[0].equals("invite"))
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();

        if(strings.length>1) return List.of();

        return List.of("create","leave","invite","accept","list");
    }
}
