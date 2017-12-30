package ninja.andrey.emoji;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.Fitzpatrick;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class DetailActivity extends AppCompatActivity {
    public static final String UNICODE_EXTRA = "UNICODE_EXTRA";
    View detailView;
    Emoji emoji;
    Spinner skinToneSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailView = findViewById(R.id.detail_view);

        emoji = EmojiManager.getByUnicode(getIntent().getStringExtra(UNICODE_EXTRA));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(WordUtils.capitalizeFully(emoji.getAliases().get(0).replace("_", " ")));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyEmoji();
            }
        });

        updateEmoji(emoji, 0);
        ((TextView) findViewById(R.id.emoji_description)).setText(WordUtils.capitalizeFully(emoji.getDescription()));
        ((Button) findViewById(R.id.copy_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyEmoji();
            }
        });

        skinToneSpinner = (Spinner) findViewById(R.id.skin_tone_spinner);
        String[] arraySpinner = new String[] {
                "Skin Tone", "Lightest - 1", "Lighter - 2", "Darker - 3", "Darker - 4", "Darkest - 5"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skinToneSpinner.setAdapter(adapter);
        skinToneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int modifier = Math.max(i - 1, 0);
                updateEmoji(emoji, modifier);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateEmoji(emoji, 0);
            }
        });

        if(!emoji.supportsFitzpatrick()) {
            skinToneSpinner.setVisibility(View.GONE);
        }
    }

    private void copyEmoji() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Emoji", emoji.getUnicode());
        clipboard.setPrimaryClip(clip);

        Snackbar.make(detailView, "Emoji copied to clipboard!", Snackbar.LENGTH_LONG).show();
    }

    private void updateEmoji(Emoji emoji, int modifier) {
        String newValue = emoji.getUnicode();

        if(emoji.supportsFitzpatrick()) {
            newValue = emoji.getUnicode(Fitzpatrick.values()[modifier]);
        }

        ((TextView) findViewById(R.id.emoji_view)).setText(newValue);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
