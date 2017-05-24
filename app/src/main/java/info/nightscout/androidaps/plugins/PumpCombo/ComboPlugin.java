package info.nightscout.androidaps.plugins.PumpCombo;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import info.nightscout.androidaps.BuildConfig;
import info.nightscout.androidaps.Config;
import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.R;
import info.nightscout.androidaps.data.PumpEnactResult;
import info.nightscout.androidaps.interfaces.InsulinInterface;
import info.nightscout.androidaps.interfaces.PluginBase;
import info.nightscout.androidaps.interfaces.PumpDescription;
import info.nightscout.androidaps.interfaces.PumpInterface;
import info.nightscout.androidaps.plugins.NSClientInternal.data.NSProfile;
import info.nightscout.androidaps.plugins.PumpCombo.events.EventComboPumpUpdateGUI;
import info.nightscout.androidaps.plugins.PumpMDI.MDIFragment;
import info.nightscout.androidaps.plugins.TreatmentsFromHistory.TreatmentsFromHistoryPlugin;
import info.nightscout.utils.DateUtil;

/**
 * Created by mike on 05.08.2016.
 */
public class ComboPlugin implements PluginBase, PumpInterface {
    private static Logger log = LoggerFactory.getLogger(ComboPlugin.class);

    boolean fragmentEnabled = false;
    boolean fragmentVisible = false;

    PumpDescription pumpDescription = new PumpDescription();

    public ComboPlugin() {
        pumpDescription.isBolusCapable = true;
        pumpDescription.bolusStep = 0.5d;

        pumpDescription.isExtendedBolusCapable = false;
        pumpDescription.extendedBolusStep = 0d;

        pumpDescription.isTempBasalCapable = false;
        pumpDescription.lowTempBasalStyle = PumpDescription.NONE;
        pumpDescription.highTempBasalStyle = PumpDescription.NONE;
        pumpDescription.maxHighTempPercent = 0;
        pumpDescription.maxHighTempAbsolute = 0;
        pumpDescription.lowTempPercentStep = 0;
        pumpDescription.lowTempAbsoluteStep = 0;
        pumpDescription.lowTempPercentDuration = 0;
        pumpDescription.lowTempAbsoluteDuration = 0;
        pumpDescription.highTempPercentStep = 0;
        pumpDescription.highTempAbsoluteStep = 0d;
        pumpDescription.highTempPercentDuration = 0;
        pumpDescription.highTempAbsoluteDuration = 0;

        pumpDescription.isSetBasalProfileCapable = false;
        pumpDescription.basalStep = 0d;
        pumpDescription.basalMinimumRate = 0d;

        pumpDescription.isRefillingCapable = false;
    }

    @Override
    public String getFragmentClass() {
        return ComboFragment.class.getName();
    }

    @Override
    public String getName() {
        return MainApp.instance().getString(R.string.combopump);
    }

    @Override
    public String getNameShort() {
        // use long name as fallback (not visible in tabs)
        return getName();
    }

    @Override
    public boolean isEnabled(int type) {
        return type == PUMP && fragmentEnabled;
    }

    @Override
    public boolean isVisibleInTabs(int type) {
        return type == PUMP && fragmentVisible;
    }

    @Override
    public boolean canBeHidden(int type) {
        return true;
    }

    @Override
    public boolean hasFragment() {
        return true;
    }

    @Override
    public boolean showInList(int type) {
        return true;
    }

    @Override
    public void setFragmentEnabled(int type, boolean fragmentEnabled) {
        if (type == PUMP) this.fragmentEnabled = fragmentEnabled;
    }

    @Override
    public void setFragmentVisible(int type, boolean fragmentVisible) {
        if (type == PUMP) this.fragmentVisible = fragmentVisible;
    }

    @Override
    public int getType() {
        return PluginBase.PUMP;
    }

    @Override
    public String treatmentPlugin() {
        return TreatmentsFromHistoryPlugin.class.getName();
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public int setNewBasalProfile(NSProfile profile) {
        return FAILED;
    }

    @Override
    public boolean isThisProfileSet(NSProfile profile) {
        return false;
    }

    @Override
    public Date lastDataTime() {
        return new Date();
    }

    @Override
    public void refreshDataFromPump(String reason) {
// this is called regulary from keepalive
    }

    @Override
    public double getBaseBasalRate() {
        return 0d;
    }

    @Override
    public PumpEnactResult deliverTreatment(InsulinInterface insulinType, Double insulin, Integer carbs, Context context) {
        PumpEnactResult result = new PumpEnactResult();
        return result;
    }

    @Override
    public void stopBolusDelivering() {
    }

    @Override
    public PumpEnactResult setTempBasalAbsolute(Double absoluteRate, Integer durationInMinutes) {
        PumpEnactResult result = new PumpEnactResult();
        return result;
    }

    @Override
    public PumpEnactResult setTempBasalPercent(Integer percent, Integer durationInMinutes) {
        PumpEnactResult result = new PumpEnactResult();
        return result;
    }

    @Override
    public PumpEnactResult setExtendedBolus(Double insulin, Integer durationInMinutes) {
        PumpEnactResult result = new PumpEnactResult();
        return result;
    }

    @Override
    public PumpEnactResult cancelTempBasal() {
        PumpEnactResult result = new PumpEnactResult();
        return result;
    }

    @Override
    public PumpEnactResult cancelExtendedBolus() {
        PumpEnactResult result = new PumpEnactResult();
        return result;
    }

    @Override
    public JSONObject getJSONStatus() {
        JSONObject pump = new JSONObject();
        JSONObject status = new JSONObject();
        JSONObject extended = new JSONObject();
        try {
            status.put("status", "normal");
            extended.put("Version", BuildConfig.VERSION_NAME + "-" + BuildConfig.BUILDVERSION);
            try {
                extended.put("ActiveProfile", MainApp.getConfigBuilder().getActiveProfile().getProfile().getActiveProfile());
            } catch (Exception e) {
            }
            status.put("timestamp", DateUtil.toISOString(new Date()));

// more info here .... look at dana plugin

            pump.put("status", status);
            pump.put("extended", extended);
            pump.put("clock", DateUtil.toISOString(new Date()));
        } catch (JSONException e) {
        }
        return pump;
    }

    @Override
    public String deviceID() {
// Serial number here
        return "Combo";
    }

    @Override
    public PumpDescription getPumpDescription() {
        return pumpDescription;
    }

    @Override
    public String shortStatus(boolean veryShort) {
        return deviceID();
    }

}


// If you want update fragment call
//        MainApp.bus().post(new EventComboPumpUpdateGUI());
// fragment should fetch data from plugin and display status, buttons etc ...