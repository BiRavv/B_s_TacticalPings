package biraw.online.b_s_TacticalPings.Listeners;

import biraw.online.b_s_TacticalPings.Managers.TeamManager;
import biraw.online.b_s_TacticalPings.Objects.TeamObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerLeaveL implements Listener {
    @EventHandler
    private void PlayerLeaves(PlayerQuitEvent event){
        TeamObject team = TeamManager.getTeamOf(event.getPlayer());

        if(team==null) return;
        team.isPartOfTeam(event.getPlayer()).playerLeaves();
    }
}
