package mod.starsystemdesign.rulecmd.entity;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI.SurveyLevel;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.fleet.CampaignFleet;

import mod.starsystemdesign.rulecmd.Utilities;
import mod.starsystemdesign.rulecmd.debris.StarSystemDesignDebrisScript;

public class StarSystemDesignEntityScript extends BaseCommandPlugin{
    public static Logger log = Global.getLogger(StarSystemDesignDebrisScript.class);

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}

        if(arg == null){
            opts.addOption("Remove object", "StarSystemDesignEntityRemoveOption");
            opts.setEnabled("StarSystemDesignEntityRemoveOption", false);

            SectorEntityToken token = Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, null);
            dialog.getTextPanel().addParagraph("You selected: " + token.getFullName());

            if(canRemoveEntity(token)) opts.setEnabled("StarSystemDesignEntityRemoveOption", true);
            else opts.setEnabled("StarSystemDesignEntityRemoveOption", false);

            opts.addOption("Back", "StarSystemDesignEntityBackOption");
            opts.setShortcut("StarSystemDesignEntityBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        }else{
            switch(arg){
                case "remove":{
                    SectorEntityToken token = Utilities.getClosetEntity(dialog.getInteractionTarget(), 100, null);
                    dialog.getInteractionTarget().getStarSystem().removeEntity(token);
                    dialog.dismiss();
                }
            }
        }
        return true;
    }

    public boolean canRemoveEntity(SectorEntityToken token){
        // Fleet are not removable
        if(token.getClass() == CampaignFleet.class) return false;
        // Can't remove star
        if(token.isStar()) return false;
        // Not market stuff (ex: stellar shade)
        if(token.getMarket() == null) return true;
        else {
            // Market stuff (ex: abandoned station, planet)
            if(canRemoveMarket(token.getMarket())) return true;
            else return false;
        }
    }

    public boolean canRemoveMarket(MarketAPI market){
        // Check netural and full surveyed
        if(!market.getFactionId().equals("neutral")) return false;
        if(market.getSurveyLevel() != SurveyLevel.FULL) return false;
        return true;
    }
}
