package biraw.online.b_s_TactikalPings.Objects;

import biraw.online.b_s_TactikalPings.B_s_TacticalPings;
import biraw.online.b_s_TactikalPings.Managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamObject {

    public ArrayList<PlayerObject> playerObjects = new ArrayList<>();
    public PlayerObject creator;
    public final String name;


    public TeamObject(Player creator, String name){
        this.name = name;
        PlayerObject creator_object = new PlayerObject(creator,this);
        playerObjects.add(creator_object);
        this.creator = creator_object;
        TeamManager.teams.add(this);
    }

    // returns null if not
    @Nullable
    public PlayerObject isPartOfTeam(Player player) {
        for (PlayerObject plr : playerObjects)
            if (plr.player.getUniqueId() == player.getUniqueId())
                return plr;

        return null;
    }

    // Coloring
    private static final Random r = new Random();

    public ArrayList<Color> AllColors = new ArrayList<>(List.of(
            Color.RED, Color.BLUE, Color.LIME, Color.YELLOW, Color.ORANGE,
            Color.AQUA, Color.PURPLE, Color.OLIVE, Color.FUCHSIA, Color.WHITE
    ));

    public Color getNewMemberColor(){
        if (AllColors.isEmpty()) return  Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        return AllColors.removeFirst();
    }

    public void addMember(Player player){
        for (PlayerObject member : playerObjects) member.player.sendMessage("§2"+player.getName()+"§a joined your team!");
        playerObjects.add(new PlayerObject(player,this));
    }
}
