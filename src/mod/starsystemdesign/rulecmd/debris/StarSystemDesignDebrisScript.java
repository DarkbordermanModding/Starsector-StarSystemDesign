package mod.starsystemdesign.rulecmd.debris;

import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.CampaignTerrain;

public class StarSystemDesignDebrisScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        CampaignTerrain terrain = (CampaignTerrain)dialog.getInteractionTarget().getOrbitFocus();
        DebrisFieldTerrainPlugin debris = (DebrisFieldTerrainPlugin)terrain.getPlugin();
        debris.getParams().lastsDays = 1f;
        dialog.dismiss();
        return true;
    }
}
