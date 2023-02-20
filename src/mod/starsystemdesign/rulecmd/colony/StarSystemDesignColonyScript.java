package mod.starsystemdesign.rulecmd.colony;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;
import com.fs.starfarer.api.util.Misc;

import mod.starsystemdesign.rulecmd.DarkMagicThread;

import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

public class StarSystemDesignColonyScript extends BaseCommandPlugin
{
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap)
    {
        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        String arg = null;
        try{
            arg = params.get(0).getString(memoryMap);
        }catch(IndexOutOfBoundsException e){}
        if(arg == null){
            opts.addOption("Create a colony", "StarSystemDesignColonyCreateOption");
            opts.setEnabled("StarSystemDesignColonyCreateOption", true);

            opts.addOption("Back", "StarSystemDesignColonyBackOption");
            opts.setShortcut("StarSystemDesignColonyBackOption", Keyboard.KEY_ESCAPE, false, false, false, false);
        }else{
            switch(arg){
                case "create": {
                    SectorEntityToken token = dialog.getInteractionTarget();
                    SectorEntityToken built = createColony(token);
                    dialog.dismiss();
                    // BIG WARNING
                    // If the player doesn't view the colony management screen within a few days of market creation
                    // There can be a bug related to population growth (Instantly grow to maximum)
                    // The dialog will be closed and can't spawn a new dialog
                    // use a thread to open another dialog
                    Thread t = new Thread(new DarkMagicThread(built));
                    t.start();
                    break;
                }
            }
        }
        return true;
    }

    public SectorEntityToken createColony(SectorEntityToken token){
        // Identity suffix
        CampaignClockAPI clock = Global.getSector().getClock();
        String suffix = clock.getCycle() + "_" + clock.getMonth() + "_" + clock.getDay() + "_" + clock.getHour();
        // Increment
        int x = Global.getSector().getMemory().getInt("$StarSystemDesignColonyIndex");

        // Create colony entity
        LocationAPI loc = token.getContainingLocation();
        SectorEntityToken built = loc.addCustomEntity(
            "station_" + suffix,
            "Side station " + x,
            "station_side03",
            token.getFaction().getId()
        );
        built.setFixedLocation(token.getLocation().x, token.getLocation().y);
        if (token.getOrbit() != null) built.setOrbit(token.getOrbit());

        // Create market
        MarketAPI market = Global.getFactory().createMarket(
            "AddStation_" + suffix,
            "Side station " + x,
            3
        );
        market.setSize(3);
        Global.getSector().getMemory().set("$StarSystemDesignColonyIndex", x + 1);

        market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        market.setPrimaryEntity(built);

        market.setFactionId(Global.getSector().getPlayerFleet().getFaction().getId());
        market.setPlayerOwned(true);

        market.addCondition("population_3");
        market.addCondition("mild_climate");

        market.addIndustry("population");
        market.addIndustry("spaceport");
        market.setFreePort(true);

        market.addSubmarket("storage");
        StoragePlugin storage = (StoragePlugin)market.getSubmarket("storage").getPlugin();
        storage.setPlayerPaidToUnlock(true);
        market.addSubmarket("local_resources");

        built.setMarket(market);
        Global.getSector().getEconomy().addMarket(market, true);
        built.setFaction(Global.getSector().getPlayerFleet().getFaction().getId());

        // Update survey and industries
        for (MarketConditionAPI condition: market.getConditions()){
            condition.setSurveyed(true);
        }
        for (Industry industry: market.getIndustries()){
            industry.doPreSaveCleanup();
            industry.doPostSaveRestore();
        }
        return built;
    }
}