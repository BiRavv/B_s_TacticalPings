package biraw.online.b_s_TactikalPings.Listeners;

import biraw.online.b_s_TactikalPings.Managers.ConfigManager;
import biraw.online.b_s_TactikalPings.Managers.TeamManager;
import biraw.online.b_s_TactikalPings.Objects.PlayerObject;
import biraw.online.b_s_TactikalPings.Objects.TeamObject;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class PlayerPingL implements Listener {
    @EventHandler
    private void PlayerPing(PlayerInteractEvent event){
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.COMPASS) return;

        Player player = event.getPlayer();

        TeamObject team = TeamManager.getTeamOf(player);
        if ( team == null) return;

        Vector direction = player.getEyeLocation().getDirection().normalize();

        // Raycast
        RayTraceResult result = player.getWorld().rayTraceBlocks(
                player.getEyeLocation(),
                direction,
                ConfigManager.getPingReach(),
                FluidCollisionMode.NEVER,
                true
        );

        // Create the ping
        PlayerObject plr_obj = team.isPartOfTeam(player);

        if (plr_obj == null) return; // not necessary but let me keep it
        if (result == null) return;
        if (result.getHitBlock() == null) return;
        plr_obj.addBlockPing(result.getHitBlock().getLocation());
    }
}
