package mod.starsystemdesign.ability;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.impl.campaign.RuleBasedInteractionDialogPluginImpl;
import com.fs.starfarer.api.impl.campaign.abilities.BaseDurationAbility;

public class StarSystemDesignAbility extends BaseDurationAbility {

    @Override
	protected void applyEffect(float amount, float level) {
		CampaignFleetAPI fleet = getFleet();
		if (fleet == null) return;
    }
	@Override
	protected void deactivateImpl() {cleanupImpl();}

	@Override
	protected void cleanupImpl() {
		CampaignFleetAPI fleet = getFleet();
		if (fleet == null) return;
    }

    @Override
	protected void activateImpl() {
		if (entity.isPlayerFleet()) {
			entity.getMemory().set("$option", "StarSystemDesignOption");
            Global.getSector().getCampaignUI().showInteractionDialog(
				new RuleBasedInteractionDialogPluginImpl(), entity
			);
        }
    }
}
