package mod.starsystemdesign.rulecmd.hyperspacejumppoint;

import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI.JumpDestination;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.JumpPoint;

import mod.starsystemdesign.rulecmd.Utilities;

import org.lwjgl.input.Keyboard;

public class StarSystemDesignHyperspaceJumpPointScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}

        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        JumpPoint jumpPoint = (JumpPoint)Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, Tags.JUMP_POINT);

        dialog.getTextPanel().addPara("You selected: " + jumpPoint.getName());

        if(arg == null){
            opts.addOption("Remove", "StarSystemDesignHyperspaceJumpPointRemoveOption");
            opts.setEnabled("StarSystemDesignHyperspaceJumpPointRemoveOption", true);

            opts.addOption("Back", "StarSystemDesignHyperspaceJumpPointBackOption");
            opts.setEnabled("StarSystemDesignHyperspaceJumpPointBackOption", true);
            opts.setShortcut("StarSystemDesignHyperspaceJumpPointBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        }else{
            switch(arg){
                case "remove":{
                    for(JumpDestination dest: jumpPoint.getDestinations()){
                        // Only remove pairing jump point
                        if(dest.getDestination().hasTag(Tags.JUMP_POINT)){
                            StarSystemAPI system = dest.getDestination().getStarSystem();
                            system.removeEntity(dest.getDestination());
                        }
                    }
                    LocationAPI location = jumpPoint.getContainingLocation();
                    location.removeEntity(jumpPoint);
                    dialog.dismiss();
                    break;
                }
            }
        }
        return true;
    }
}
