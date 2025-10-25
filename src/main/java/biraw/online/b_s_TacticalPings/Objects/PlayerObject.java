package biraw.online.b_s_TacticalPings.Objects;

import biraw.online.b_s_TacticalPings.B_s_TacticalPings;
import biraw.online.b_s_TacticalPings.Managers.ConfigManager;
import biraw.online.b_s_TacticalPings.Managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class PlayerObject {
    public final Player player;
    public final Color color;
    public TeamObject team;

    private ArrayList<Integer> tasks = new ArrayList<Integer>();
    private ArrayList<BlockDisplay> blockPings = new ArrayList<>();

    public PlayerObject(Player player, TeamObject team){
        this.player = player;
        this.team = team;
        this.color = this.team.getNewMemberColor();
    }

    public ArrayList<BlockDisplay> getBlockPings() {
        return blockPings;
    }

    public void playerLeaves(){
        for(BlockDisplay bd : blockPings) bd.remove();
        team.playerObjects.remove(this);
        team.AllColors.add(color);

        if (team.creator == this)
        {
            if (team.playerObjects.isEmpty()){
                TeamManager.teams.remove(this);
                return;
            };

            PlayerObject newLeader = team.playerObjects.getFirst();
            team.creator = newLeader;
            newLeader.player.sendMessage("§eSince your previous team leader left you have been elected as the new one in §6"+team.name+"§e!");
        }
    }

    public void addBlockPing(Location location) {
        BlockDisplay display = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);

        display.setBlock(Material.WHITE_STAINED_GLASS.createBlockData());
        display.setInvisible(true);
        display.setGlowing(true);
        display.setGlowColorOverride(color);

        for(Player plr : Bukkit.getOnlinePlayers())
        {
            plr.hideEntity(B_s_TacticalPings.getInstance(),display);
            if (team.isPartOfTeam(plr) != null)
                plr.showEntity(B_s_TacticalPings.getInstance(),display);
        }

        // It is just smaller than the block and it's inside
        Transformation transform = new Transformation(
                new Vector3f(0.01f, 0.01f, 0.01f),
                new Quaternionf(),
                new Vector3f(0.98f, 0.98f, 0.98f),
                new Quaternionf()
        );
        display.setTransformation(transform);

        blockPings.add(display);
        if (blockPings.size()>ConfigManager.getPingLimit()){
            blockPings.getFirst().remove();
            blockPings.removeFirst();
        }
        tasks.add(Bukkit.getScheduler().runTaskLater(
                B_s_TacticalPings.getInstance(),
                ()->{blockPings.remove(display);display.remove();},
                ConfigManager.getPingDuration() *20L).getTaskId()
        );
    }
}
