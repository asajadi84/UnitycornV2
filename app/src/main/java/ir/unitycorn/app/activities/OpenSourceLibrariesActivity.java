package ir.unitycorn.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

import ir.unitycorn.app.R;
import ir.unitycorn.app.viewmodels.OpenSourceLibrariesActivityViewModel;

public class OpenSourceLibrariesActivity extends AppCompatActivity {

    OpenSourceLibrariesActivityViewModel openSourceLibrariesActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Locale farsi = new Locale("fa", "IR");
        getResources().getConfiguration().setLocale(farsi);

        setContentView(R.layout.activity_open_source_libraries);

        openSourceLibrariesActivityViewModel = ViewModelProviders.of(this).get(OpenSourceLibrariesActivityViewModel.class);

        ((ScrollView)findViewById(R.id.libraries_scroll_view)).setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_RIGHT);
        ((TextView)findViewById(R.id.libraries_text_view)).setText(openSourceLibrariesActivityViewModel.getLibraries());
    }
}
