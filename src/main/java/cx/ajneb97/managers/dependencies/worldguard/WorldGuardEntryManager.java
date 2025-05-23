package cx.ajneb97.managers.dependencies.worldguard;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.Set;


/**
 * @author Weby &amp; Anrza (info@raidstone.net)
 * @version 1.0.0
 * @since 3/3/19
 */
public class WorldGuardEntryManager extends Handler implements Listener {

    public final PluginManager pm = Bukkit.getPluginManager();
    public static final Factory factory = new Factory();

    public static class Factory extends Handler.Factory<WorldGuardEntryManager> {
        @Override
        public WorldGuardEntryManager create(Session session) {
            return new WorldGuardEntryManager(session);
        }
    }

    public WorldGuardEntryManager(Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> left, MoveType moveType)
    {
        RegionsChangedEvent rce = new RegionsChangedEvent(player.getUniqueId(), left, entered);
        pm.callEvent(rce);
        if(rce.isCancelled()) return false;

        RegionsEnteredEvent ree = new RegionsEnteredEvent(player.getUniqueId(), entered);
        pm.callEvent(ree);
        if(ree.isCancelled()) return false;

        RegionsLeftEvent rle = new RegionsLeftEvent(player.getUniqueId(), left);
        pm.callEvent(rle);
        if(rle.isCancelled()) return false;

        for(ProtectedRegion r : entered) {
            RegionEnteredEvent regentered = new RegionEnteredEvent(player.getUniqueId(), r);
            pm.callEvent(regentered);
            if(regentered.isCancelled()) return false;
        }


        for(ProtectedRegion r : left) {
            RegionLeftEvent regleft = new RegionLeftEvent(player.getUniqueId(), r);
            pm.callEvent(regleft);
            if(regleft.isCancelled()) return false;
        }
        return true;
    }





}
