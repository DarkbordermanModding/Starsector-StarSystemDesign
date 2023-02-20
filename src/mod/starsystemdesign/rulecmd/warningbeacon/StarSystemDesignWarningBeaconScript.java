package mod.starsystemdesign.rulecmd.warningbeacon;

import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.comm.IntelManagerAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.misc.WarningBeaconIntel;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import mod.starsystemdesign.rulecmd.Utilities;

import org.lwjgl.input.Keyboard;

public class StarSystemDesignWarningBeaconScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}

        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        SectorEntityToken warningBeacon = Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, Tags.WARNING_BEACON);

        dialog.getTextPanel().addPara("You selected: " + warningBeacon.getName());

        if(arg == null){
            opts.addOption("Remove", "StarSystemDesignWarningBeaconRemoveOption");
            opts.setEnabled("StarSystemDesignWarningBeaconRemoveOption", true);

            opts.addOption("Back", "StarSystemDesignWarningBeaconBackOption");
            opts.setEnabled("StarSystemDesignWarningBeaconBackOption", true);
            opts.setShortcut("StarSystemDesignWarningBeaconBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        }else{
            switch(arg){
                case "remove":{
                    LocationAPI location = warningBeacon.getContainingLocation();

                    // Remove related warning beacon intel
                    IntelManagerAPI manager = Global.getSector().getIntelManager();
                    IntelInfoPlugin warningBeaconIntel = null;
                    for(IntelInfoPlugin intel: manager.getIntel(WarningBeaconIntel.class)){
                        SectorEntityToken intelToken = intel.getMapLocation(null);
                        if(intelToken == warningBeacon) warningBeaconIntel = intel;
                    }
                    if(warningBeaconIntel != null) manager.removeIntel(warningBeaconIntel);
                    location.removeEntity(warningBeacon);

                    opts.addOption("Back", "StarSystemDesignWarningBeaconBackOption");
                    opts.setEnabled("StarSystemDesignWarningBeaconBackOption", true);
                    opts.setShortcut("StarSystemDesignWarningBeaconBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
                    break;
                }
            }
        }
        return true;
    }
}
