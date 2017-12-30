package ninja.andrey.emoji;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.ios.IosEmojiProvider;

public class MainActivity extends AppCompatActivity {
    EditText emojiInput;
    EmojiTextView emojiOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmojiManager.install(new IosEmojiProvider()); // must be before setContentView
        setContentView(R.layout.activity_main);

        emojiInput = (EditText) findViewById(R.id.input);
        emojiOutput = (EmojiTextView) findViewById(R.id.emoji_output);

        emojiInput.addTextChangedListener(emojiInputTextWatcher);
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
}
