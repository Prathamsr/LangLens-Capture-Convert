package com.example.langlens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class translate_text extends AppCompatActivity {
    private Spinner source, target;
    private EditText source_text, target_text;
    private ImageView translate, record, speaker_target;
    private String[] arr = {"English",
            "हिन्दी(Hindi)",
            "한국어(Korean)",
            "Dutch",
            "French",
            "German",
            "Italian",
            "Spanish",
            "Swedish",
            "Turkish"};
    private HashMap<String, Integer> map = new HashMap<String, Integer>();
    private TextToSpeech speech;
    private HashMap<String, Locale> speechMap = new HashMap<>();

    Local_translator model = new Local_translator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);
        Intent i = getIntent();
        String source_content = i.getStringExtra("data");
        String source_language = i.getStringExtra("language");
        for (int j = 0; j < arr.length; j++) {
            map.put(arr[j], j);
        }
        source = findViewById(R.id.spinner2);
        target = findViewById(R.id.spinner3);
        source_text = findViewById(R.id.editTextTextMultiLine);
        target_text = findViewById(R.id.editTextTextMultiLine2);
        translate = findViewById(R.id.imageView5);
        record = findViewById(R.id.imageView6);
        ArrayAdapter adapter = new ArrayAdapter(translate_text.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arr);
        source.setAdapter(adapter);
        target.setAdapter(adapter);
        source.setSelection(map.get(source_language));
        source_text.setText(source_content);
        speaker_target = findViewById(R.id.imageView8);
        speaker_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        String tarlang = target.getSelectedItem().toString();
                        String[] support_lang = {"English",
                                "한국어(Korean)",
                                "French",
                                "German",
                                "Italian",
                        };

                        boolean test = Arrays.asList(support_lang).contains(tarlang);
                        if (test) {
                            speech.setLanguage(speechMap.get(target.getSelectedItem().toString()));
                            String text = target_text.getText().toString();
                            speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        } else {
                            Toast t = Toast.makeText(getApplicationContext(), "Language Not Supported By Speaker", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
            }
        });


        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (source.getSelectedItem().toString().equals("English")) {
                        model.translate_from_english(target.getSelectedItem().toString());
                    } else if (target.getSelectedItem().toString().equals("English")) {
                        model.translate_to_english(source.getSelectedItem().toString());
                    } else {
                        model.translate_different_languages(source.getSelectedItem().toString(), target.getSelectedItem().toString());
                    }
                } catch (Exception e) {
                    Toast t = Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT);
                    t.show();
                    target_text.setText(e.toString() + source_text.getText().toString());
                }

            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
                startActivityForResult(i, 111);
            }
        });
    }

    private void set_speech_languages() {
        String[] arr = {"English",

                "한국어(Korean)",

                "French",
                "German",
                "Italian",
        };
        speechMap.put("English", Locale.ENGLISH);
        speechMap.put("한국어(Korean)", Locale.KOREAN);
        speechMap.put("French", Locale.FRENCH);
        speechMap.put("German", Locale.GERMAN);
        speechMap.put("Italian", Locale.ITALIAN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            source_text.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            //source_text.setText(model.translate_from_english_for_speech(source.getSelectedItem().toString().trim()));
        }
    }

    class Local_translator {
        HashMap<String, Translator> from_translator = new HashMap<String, Translator>();
        HashMap<String, Translator> to_translator = new HashMap<String, Translator>();
        final Translator englishHindiTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.HINDI).build());
        final Translator englishKoreanTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.KOREAN).build());
        final Translator englishDutchTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.DUTCH).build());
        final Translator englishFrenchTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.FRENCH).build());
        final Translator englishGermanTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.GERMAN).build());
        final Translator englishItalianTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.ITALIAN).build());
        final Translator englishSpanishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH).build());
        final Translator englishSwedishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SWEDISH).build());
        final Translator englishTurkishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TURKISH).build());
        final Translator HindienglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.HINDI)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator KoreanenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.KOREAN)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator DutchenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.DUTCH)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator FrenchenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.FRENCH)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator GermanenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator ItalianenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ITALIAN)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator SpanishenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.SPANISH)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator SwedishenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.SWEDISH)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());
        final Translator TurkishenglishTranslator = Translation.getClient(new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.TURKISH)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build());

        DownloadConditions conditions = new DownloadConditions.Builder().build();

        Local_translator() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    download_Translators();
                }
            }).start();
        }

        private void download_Translators() {
            englishKoreanTranslator
                    .downloadModelIfNeeded(conditions).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("한국어(Korean)", englishKoreanTranslator);
                        }
                    });
            englishHindiTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("हिन्दी(Hindi)", englishHindiTranslator);
                        }
                    });
            englishDutchTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("Dutch", englishDutchTranslator);
                        }
                    });
            englishFrenchTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("French", englishFrenchTranslator);
                        }
                    });
            englishGermanTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("German", englishGermanTranslator);
                        }
                    });
            englishItalianTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("Italian", englishItalianTranslator);
                        }
                    });
            englishSpanishTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("Spanish", englishSpanishTranslator);
                        }
                    });
            englishSwedishTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("Swedish", englishSwedishTranslator);
                        }
                    });
            englishTurkishTranslator
                    .downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            from_translator.put("Turkish", englishTurkishTranslator);
                        }
                    });
            HindienglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("हिन्दी(Hindi)", HindienglishTranslator);
                        }
                    });
            KoreanenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("한국어(Korean)", KoreanenglishTranslator);
                        }
                    });
            DutchenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("Dutch", DutchenglishTranslator);
                        }
                    });
            FrenchenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("French", FrenchenglishTranslator);
                        }
                    });
            GermanenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("German", GermanenglishTranslator);
                        }
                    });
            ItalianenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("Italian", ItalianenglishTranslator);
                        }
                    });
            SpanishenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("Spanish", SpanishenglishTranslator);
                        }
                    });
            SwedishenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("Swedish", SwedishenglishTranslator);
                        }
                    });
            TurkishenglishTranslator
                    .downloadModelIfNeeded()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            to_translator.put("Turkish", TurkishenglishTranslator);
                        }
                    });
        }

        public void translate_to_english(String lang) {
            String sour = source_text.getText().toString().trim();
            to_translator
                    .get(lang)
                    .translate(sour)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            target_text.setText(s);
                            Toast t = Toast.makeText(getApplicationContext(), "Translated", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast t = Toast.makeText(getApplicationContext(), "Change Language", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
        }

        public void translate_from_english(String lang) {
            String sour = source_text.getText().toString().trim();
            from_translator
                    .get(lang)
                    .translate(sour)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            target_text.setText(s);
                            Toast t = Toast.makeText(getApplicationContext(), "Translated", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast t = Toast.makeText(getApplicationContext(), "Change Language", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
        }

        public void translate_different_languages(String source_lang, String target_lang) {
            String sour = source_text.getText().toString().trim();
            to_translator
                    .get(source_lang)
                    .translate(sour)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            from_translator
                                    .get(target_lang)
                                    .translate(s)
                                    .addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            target_text.setText(s);
                                            Toast t = Toast.makeText(getApplicationContext(), "Translated", Toast.LENGTH_SHORT);
                                            t.show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast t = Toast.makeText(getApplicationContext(), "Change target Language", Toast.LENGTH_SHORT);
                                            t.show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast t = Toast.makeText(getApplicationContext(), "Change Source Language", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
        }

        public String translate_from_english_for_speech(String lang) {
            String sour = source_text.getText().toString().trim();
            final String[] result = {sour};
            if (!lang.equals("English")) {
                from_translator
                        .get(lang)
                        .translate(sour)
                        .addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                result[0] = s;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast t = Toast.makeText(getApplicationContext(), "Change Language", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        });
            }
            return result[0];
        }
    }
}