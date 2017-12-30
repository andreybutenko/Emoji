package ninja.andrey.emoji;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SearchFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int page;

    List<Emoji> emojiList = new LinkedList<>();
    ListView emojiListView;
    EditText searchInput;
    EmojiArrayAdapter emojiArrayAdapter;

    public static SearchFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
        emojiList = getEmojiResults("");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        emojiListView = (ListView) getView().findViewById(R.id.emoji_list);
        emojiArrayAdapter = new EmojiArrayAdapter(getContext(), emojiList);
        emojiListView.setAdapter(emojiArrayAdapter);
        emojiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedUnicode = emojiList.get(i).getUnicode();

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.UNICODE_EXTRA, selectedUnicode);
                startActivity(intent);
            }
        });

        searchInput = (EditText) getView().findViewById(R.id.search_input);
        searchInput.addTextChangedListener(searchInputTextWatcher);
    }

    private List<Emoji> getEmojiResults(String search) {
        Collection<Emoji> emojiAll = EmojiManager.getAll();
        List<Emoji> emojiResults = new LinkedList<>();
        for(Emoji emoji : emojiAll) {
            String description = emoji.getDescription();
            String primaryAlias = emoji.getAliases().get(0).replace("_", " ");
            if(description.contains(search) || primaryAlias.contains(search)) {
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_emoji, parent, false);

            TextView emojiDescription = (TextView) rowView.findViewById(R.id.emoji_description);
            emojiDescription.setText(WordUtils.capitalizeFully(values.get(position).getDescription()));

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
