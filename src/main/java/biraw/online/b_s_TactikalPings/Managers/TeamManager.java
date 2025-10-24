package biraw.online.b_s_TactikalPings.Managers;

import biraw.online.b_s_TactikalPings.B_s_TacticalPings;
import biraw.online.b_s_TactikalPings.Objects.PlayerObject;
import biraw.online.b_s_TactikalPings.Objects.TeamObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TeamManager {
    public static ArrayList<TeamObject> teams = new ArrayList<>();

    @Nullable
    public static TeamObject getTeamOf(Player player){
        for(TeamObject team : teams)
            if (team.isPartOfTeam(player) != null)
                return team;

        return null;
    }

    @Nullable
    public static TeamObject getLeadTeamOf(Player player){
        for(TeamObject team : teams)
            if (team.creator.player.getUniqueId() == player.getUniqueId())
                return team;

        return null;
    }

    private static record Invitation(Player player, TeamObject team) {}

    private static ArrayList<Invitation> invitations = new ArrayList<>();

    public static void sendInvite(Player player, Player inviter,TeamObject team){
        if (player == inviter){
            inviter.sendMessage("§cYou cannot invite yourself!");
            return;
        }

        TeamObject currentTeam = TeamManager.getTeamOf(player);
        if (currentTeam == team){
            inviter.sendMessage("§cThis player is already in your team!");
            return;
        }
        if (currentTeam != null){
            inviter.sendMessage("§cThis player is in an other team already!");
            return;
        }

        boolean alreadyInvited = invitations.stream()
                .anyMatch(inv -> inv.player().equals(player));

        if (alreadyInvited){
            inviter.sendMessage("§cThis player is already invited!");
            return;
        }

        Invitation inv = new Invitation(player,team);
        invitations.add(inv);
        Bukkit.getScheduler().runTaskLater(B_s_TacticalPings.getInstance(),()->{
            invitations.remove(inv);
        },20*20);
    }

    public static void acceptInvitation(Player accepter){
        Invitation invitation = invitations.stream()
                .filter(inv -> inv.player().equals(accepter))
                .findFirst()
                .orElse(null);

        if (invitation == null) {
            accepter.sendMessage("§cYou are not invited anywhere!");
            return;
        }

        invitation.team.addMember(accepter);
        invitations.remove(invitation);
        accepter.sendMessage("§aYou have entered the group: §2"+invitation.team.name);
    }

    public static void closeAllTeams(){
        for(TeamObject team : new ArrayList<>(teams))
            for (PlayerObject plr : new ArrayList<>(team.playerObjects))
                plr.playerLeaves();
    }
}
