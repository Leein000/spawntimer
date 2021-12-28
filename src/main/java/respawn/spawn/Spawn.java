package respawn.spawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.UUID;

public class Spawn extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {

                for(Player p : Bukkit.getOnlinePlayers()) {

                    if(p.getGameMode().equals(GameMode.SPECTATOR) && deathtime.containsKey(p.getUniqueId())) {

                        long timePassed = (System.currentTimeMillis() - deathtime.get(p.getUniqueId())) / 1000;
                        if(timePassed >= 5) {
                            p.setGameMode(gamemode.get(p.getUniqueId()));
                            p.resetTitle();
                            Location spawn = p.getBedSpawnLocation();
                            p.teleport(spawn);
                            deathtime.remove(p.getUniqueId());
                        }
                    }
                }
            }
        }, 0, 0);

    }

    @Override
    public void onDisable() {

    }

    HashMap<UUID, Long> deathtime = new HashMap<>();
    HashMap<UUID, GameMode> gamemode = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player p =e.getEntity();
        GameMode gm = p.getGameMode();

        p.setGameMode(GameMode.SPECTATOR);
        p.sendTitle(ChatColor.AQUA + "사망하셨습니다", "5초 후 부활합니다", 0, 100, 0);
        deathtime.put(p.getUniqueId(), System.currentTimeMillis());
        gamemode.put(p.getUniqueId(), gm);
    }
}
