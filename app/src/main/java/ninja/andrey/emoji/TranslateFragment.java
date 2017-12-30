package ninja.andrey.emoji;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.ios.IosEmojiProvider;

public class TranslateFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int page;
    private EditText emojiInput;
    private EmojiTextView emojiOutput;

    public static TranslateFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TranslateFragment fragment = new TranslateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        emojiInput = (EditText) getView().findViewById(R.id.input);
        emojiOutput = (EmojiTextView) getView().findViewById(R.id.emoji_output);
        emojiInput.addTextChangedListener(emojiInputTextWatcher);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ViewGroup rootView = (ViewGroup) getView().findViewById(R.id.translate_root);
            LayoutTransition layoutTransition = rootView.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(emojiOutput, "See what your emoji look like on an iPhone!", Snackbar.LENGTH_LONG).show();
            }
        }, 500);
    }

    TextWatcher emojiInputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String newInput = charSequence.toString();
            emojiOutput.setText(newInput);
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }
}