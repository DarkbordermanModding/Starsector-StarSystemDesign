package mod.starsystemdesign.rulecmd.jumppoint;

import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;


public class StarSystemDesignJumpPointScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}

        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        StarSystemAPI starSystem = dialog.getInteractionTarget().getStarSystem();
        List<SectorEntityToken> jumpPoints = starSystem.getJumpPoints();
        boolean hasJumpPoint = !jumpPoints.isEmpty();

        if(arg == null){
            dialog.getOptionPanel().addOption("Create jump point", "StarSystemDesignJumpPointCreateOption");
            opts.setEnabled("StarSystemDesignJumpPointCreateOption", false); // TODO
            dialog.getOptionPanel().addOption("Move jump point", "StarSystemDesignJumpPointMoveOption");
            if(hasJumpPoint) opts.setEnabled("StarSystemDesignJumpPointMoveOption", true);
            else opts.setEnabled("StarSystemDesignJumpPointMoveOption", false);
            dialog.getOptionPanel().addOption("Remove jump point", "StarSystemDesignJumpPointRemoveOption");
            if(hasJumpPoint && jumpPoints.size() > 1) opts.setEnabled("StarSystemDesignJumpPointRemoveOption", true);
            else opts.setEnabled("StarSystemDesignJumpPointRemoveOption", false);
            dialog.getOptionPanel().addOption("Back", "StarSystemDesignJumpPointBackOption");
            opts.setEnabled("StarSystemDesignJumpPointBackOption", true);
            opts.setShortcut("StarSystemDesignJumpPointBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        }else{
            switch(arg){
                case "create":{
                    //TODO: create jump point
                    break;
                }
                case "move":{
                    SectorEntityToken token = jumpPoints.get(0);
                    SectorEntityToken player = dialog.getInteractionTarget();
                    token.setFixedLocation(player.getLocation().x, player.getLocation().y);
                    dialog.getOptionPanel().addOption("Back", "StarSystemDesignJumpPointBackOption");
                    opts.setEnabled("StarSystemDesignJumpPointBackOption", true);
                    opts.setShortcut("StarSystemDesignJumpPointBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
                    break;
                }
                case "remove":{
                    JumpPointAPI jumpPoint = (JumpPointAPI)jumpPoints.get(0);
                    LocationAPI location = jumpPoint.getContainingLocation();
                    location.removeEntity(jumpPoint);
                    // For now, I can't figure out how to remove jump point in hyperspace
                    dialog.getOptionPanel().addOption("Back", "StarSystemDesignJumpPointBackOption");
                    opts.setEnabled("StarSystemDesignJumpPointBackOption", true);
                    opts.setShortcut("StarSystemDesignJumpPointBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
                    break;
                }
            }
        }
        return true;
    }
}
