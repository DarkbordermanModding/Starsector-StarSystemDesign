package mod.starsystemdesign.rulecmd;

import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.NascentGravityWell;

import org.lwjgl.input.Keyboard;

public class StarSystemDesignEntryScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        boolean isInHyperspace = dialog.getInteractionTarget().isInHyperspace();

        if(!isInHyperspace){
            dialog.getOptionPanel().addOption("Manage stable locations", "StarSystemDesignStableLocationOption");
            opts.setEnabled("StarSystemDesignStableLocationOption", true);

            dialog.getOptionPanel().addOption("Manage star gate", "StarSystemDesignStarGateOption");
            opts.setEnabled("StarSystemDesignStarGateOption", true);

            dialog.getOptionPanel().addOption("Manage system jump point", "StarSystemDesignJumpPointOption");
            opts.setEnabled("StarSystemDesignJumpPointOption", true);

            dialog.getOptionPanel().addOption("Manage system colony", "StarSystemDesignColonyOption");
            opts.setEnabled("StarSystemDesignColonyOption", true);

            dialog.getOptionPanel().addOption("Select current orbiting object", "StarSystemDesignDebrisOption");
            opts.setEnabled("StarSystemDesignDebrisOption", false);
            SectorEntityToken orbit = dialog.getInteractionTarget().getOrbitFocus();
            if(orbit != null) opts.setEnabled("StarSystemDesignDebrisOption", true);

        }else{
            dialog.getOptionPanel().addOption("Manage nearby jump point", "StarSystemDesignHyperspaceJumpPointOption");
            SectorEntityToken jumpPoint = Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, Tags.JUMP_POINT);
            if(jumpPoint != null) opts.setEnabled("StarSystemDesignHyperspaceJumpPointOption", true);
            else opts.setEnabled("StarSystemDesignHyperspaceJumpPointOption", false);

            dialog.getOptionPanel().addOption("Manage nearby warning beacon", "StarSystemDesignWarningBeaconOption");
            SectorEntityToken warningBeacon = Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, Tags.WARNING_BEACON);
            if(warningBeacon != null) opts.setEnabled("StarSystemDesignWarningBeaconOption", true);
            else opts.setEnabled("StarSystemDesignWarningBeaconOption", false);

            dialog.getOptionPanel().addOption("Manage nearby nascent gravity well", "StarSystemDesignNascentGravityWellOption");
            SectorEntityToken token = Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, null);
            if(token != null && token instanceof NascentGravityWell) opts.setEnabled("StarSystemDesignNascentGravityWellOption", true);
            else opts.setEnabled("StarSystemDesignNascentGravityWellOption", false);
        }
        dialog.getOptionPanel().addOption("Leave", "StarSystemDesignLeaveOption");
        opts.setShortcut("StarSystemDesignLeaveOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        return true;
    }
}
