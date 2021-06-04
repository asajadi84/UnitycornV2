package ir.unitycorn.app;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.tapsell.sdk.Tapsell;
import shortbread.Shortbread;

public class ApplicationManager extends Application {

    @Override
    public void onCreate() {

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                        .setDefaultFontPath("iransans.ttf")
                        .build()))
                .build());


        Shortbread.create(this);

        Tapsell.initialize(this, "tapsell api key");


        super.onCreate();
    }
}
