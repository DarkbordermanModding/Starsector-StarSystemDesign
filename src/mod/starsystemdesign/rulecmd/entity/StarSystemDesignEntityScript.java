package mod.starsystemdesign.rulecmd.entity;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import mod.starsystemdesign.rulecmd.debris.StarSystemDesignDebrisScript;

public class StarSystemDesignEntityScript extends BaseCommandPlugin{
    public static Logger log = Global.getLogger(StarSystemDesignDebrisScript.class);

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        return true;
    }
}
