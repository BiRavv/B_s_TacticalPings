package biraw.online.b_s_TactikalPings.Listeners;

import biraw.online.b_s_TactikalPings.Managers.TeamManager;
import biraw.online.b_s_TactikalPings.Objects.PlayerObject;
import biraw.online.b_s_TactikalPings.Objects.TeamObject;
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
