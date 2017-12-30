package ninja.andrey.emoji;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.ios.IosEmojiProvider;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    List<Emoji> emojiList = new LinkedList<>();
    ListView emojiListView;
    EditText searchInput;
    EmojiArrayAdapter emojiArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.vanniktech.emoji.EmojiManager.install(new IosEmojiProvider()); // must be before setContentView
        setContentView(R.layout.activity_search);

        emojiList = getEmojiResults("");

        emojiListView = (ListView) findViewById(R.id.emoji_list);
        emojiArrayAdapter = new EmojiArrayAdapter(this, emojiList);
        emojiListView.setAdapter(emojiArrayAdapter);

        searchInput = (EditText) findViewById(R.id.search_input);
        searchInput.addTextChangedListener(searchInputTextWatcher);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<Emoji> getEmojiResults(String search) {
        Collection<Emoji> emojiAll = EmojiManager.getAll();
        List<Emoji> emojiResults = new LinkedList<>();
        for(Emoji emoji : emojiAll) {
            String description = emoji.getDescription();
            Log.d("SEARCH", search + "??" + description);
            if(description.contains(search)) {
                emojiResults.add(emoji);
            }
        }
        return emojiResults;
    }

    private class EmojiArrayAdapter extends ArrayAdapter<Emoji> {
        private final Context context;
        private final List<Emoji> values;

        EmojiArrayAdapter(Context context, List<Emoji> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_emoji, parent, false);

            TextView emojiDescription = (TextView) rowView.findViewById(R.id.emoji_description);
            emojiDescription.setText(values.get(position).getDescription());

            TextView emojiPreview = (TextView) rowView.findViewById(R.id.emoji_preview);
            emojiPreview.setText(values.get(position).getUnicode());

            return rowView;
        }
    }

    TextWatcher searchInputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            emojiList.clear();
            emojiList.addAll(getEmojiResults(charSequence.toString()));
            emojiArrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };
}
