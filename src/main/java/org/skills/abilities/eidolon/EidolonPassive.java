package org.skills.abilities.eidolon;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.skills.abilities.Ability;
import org.skills.api.events.EidolonImbalanceChangeEvent;
import org.skills.data.managers.SkilledPlayer;
import org.skills.main.SkillsConfig;
import org.skills.main.SkillsPro;
import org.skills.main.locale.SkillsLang;
import org.skills.utils.versionsupport.VersionSupport;

import java.awt.*;

public class EidolonPassive extends Ability {
    public EidolonPassive() {
        super("Eidolon", "passive");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEidolonChangeForm(EidolonImbalanceChangeEvent event) {
        Player player = event.getPlayer();
        if (SkillsConfig.isInDisabledWorld(player.getLocation())) return;
        SkilledPlayer info = this.checkup(player);
        if (info == null) return;

        double scaling = this.getScaling(info);
        Bukkit.getScheduler().runTask(SkillsPro.get(), () -> VersionSupport.heal(event.getPlayer(), scaling));

        if (!info.showReadyMessage()) return;
        if (event.getNewForm() == EidolonForm.LIGHT) {
            ParticleDisplay.simple(player.getLocation(), Particle.REDSTONE).withColor(Color.WHITE, 1.0f).offset(0.5, 0.5, 0.5).withCount(100).spawn();
            XSound.play(player, getExtra(info, "sounds.imbalance.light").getString());
            SkillsLang.Skill_Eidolon_Turn_Light.sendMessage(player);
        } else {
            ParticleDisplay.simple(player.getLocation(), Particle.REDSTONE).withColor(Color.BLACK, 1.0f).withCount(100).offset(0.5, 0.5, 0.5).spawn();
            XSound.play(player, getExtra(info, "sounds.imbalance.dark").getString());
            SkillsLang.Skill_Eidolon_Turn_Dark.sendMessage(player);
        }
    }
}
