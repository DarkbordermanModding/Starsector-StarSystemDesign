package mod.starsystemdesign.rulecmd.stablelocation;

import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;


public class StarSystemDesignStableLocationScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        List<SectorEntityToken> stableLocations = dialog.getInteractionTarget().getStarSystem().getEntitiesWithTag(Tags.STABLE_LOCATION);
        boolean hasStableLocations = !stableLocations.isEmpty();

        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}
        if(arg == null){
            opts.addOption("Create a stable location", "StarSystemDesignStableLocationCreateOption");

            StarSystemAPI system = Global.getSector().getPlayerFleet().getStarSystem();
            if(Misc.getNumStableLocations(system) < Global.getSettings().getInt("stablelocationcount")){
                opts.setEnabled("StarSystemDesignStableLocationCreateOption", true);
            }else{
                opts.setEnabled("StarSystemDesignStableLocationCreateOption", false);
            }

            opts.addOption("Move a stable location to current location", "StarSystemDesignStableLocationMoveOption");
            if(hasStableLocations) opts.setEnabled("StarSystemDesignStableLocationMoveOption", true);
            else opts.setEnabled("StarSystemDesignStableLocationMoveOption", false);

            opts.addOption("Back", "StarSystemDesignStableLocationBackOption");
            opts.setShortcut("StarSystemDesignStableLocationBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        }else{
            switch(arg){
                case "create": {
                    StarSystemGenerator.addStableLocations(dialog.getInteractionTarget().getStarSystem(), 1);
                    dialog.getTextPanel().addParagraph("You created the stable location to your current location");
                    opts.addOption("Back", "StarSystemDesignStableLocationBackOption");
                    opts.setShortcut("StarSystemDesignStableLocationBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
                    break;
                }
                case "move":{
                    SectorEntityToken stableLocation = stableLocations.get(0);
                    SectorEntityToken player = dialog.getInteractionTarget();
                    stableLocation.setFixedLocation(player.getLocation().x, player.getLocation().y);
                    if (player.getOrbit() != null) stableLocation.setOrbit(player.getOrbit());
                    dialog.getTextPanel().addParagraph("You move the stable location to your current location");
                    opts.addOption("Back", "StarSystemDesignStableLocationBackOption");
                    opts.setShortcut("StarSystemDesignStableLocationBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
                    break;
                }
            }
        }

        return true;
    }
}
