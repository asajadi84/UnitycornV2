package ir.unitycorn.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.unitycorn.app.DarkModeCapable;
import ir.unitycorn.app.R;
import ir.unitycorn.app.util.IabHelper;
import ir.unitycorn.app.util.IabResult;
import ir.unitycorn.app.util.Inventory;
import ir.unitycorn.app.util.Purchase;

public class UpgradeActivity extends AppCompatActivity implements DarkModeCapable {

    SharedPreferences settingsPrefs;

    //Myket fields
    IabHelper iabHelper;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass on the activity result to the helper for handling
        if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            if (iabHelper != null) iabHelper.disposeWhenFinished();
            iabHelper = null;
        }catch(Exception e){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        //Myket stuff
        try{
            iabHelper = new IabHelper(this, "myket api key");
            iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        Toasty.error(UpgradeActivity.this, getResources().getString(R.string.upgrade_toast_iab_initialize_failed)).show();
                    }

                    List<String> inAppPurchaseIds = new ArrayList<>();
                    inAppPurchaseIds.add("unitycorn_plus");

                    try {
                        iabHelper.queryInventoryAsync(false, inAppPurchaseIds, null, new IabHelper.QueryInventoryFinishedListener() {
                            @Override
                            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                                if (result.isFailure()) {
                                    return;
                                }
                                else {
                                    if(inventory.hasPurchase("unitycorn_plus")){
                                        upgradeAppToPlus(getResources().getString(R.string.upgrade_toast_already_upgraded));
                                    }
                                }
                            }
                        });
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            Toasty.error(UpgradeActivity.this, getResources().getString(R.string.upgrade_toast_market_not_found)).show();
        }


        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if(settingsPrefs.getBoolean("dark_mode", false)){
            makeViewsDark();
        }

        Typeface iranSans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);
        Typeface iranSansBold = ResourcesCompat.getFont(getApplicationContext(), R.font.iransansb);

        //Initialize Toasty
        Toasty.Config.getInstance()
                .setToastTypeface(iranSans)
                .allowQueue(false)
                .apply();

        TextView title = findViewById(R.id.upgrade_title);
        title.setTypeface(iranSansBold);

        MaterialButton payButton = findViewById(R.id.activity_upgrade_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upgradeToPremiumPay(view);
            }
        });
    }



    @Override
    public void makeViewsDark() {
        findViewById(R.id.upgrade_layout_container).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ((TextView)findViewById(R.id.upgrade_title)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    public void upgradeToPremiumPay(View view) {
        try {
            iabHelper.launchPurchaseFlow(this, "unitycorn_plus", 1, new IabHelper.OnIabPurchaseFinishedListener() {
                public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                    if (result.isFailure()) {
                        Toasty.error(UpgradeActivity.this, getResources().getString(R.string.upgrade_toast_payment_cancelled)).show();
                        return;
                    }
                    else if (purchase.getSku().equals("unitycorn_plus")) {
                        upgradeAppToPlus("default");
                    }
                }
            }, "payload-string");
        }catch(Exception e){
            Toasty.error(UpgradeActivity.this, getResources().getString(R.string.upgrade_toast_payment_error)).show();
        }
    }

    public void upgradeAppToPlus(String customMessage){
        settingsPrefs.edit().putBoolean("unitycorn_plus", true).apply();
        MainActivity.activityShouldReset = true;
        if(customMessage.equals("default")){
            Toasty.success(UpgradeActivity.this, getResources().getString(R.string.upgrade_toast_payment_succeeded)).show();
        }else{
            Toasty.success(UpgradeActivity.this, customMessage).show();
        }

        finish();
    }
}
