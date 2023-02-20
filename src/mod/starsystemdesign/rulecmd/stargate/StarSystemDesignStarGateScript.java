package mod.starsystemdesign.rulecmd.stargate;

import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import org.lwjgl.input.Keyboard;

public class StarSystemDesignStarGateScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}

        List<SectorEntityToken> gates = dialog.getInteractionTarget().getStarSystem().getEntitiesWithTag(Tags.GATE);
        boolean hasGate = !gates.isEmpty();
        if(arg == null){
            opts.addOption("Build star gate", "StarSystemDesignStarGateBuildOption");
            if(!hasGate) opts.setEnabled("StarSystemDesignStarGateBuildOption", true);
            else opts.setEnabled("StarSystemDesignStarGateBuildOption", false);

            opts.addOption("Move gate to current location", "StarSystemDesignStarGateMoveOption");
            if(hasGate) opts.setEnabled("StarSystemDesignStarGateMoveOption", true);
            else opts.setEnabled("StarSystemDesignStarGateMoveOption", false);

            opts.addOption("Back", "StarSystemDesignStarGateBackOption");
            opts.setShortcut("StarSystemDesignStarGateBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        } else{
            switch(arg){
                case "move":{
                    SectorEntityToken gate = gates.get(0);
                    SectorEntityToken player = dialog.getInteractionTarget();
                    gate.setFixedLocation(player.getLocation().x, player.getLocation().y);
                    if (player.getOrbit() != null) gate.setOrbit(player.getOrbit());
                    dialog.getTextPanel().addParagraph("You move the stargate to your current location");
                    opts.addOption("Back", "StarSystemDesignStarGateBackOption");
                    opts.setShortcut("StarSystemDesignStarGateBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
                    break;
                }
                case "build":{
                    SectorEntityToken target = dialog.getInteractionTarget();
                    SectorEntityToken built = target.getStarSystem().addCustomEntity(
                        "gate_" + target.getStarSystem().getId(),
                        target.getStarSystem().getNameWithTypeShort() + " Gate",
                        Entities.INACTIVE_GATE,
                        target.getFaction().getId()
                    );
                    built.setFixedLocation(target.getLocation().x, target.getLocation().y);
                    if (target.getOrbit() != null) built.setOrbit(target.getOrbit());
                    dialog.getTextPanel().addParagraph("You build the stargate");
                    opts.addOption("Back", "StarSystemDesignStarGateBackOption");
                    opts.setShortcut("StarSystemDesignStarGateBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
                    break;
                }
            }
        }
        return true;
    }
}
