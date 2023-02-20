package mod.starsystemdesign.rulecmd.nascentgravitywell;

import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import mod.starsystemdesign.rulecmd.Utilities;

import org.lwjgl.input.Keyboard;

public class StarSystemDesignNascentGravityWellScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}

        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        SectorEntityToken gravitywell = Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, null);

        dialog.getTextPanel().addPara("You selected: " + gravitywell.getName());

        if(arg == null){
            opts.addOption("Remove", "StarSystemDesignNascentGravityWellRemoveOption");
            opts.setEnabled("StarSystemDesignNascentGravityWellRemoveOption", true);

            opts.addOption("Back", "StarSystemDesignNascentGravityWellBackOption");
            opts.setEnabled("StarSystemDesignNascentGravityWellBackOption", true);
            opts.setShortcut("StarSystemDesignNascentGravityWellBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        }else{
            switch(arg){
                case "remove":{
                    LocationAPI location = gravitywell.getContainingLocation();
                    location.removeEntity(gravitywell);
                    dialog.dismiss();
                    break;
                }
            }
        }
        return true;
    }
}
